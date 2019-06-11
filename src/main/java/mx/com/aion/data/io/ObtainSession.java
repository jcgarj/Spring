package mx.com.aion.data.io;

import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import org.springframework.jdbc.core.JdbcTemplate;

import static mx.com.aion.data.enums.CfgQueries.GET_SESSIONID;

public class ObtainSession {

    private JdbcTemplate jdbcTemplate;
    private IDsbCfgAdcQueryDao dsbCfgAdcQueryDao;

    public ObtainSession(JdbcTemplate jdbcTemplate, IDsbCfgAdcQueryDao dsbCfgAdcQueryDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.dsbCfgAdcQueryDao = dsbCfgAdcQueryDao;
    }

    public String getSessionId(){
        return jdbcTemplate.queryForList(dsbCfgAdcQueryDao.findById(GET_SESSIONID.getValue()).get().getVcQueryStatement(), String.class).get(0);
    }
}
