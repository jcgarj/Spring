package mx.com.aion.data.models.dao;

import mx.com.aion.data.models.entity.ServiceTagsParameters;

import java.util.List;
import java.util.Map;

public interface IServiceDAO {

    boolean registerDataResults(String serviceName, String urlService, String request, String dataValidation,
                                String expectedResults, String response, String testingResults);
    Map<String, ServiceTagsParameters> obtainDataParameters(String serviceName);
    List<String> obtainTagsNames(String serviceName);
    String obtainPathMatrixByServiceName(String serviceName);
    String obtainUrlServiceByServiceName(String serviceName);

}
