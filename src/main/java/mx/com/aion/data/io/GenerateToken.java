package mx.com.aion.data.io;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;

//import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

public class GenerateToken {

    private final String url = "https://amlc68wiuc.execute-api.us-east-2.amazonaws.com/easytransfer/api/etToken\n";

    public String obtainToken() {
        String respuesta = "";
        RequestSpecification request = RestAssured.given();
        request.headers("Content-Type", "application/json");

        JSONObject json = new JSONObject();
        try {
            json.put("username", "lmarquez");
            json.put("application", "ETM");
        }catch (JSONException e){
            e.printStackTrace();
        }

        request.body(json.toString());
        System.out.println(json.toString());

        try {
            Response response = request.post(url);
            ResponseBody body = response.getBody();
            respuesta = body.asString().replace(",", ",\n");
        } catch (Exception e) {
            //LOGGER.log(Level.SEVERE, "Error al enviar el request ", e);
        }
        return obtainValueOfJSONResponse("token", respuesta);
    }

    public String obtainValueOfJSONResponse(String tagName, String response) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(response);
        return element.getAsJsonObject().get(tagName).getAsString();
    }

}
