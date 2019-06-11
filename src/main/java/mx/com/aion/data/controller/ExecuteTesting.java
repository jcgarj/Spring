package mx.com.aion.data.controller;

import mx.com.aion.data.datasources.JDBCTemplateEasyTransfer;
import mx.com.aion.data.datasources.JDBCTemplateWarMachine;
import mx.com.aion.data.io.ReadDataTestingFile;
import mx.com.aion.data.io.SendJSON;
import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.models.dao.ServiceDAOImp;
import mx.com.aion.data.models.entity.ResultsTest;
import mx.com.aion.data.models.entity.ServiceTagsParameters;
import mx.com.aion.data.models.entity.WmResultsTesting;
import mx.com.aion.data.util.ValidateTags;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static mx.com.aion.data.enums.CfgQueries.GET_ALL_RESULTS;

@Controller
@SessionAttributes("testing")
public class ExecuteTesting {

    @Autowired
    @Qualifier("warmachine")
    private JDBCTemplateWarMachine jdbcTemplateWarMachine;
    @Autowired
    @Qualifier("easytransfer")
    private JDBCTemplateEasyTransfer jdbcTemplateEasyTransfer;
    @Autowired
    private IDsbCfgAdcQueryDao dsbCfgAdocQuery;
    private static final Logger LOGGER = Logger.getLogger(ExecuteTesting.class.getName());
    private boolean isToken = true;

    @GetMapping("/test/{serviceName}")
    public String executeTesting(@PathVariable(value = "serviceName") String serviceName) {
        ArrayList<ResultsTest> resultados = new ArrayList<>();
        ServiceDAOImp serviceDAOImp = new ServiceDAOImp(jdbcTemplateWarMachine.getDS(), dsbCfgAdocQuery);
        String path = serviceDAOImp.obtainPathMatrixByServiceName(serviceName);
        String urlService = serviceDAOImp.obtainUrlServiceByServiceName(serviceName);
        String vcRuleGroup = serviceDAOImp.obtainVcRuleGroupByServiceName(serviceName);
        Map<String, ServiceTagsParameters> dataParameters = serviceDAOImp.obtainDataParameters(serviceName);
        List<String> tagsNames = serviceDAOImp.obtainTagsNames(serviceName);
        ArrayList<Map> collectionTestingTags = new ArrayList<>();
        SendJSON sendJSON = new SendJSON();
        boolean expectedResult = true;

        ReadDataTestingFile readDataTestingFile = new ReadDataTestingFile(tagsNames, serviceName, path);
        try {
            collectionTestingTags = readDataTestingFile.readExcel();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error al leer el archivo", e);

        } catch (InvalidFormatException e) {
            LOGGER.log(Level.SEVERE, "Formato incorrecto", e);
        }
        if (!vcRuleGroup.equals("GRUPO_ET_TOKEN")) {
            isToken = false;
        }

        for (Map collectionTestingTag : collectionTestingTags) {
            String testExpectedResult = "";
            ResultsTest resultsTest = new ResultsTest();
            StringBuilder validateDataTesting = new StringBuilder();
            ValidateTags validateTags = new ValidateTags(dataParameters, collectionTestingTag);

            for (String tagName : tagsNames) {
                validateDataTesting.append(tagName).append(": ").append(collectionTestingTag.get(tagName)).append("\n");
                boolean validation = validateTags.isACorrectValue(tagName, (String) collectionTestingTag.get(tagName));
                validateDataTesting.append("Validación : ").append(validation).append("\n");

                if (!validation) {
                    expectedResult = false;
                    if (testExpectedResult == "") {
                        testExpectedResult = "Resultado esperado: ERROR\nEl servicio debe indicar que los siguientes tags presentan error: " + tagName + " ";
                    } else {
                        testExpectedResult += ",\n" + tagName;
                    }
                }
            }

            if (testExpectedResult == "") {
                testExpectedResult = "Resultado esperado: ÉXITO";
            }

            resultsTest.setDataTestingValidates(validateDataTesting.toString());
            String respuesta = "";
            String request = "";
            try {
                request = sendJSON.setResponse(tagsNames, collectionTestingTag, jdbcTemplateWarMachine.getDS(), dsbCfgAdocQuery, vcRuleGroup, serviceName, isToken, jdbcTemplateEasyTransfer.getDS());
                respuesta = sendJSON.getResponse(urlService);
                resultsTest.setResponseTest(respuesta);
                resultados.add(resultsTest);

            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error al enviar el request, no hay conexión con el servicio", e);
            }
            respuesta = respuesta.replace("'", "''");
            String testingResults;
            if (expectedResult){
                if (respuesta.contains("\"result\":0")){
                    testingResults = "Exito";
                }
                else {
                    testingResults = "Error: Se esperaba exito en la prueba";
                }
            }
            else {
                if (respuesta.contains("\"result\":0")){
                    testingResults = "Error: Se esperaba error en la prueba";
                }
                else {
                    testingResults = "Exito";
                }
            }
            serviceDAOImp.registerDataResults(serviceName, urlService, request, validateDataTesting.toString(), testExpectedResult, respuesta, testingResults);
        }
        return "redirect:/resultTesting";
    }

    @GetMapping(value = "/resultTesting")
    public String resultTesting(Model model) {
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("tests", jdbcTemplateWarMachine.getDS().query(dsbCfgAdocQuery.findById(GET_ALL_RESULTS.getValue()).get().getVcQueryStatement(), new BeanPropertyRowMapper<>(WmResultsTesting.class)));
        return "resultTesting";
    }
}
