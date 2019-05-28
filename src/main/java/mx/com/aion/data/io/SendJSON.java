package mx.com.aion.data.io;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class SendJSON {

    public String sendReqTestRest(List<String> tagsNames, Map values, String url){
        String respuesta = "";
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        JSONObject json = new JSONObject();

        for (String tagsName : tagsNames) {
            json.put(tagsName, values.get(tagsName));
        }
        request.body(json.toJSONString());

        try{
            Response response = request.post(url);
            ResponseBody body = response.getBody();
            respuesta = body.asString().replace(",", ",\n");
        }catch (Exception e){
            LOGGER.log(Level.SEVERE, "Error al enviar el request ", e);
        }
        return respuesta;
    }

    public String obtainValueOfJSONResponse(String tagName, String response){
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(response);
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "Error al obtener los valores del response ", e);
        }
        assert jsonObject != null;
        return (String) jsonObject.get(tagName);
    }
}
