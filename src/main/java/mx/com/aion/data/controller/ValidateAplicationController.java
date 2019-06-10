package mx.com.aion.data.controller;

import mx.com.aion.data.datasources.JDBCTemplateEasyTransfer;
import mx.com.aion.data.datasources.JDBCTemplateWarMachine;
import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.models.entity.Cliente;
import mx.com.aion.data.models.entity.entity.DsbCfgAdocQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ValidateAplicationController {

    String sql = "SELECT VC_QUERY_ID, N_QUERY_ACTIVE, VC_QUERY_DESCRIPTION, VC_QUERY_VERSION, VC_QUERY_STATEMENT, VC_QUERY_DATE, VC_QUERY_AUTHOR, VC_MODULE_ID "
            + "FROM DSB_CFG_ADOC_QUERY "
            + "WHERE VC_QUERY_ID=? ";

    String paramId = "GRUPO_ET_TOKEN";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("warmachine")
    private JDBCTemplateWarMachine jdbcTemplateWarMachine;

    @Autowired
    @Qualifier("easytransfer")
    private JDBCTemplateEasyTransfer jdbcTemplateEasyTransfer;

    @Autowired
    private IDsbCfgAdcQueryDao dsbCfgAdocQuery;

    @GetMapping(value = "/clientes")
    public List<Cliente> getClientes() {
        return jdbcTemplate.query("select * from clientes", new BeanPropertyRowMapper<>(Cliente.class));
    }

    @GetMapping("/listaReglas")
    public List<DsbCfgAdocQuery> getReglas() {
        return jdbcTemplate.query("select * from DSB_CFG_ADOC_QUERY", new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
    }

    @GetMapping("/reglas")
    public List<DsbCfgAdocQuery> getQuery() {
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
    }

    @GetMapping("/borrarreglas")
    public List<DsbCfgAdocQuery> deleteQuery(Model model) {
        dsbCfgAdocQuery.deleteById("query1");
        dsbCfgAdocQuery.deleteById("query2");
        System.out.println();
        System.out.println("Se elimino correctamente");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
    }

    @GetMapping("/revisar")
    public DsbCfgAdocQuery find(Model model) {
        DsbCfgAdocQuery dsbCfgAdocQuery = jdbcTemplate.queryForObject(sql, new Object[]{paramId}, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
        return dsbCfgAdocQuery;
    }

}
