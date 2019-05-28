package mx.com.aion.data.models.dao;

import mx.com.aion.data.models.entity.ServiceTagsParameters;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceDAOImp implements IServiceDAO {


    private JdbcTemplate jdbcTemplate;

    public ServiceDAOImp(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<String, ServiceTagsParameters> obtainDataParameters(String serviceName) {
        Map<String, ServiceTagsParameters> tagsParametersMap = new HashMap<>();
        List<ServiceTagsParameters> listaServiceTagsParameters = jdbcTemplate.query(ObtainParametersProcessDAO.obtainAllInfoByServiceName(), new Object[]{serviceName},new BeanPropertyRowMapper<>(ServiceTagsParameters.class));

        for (ServiceTagsParameters listaServiceTagsParameter : listaServiceTagsParameters) {
            tagsParametersMap.put(listaServiceTagsParameter.getTagNameId(), listaServiceTagsParameter);
        }

        return tagsParametersMap;
    }

    /*@Override
    public List<String> obtainTagsNames(String serviceName) {
        return jdbcTemplate.queryForList(ObtainParametersProcessDAO.obtainTagsNamesByServiceName(serviceName), String.class);
    }*/

    public List<String> obtainTagsNames(String serviceName) {
        return jdbcTemplate.queryForList(ObtainParametersProcessDAO.obtainTagsNamesByServiceName(serviceName), String.class);
    }

    @Override
    public String obtainPathMatrixByServiceName(String serviceName) {
        return jdbcTemplate.queryForList(ObtainParametersProcessDAO.obtainPathMatrixByServiceName(serviceName), String.class).get(0);
    }

    @Override
    public String obtainUrlServiceByServiceName(String serviceName) {
        return jdbcTemplate.queryForList(ObtainParametersProcessDAO.obtainUrlServiceByServiceName(serviceName), String.class).get(0);
    }

    @Override
    public boolean registerDataResults(String serviceName, String urlService, String request, String testExpectedResult, String expectedResults,
                                  String response, String testingResults) {

        boolean success = false;
        jdbcTemplate.update(ResultsProcessDAO.registerResults(serviceName), urlService, request, testExpectedResult , expectedResults, response, testingResults);

        return success;
    }

}
