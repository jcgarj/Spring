package mx.com.aion.data.service;

import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.models.entity.DsbCfgAdocQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DsbCfgAdocQueryService {

    @Autowired
    private IDsbCfgAdcQueryDao dsbCfgAdocQueryDao;

    public Optional<DsbCfgAdocQuery> getDsbAdocQueryById(String paramId) {
        return dsbCfgAdocQueryDao.findById(paramId);
    }
}
