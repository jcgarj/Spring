package mx.com.aion.data.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.models.entity.EtToken;
import mx.com.aion.data.service.ValidationRuleService;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class SendJSON {

    public String sendReqTestRest(List<String> tagsNames, Map values, String url, JdbcTemplate jdbcTemplate, IDsbCfgAdcQueryDao dsbCfgAdcQueryDao) throws IOException {
        String respuesta = "";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject json = new JSONObject();
        ObjectMapper mapper = new ObjectMapper();

        for (String tagsName : tagsNames) {
            json.put(tagsName, values.get(tagsName));
        }

        EtToken etToken = new Gson().fromJson(json.toString(), EtToken.class);
        ValidationRuleService validationRuleService= new ValidationRuleService(jdbcTemplate, dsbCfgAdcQueryDao);
        validationRuleService.generateValidation("GRUPO_ET_TOKEN", 1, etToken, "V");
        if(validationRuleService.getErrors().size() > 0){//hubo errores
            for (int i = 0; i < validationRuleService.getErrors().size(); i++) {
                System.out.println("Hubo errores " + validationRuleService.getErrors().get(i));
            }

        }
        else {
            System.out.println("Todo chido");
        }

        request.body(json.toString());
        System.out.println(json.toString());

        try{
            Response response = request.post(url);
            ResponseBody body = response.getBody();
            respuesta = body.asString().replace(",", ",\n");
        }catch (Exception e){
            //LOGGER.log(Level.SEVERE, "Error al enviar el request ", e);
        }
        return respuesta;
    }

    public String obtainValueOfJSONResponse(String tagName, String response){
        JsonParser parser= new JsonParser();
        JsonElement element = parser.parse(response);
        return element.getAsJsonObject().get(tagName).getAsString();
    }
}