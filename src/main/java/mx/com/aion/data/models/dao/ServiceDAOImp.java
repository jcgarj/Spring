package mx.com.aion.data.models.dao;

import mx.com.aion.data.models.entity.ServiceTagsParameters;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mx.com.aion.data.enums.CfgQueries.*;

public class ServiceDAOImp implements IServiceDAO {


    private JdbcTemplate jdbcTemplate;
    private IDsbCfgAdcQueryDao dsbCfgAdcQueryDao;

    public ServiceDAOImp(JdbcTemplate jdbcTemplate, IDsbCfgAdcQueryDao dsbCfgAdcQueryDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.dsbCfgAdcQueryDao = dsbCfgAdcQueryDao;
    }

    @Override
    public Map<String, ServiceTagsParameters> obtainDataParameters(String serviceName) {
        Map<String, ServiceTagsParameters> tagsParametersMap = new HashMap<>();
        List<ServiceTagsParameters> listaServiceTagsParameters = jdbcTemplate.query(dsbCfgAdcQueryDao.findById(GET_ALL_INFO_PARAMETERS.getValue()).get().getVcQueryStatement(), new Object[]{serviceName},new BeanPropertyRowMapper<>(ServiceTagsParameters.class));

        for (ServiceTagsParameters listaServiceTagsParameter : listaServiceTagsParameters) {
            tagsParametersMap.put(listaServiceTagsParameter.getTagNameId(), listaServiceTagsParameter);
        }

        return tagsParametersMap;
    }

    public List<String> obtainTagsNames(String serviceName) {
        return jdbcTemplate.queryForList(dsbCfgAdcQueryDao.findById(GET_TAGS_NAMES.getValue()).get().getVcQueryStatement(), new Object[]{serviceName}, String.class);
    }

    @Override
    public String obtainPathMatrixByServiceName(String serviceName) {
        return jdbcTemplate.queryForList(dsbCfgAdcQueryDao.findById(GET_PATH_MATRIX.getValue()).get().getVcQueryStatement(), new Object[]{serviceName}, String.class).get(0);
    }

    @Override
    public String obtainUrlServiceByServiceName(String serviceName) {
        return jdbcTemplate.queryForList(dsbCfgAdcQueryDao.findById(GET_URL_SERVICE.getValue()).get().getVcQueryStatement(), new Object[]{serviceName}, String.class).get(0);
    }

    @Override
    public String obtainVcRuleGroupByServiceName(String serviceName) {
        return jdbcTemplate.queryForList(dsbCfgAdcQueryDao.findById(GET_RULE_GROUP.getValue()).get().getVcQueryStatement(), new Object[]{serviceName}, String.class).get(0);
    }

    @Override
    public boolean registerDataResults(String serviceName, String urlService, String request, String testExpectedResult, String expectedResults,
                                  String response, String testingResults) {

        int idService = jdbcTemplate.queryForList(dsbCfgAdcQueryDao.findById(GET_SERVICE_ID.getValue()).get().getVcQueryStatement(), new Object[]{serviceName}, Integer.class).get(0);
        jdbcTemplate.update(dsbCfgAdcQueryDao.findById(SET_RESULTS_TESTING.getValue()).get().getVcQueryStatement(),idService, urlService, request, testExpectedResult , expectedResults, response, testingResults);
        return true;
    }

}
