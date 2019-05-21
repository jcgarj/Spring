package mx.com.aion.data.controller;
import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.models.entity.Cliente;
import mx.com.aion.data.models.entity.DsbCfgAdocQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class JdbcController {

    String sql = "SELECT * FROM DSB_CFG_ADOC_QUERY WHERE VC_QUERY_ID = ?";

    String paramId = "GRUPO_RSVALIDAUSUARIO";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IDsbCfgAdcQueryDao dsbCfgAdocQuery;

    @GetMapping("/clientes")
    public List<Cliente> getClientes(){
        return jdbcTemplate.query("select * from clientes", new BeanPropertyRowMapper<>(Cliente.class));
    }

    @GetMapping("/reglas")
    public List<DsbCfgAdocQuery> getQuery(){

        Optional<DsbCfgAdocQuery> dsbCfgAdocQuery1 = null;
        dsbCfgAdocQuery.save(new DsbCfgAdocQuery("query1",1,"Ejemplo1","1","algo","01-01-1999","jesus","Producto"));
        dsbCfgAdocQuery.save(new DsbCfgAdocQuery("query2",1,"Ejemplo1","1","algo","01-01-1999","jesus","Producto"));
        String id = "GRUPO_RSVALIDAUSUARIO";
        dsbCfgAdocQuery1 = dsbCfgAdocQuery.findById(id);
        dsbCfgAdocQuery1.get().getVcModuleId();
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
    }

    @GetMapping("/borrarreglas")
    public List<DsbCfgAdocQuery> deleteQuery(Model model){
        dsbCfgAdocQuery.deleteById("query1");
        dsbCfgAdocQuery.deleteById("query2");
        System.out.println();
        System.out.println("Se elimino correctamente");
        System.out.println("----------------------------------------------------------------------------------------------------------------------------------");
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
    }

    @GetMapping("/revisar")
    public List<DsbCfgAdocQuery> find(Model model){
        DsbCfgAdocQuery dsbCfgClobQuery = jdbcTemplate.queryForObject(sql, new Object[]{paramId}, new BeanPropertyRowMapper<>(DsbCfgAdocQuery.class));
        return (List<DsbCfgAdocQuery>) dsbCfgClobQuery;
    }

}
