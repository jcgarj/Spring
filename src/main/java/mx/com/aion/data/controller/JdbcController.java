package mx.com.aion.data.controller;
import mx.com.aion.data.io.ReadDataTestingFile;
import mx.com.aion.data.io.SendJSON;
import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.models.dao.ServiceDAOImp;
import mx.com.aion.data.models.entity.Cliente;
import mx.com.aion.data.models.entity.ResultsTest;
import mx.com.aion.data.models.entity.ServiceTagsParameters;
import mx.com.aion.data.models.entity.entity.DsbCfgAdocQuery;
import mx.com.aion.data.util.ValidateTags;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class JdbcController {
    private static final Logger LOGGER = Logger.getLogger(JdbcController.class.getName());

    String sql = "SELECT VC_QUERY_ID, N_QUERY_ACTIVE, VC_QUERY_DESCRIPTION, VC_QUERY_VERSION, VC_QUERY_STATEMENT, VC_QUERY_DATE, VC_QUERY_AUTHOR, VC_MODULE_ID "
            + "FROM DSB_CFG_ADOC_QUERY "
            + "WHERE VC_QUERY_ID=? ";

    String paramId = "GRUPO_RSVALIDAUSUARIO";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IDsbCfgAdcQueryDao dsbCfgAdocQuery;

    @GetMapping(value = "/clientes")
    public List<Cliente> getClientes(){
        return jdbcTemplate.query("select * from clientes", new BeanPropertyRowMapper<>(Cliente.class));
    }

    @GetMapping("/listaReglas")
    public List<DsbCfgAdocQuery> getReglas(){
        return jdbcTemplate.query("select * from DSB_CFG_ADOC_QUERY", new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
    }

    @GetMapping("/reglas")
    public List<DsbCfgAdocQuery> getQuery(){

        Optional<DsbCfgAdocQuery> dsbCfgAdocQuery1 = null;
        String id = "GRUPO_RSVALIDAUSUARIO";
        dsbCfgAdocQuery1 = dsbCfgAdocQuery.findById(id);
        dsbCfgAdocQuery1.get().getVcModuleId();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
    }

    @GetMapping("/borrarreglas")
    public List<DsbCfgAdocQuery> deleteQuery(Model model){
        dsbCfgAdocQuery.deleteById("query1");
        dsbCfgAdocQuery.deleteById("query2");
        System.out.println();
        System.out.println("Se elimino correctamente");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
    }

    @GetMapping("/revisar")
    public DsbCfgAdocQuery find(Model model){
        DsbCfgAdocQuery dsbCfgAdocQuery = jdbcTemplate.queryForObject(sql, new Object[]{paramId}, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
        return dsbCfgAdocQuery;
    }

    @GetMapping("/test")
    public ArrayList<ResultsTest> test(){
        ArrayList<ResultsTest> executeTesting = executeTesting("rsValidaCredenciales");

        return executeTesting;
    }

    public ArrayList<ResultsTest> executeTesting(String serviceName){
        ArrayList<ResultsTest> resultados = new ArrayList<>();
        ServiceDAOImp serviceDAOImp = new ServiceDAOImp(jdbcTemplate);
        String path = serviceDAOImp.obtainPathMatrixByServiceName(serviceName);
        String urlService = serviceDAOImp.obtainUrlServiceByServiceName(serviceName);
        Map<String, ServiceTagsParameters> dataParameters = serviceDAOImp.obtainDataParameters(serviceName);
        List<String> tagsNames = serviceDAOImp.obtainTagsNames(serviceName);
        ArrayList<Map> collectionTestingTags = new ArrayList<>();
        SendJSON sendJSON = new SendJSON();

        ReadDataTestingFile readDataTestingFile = new ReadDataTestingFile(tagsNames, serviceName, path);
        try {
            collectionTestingTags = readDataTestingFile.readExcel();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo", e);

        } catch (InvalidFormatException e) {
            LOGGER.log(Level.SEVERE, "Formato incorrecto", e);
        }

        for (Map collectionTestingTag : collectionTestingTags) {
            String testExpectedResult = "";
            ResultsTest resultsTest = new ResultsTest();
            StringBuilder validateDataTesting = new StringBuilder();
            StringBuilder request = new StringBuilder();
            ValidateTags validateTags = new ValidateTags(dataParameters, collectionTestingTag);

            for (String tagName : tagsNames) {

                request.append(tagName).append(": ").append(collectionTestingTag.get(tagName)).append("\n");
                validateDataTesting.append(tagName).append(": ").append(collectionTestingTag.get(tagName)).append("\n");
                boolean validation = validateTags.isACorrectValue(tagName, (String) collectionTestingTag.get(tagName));
                validateDataTesting.append("Validación : ").append(validation).append("\n");

                if(!validation){
                    if (testExpectedResult == ""){
                        testExpectedResult = "Resultado esperado: ERROR\nEl servicio debe indicar que los siguientes tags presentan error: " + tagName + " ";
                    }
                    else {
                        testExpectedResult += ",\n" + tagName;
                    }
                }
            }

            if (testExpectedResult == ""){

                testExpectedResult = "Resultado esperado: ÉXITO";

            }

            resultsTest.setDataTestingValidates(validateDataTesting.toString());
            String respuesta = "";

            try {

                respuesta = sendJSON.sendReqTestRest(tagsNames, collectionTestingTag, urlService);
                resultsTest.setResponseTest(respuesta);
                resultsTest.setSessionID(sendJSON.obtainValueOfJSONResponse("sessionID", respuesta));
                resultados.add(resultsTest);

            } catch (Exception e) {

                LOGGER.log(Level.SEVERE, "Error al enviar el request, no hay conexión con el servicio", e);

            }

            serviceDAOImp.registerDataResults(serviceName, urlService, request.toString(), validateDataTesting.toString(),
                    testExpectedResult, respuesta.replace("'", "''"), "");
        }
        return resultados;
    }

    private List<String> extractNameTags(Map<String, ServiceTagsParameters> dataParameters) {
        List<String> tagsNames = new ArrayList<>();
        for (int i = 0; i < dataParameters.size(); i++) {
            System.out.println("valores de nombres de tags" + dataParameters.get(i).getTagNameId());
        }
        return tagsNames;
    }

}
