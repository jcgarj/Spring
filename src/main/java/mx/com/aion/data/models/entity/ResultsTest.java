package mx.com.aion.data.models.entity;

public class ResultsTest {

    private String validateDataTesting;
    private String respuesta;
    private String sessionID;

    public ResultsTest(){}

    public void setDataTestingValidates(String validateDataTesting) {
        this.validateDataTesting = validateDataTesting;
    }

    public String getValidateDataTesting() {
        return validateDataTesting;
    }

    public void setResponseTest(String respuesta) {
        this.respuesta = respuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }
}
