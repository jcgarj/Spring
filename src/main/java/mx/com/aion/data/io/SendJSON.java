package mx.com.aion.data.io;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.service.ServiceType;
import mx.com.aion.data.service.ValidationRuleService;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class SendJSON {

    RequestSpecification request = RestAssured.given();

    public String setResponse(@NotNull List<String> tagsNames, Map values, JdbcTemplate jdbcTemplate, IDsbCfgAdcQueryDao dsbCfgAdcQueryDao, String vcRuleGroup, String serviceName, boolean isToken, JdbcTemplate jdbcTemplateEasyTransfer) {
        if (isToken) {
            request.headers("Content-Type", "application/json");
        } else {
            GenerateToken generateToken = new GenerateToken();
            if (tagsNames.contains("sessionId")){
                values.replace("sessionId", new ObtainSession(jdbcTemplateEasyTransfer, dsbCfgAdcQueryDao).getSessionId());
            }
            request.headers("Content-Type", "application/json", "token", generateToken.obtainToken());
        }
        JSONObject json = new JSONObject();

        for (String tagsName : tagsNames) {
            try {
                json.put(tagsName, values.get(tagsName));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        ServiceType serviceType = new ServiceType();


        ValidationRuleService validationRuleService = new ValidationRuleService(jdbcTemplate, dsbCfgAdcQueryDao);
        validationRuleService.generateValidation(vcRuleGroup, 1, serviceType.obtainPojoByServiceName(serviceName, json), "V");
        if (validationRuleService.getErrors().size() > 0) {//hubo errores
            for (int i = 0; i < validationRuleService.getErrors().size(); i++) {
                System.out.println("Hubo errores " + validationRuleService.getErrors().get(i));
            }

        } else {
            System.out.println("Todo chido");
        }

        request.body(json.toString());
        System.out.println(json.toString());
        return json.toString();

    }

    public String getResponse(String url){
        String respuesta = "";
        try {
            Response response = request.post(url);
            ResponseBody body = response.getBody();
            respuesta = body.asString().replace(",", ",\n");
        } catch (Exception e) {
            //          LOGGER.log(Level.SEVERE, "Error al enviar el request ", e);
        }
        return respuesta;
    }

    /*public String obtainValueOfJSONResponse(String tagName, String response){
        JsonParser parser= new JsonParser();
        JsonElement element = parser.parse(response);
        return element.getAsJsonObject().get(tagName).getAsString();
    }*/
}