package mx.com.aion.data.models.dao;

public class ResultsProcessDAO {

    public static String obtainServiceId(String serviceName){
        return "SELECT SERVICE_ID FROM WM_SERVICE WHERE SERVICE_NAME = '" + serviceName + "'";
    }

    /*public static String registerResults(String serviceName, String urlService, String request, String dataValidate, String expectedResults,
                                         String response, String testingResults){
        return "INSERT INTO WM_RESULTS_TESTING\n" +
                "(SERVICE_ID, SERVICE_URL, REQUEST, DATA_VALIDATE ,EXPECTED_RESULT, RESPONSE, TESTING_RESULTS)\n" +
                "VALUES\n" +
                "((" + obtainServiceId(serviceName) + "),\n" +
                "'" + urlService + "',\n" +
                "'" + request + "',\n" +
                "'" + dataValidate + "',\n" +
                "'" + expectedResults + "',\n" +
                "'" + response + "',\n" +
                "'" + testingResults + "')";
    }*/

    public static String registerResults(String serviceName){
        return "INSERT INTO WM_RESULTS_TESTING\n" +
                "(SERVICE_ID, SERVICE_URL, REQUEST, DATA_VALIDATE ,EXPECTED_RESULT, RESPONSE, TESTING_RESULTS)\n" +
                "VALUES\n" +
                "((" + obtainServiceId(serviceName) + "),\n" +
                "?,?,?,?,?,?)";
    }

}
