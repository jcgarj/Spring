package mx.com.aion.data.controller;

import mx.com.aion.data.datasources.JDBCTemplateEasyTransfer;
import mx.com.aion.data.datasources.JDBCTemplateWarMachine;
import mx.com.aion.data.io.ReadDataTestingFile;
import mx.com.aion.data.io.SendJSON;
import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.models.dao.ServiceDAOImp;
import mx.com.aion.data.models.entity.ResultsTest;
import mx.com.aion.data.models.entity.ServiceTagsParameters;
import mx.com.aion.data.util.ValidateTags;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
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
    public ArrayList<ResultsTest> executeTesting(@PathVariable(value = "serviceName") String serviceName) {
        ArrayList<ResultsTest> resultados = new ArrayList<>();
        ServiceDAOImp serviceDAOImp = new ServiceDAOImp(jdbcTemplateWarMachine.getDS(), dsbCfgAdocQuery);
        String path = serviceDAOImp.obtainPathMatrixByServiceName(serviceName);
        String urlService = serviceDAOImp.obtainUrlServiceByServiceName(serviceName);
        String vcRuleGroup = serviceDAOImp.obtainVcRuleGroupByServiceName(serviceName);
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
        if (!vcRuleGroup.equals("GRUPO_ET_TOKEN")) {
            isToken = false;
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

                if (!validation) {
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

            try {
                respuesta = sendJSON.sendReqTestRest(tagsNames, collectionTestingTag, urlService, jdbcTemplateWarMachine.getDS(), dsbCfgAdocQuery, vcRuleGroup, serviceName, isToken);
                resultsTest.setResponseTest(respuesta);
                //resultsTest.setToken(sendJSON.obtainValueOfJSONResponse("token", respuesta));
                resultados.add(resultsTest);

            } catch (Exception e) {

                LOGGER.log(Level.SEVERE, "Error al enviar el request, no hay conexión con el servicio", e);

            }

            serviceDAOImp.registerDataResults(serviceName, urlService, request.toString(), validateDataTesting.toString(),
                    testExpectedResult, respuesta.replace("'", "''"), "");
        }
        return resultados;
    }
}
