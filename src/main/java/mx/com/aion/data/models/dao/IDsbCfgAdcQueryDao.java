package mx.com.aion.data.models.dao;

import mx.com.aion.data.models.entity.entity.DsbCfgAdocQuery;
import org.springframework.data.repository.CrudRepository;

//Realiza la busqueda de querys en la tabla de DSB_CFG_ADOC_QUERY
public interface IDsbCfgAdcQueryDao extends CrudRepository<DsbCfgAdocQuery, String> {
}
