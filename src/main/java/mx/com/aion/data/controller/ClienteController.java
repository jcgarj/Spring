package mx.com.aion.data.controller;

import mx.com.aion.data.models.dao.IDsbCfgAdcQueryDao;
import mx.com.aion.data.models.entity.Cliente;
import mx.com.aion.data.models.entity.DataSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Map;

import static mx.com.aion.data.enums.CfgQueries.*;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private IDsbCfgAdcQueryDao iDsbCfgAdcQueryDao;

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(Model model){
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", jdbcTemplate.query(iDsbCfgAdcQueryDao.findById(GET_ALL_CLIENTS.getValue()).get().getVcQueryStatement(), new BeanPropertyRowMapper<>(Cliente.class)));
        return "listar";
    }

    @RequestMapping(value = "/index")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String,Object> model){

        Cliente cliente = new Cliente();
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de Cliente");
        return "form";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable(value = "id") Long id, Map<String,Object> model){
        Cliente cliente;
        if(id > 0){
            cliente = jdbcTemplate.queryForObject(iDsbCfgAdcQueryDao.findById(GET_CLIENT_BY_ID.getValue()).get().getVcQueryStatement(), new Object[]{id}, new BeanPropertyRowMapper<>(Cliente.class));
        }
        else{
            return "redirect:/listar";
        }
        model.put("cliente", cliente);
        model.put("titulo", "Editar Cliente");
        return "form";
    }

    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable(value = "id") Long id){
        if(id > 0 ){

            System.out.println(jdbcTemplate.update(iDsbCfgAdcQueryDao.findById(DELETE_CLIENT_BY_ID.getValue()).get().getVcQueryStatement(), id));//El rowmapper aplica para selects
        }
        return "redirect:/listar";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status){
        if (result.hasErrors()){
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }
        if (cliente.getId() == null ){

            jdbcTemplate.update(iDsbCfgAdcQueryDao.findById(SET_CLIENT.getValue()).get().getVcQueryStatement(), cliente.getId(), cliente.getNombre(), cliente.getApellido(),cliente.getEmail(), cliente.getCrateAt());
        }else {
            jdbcTemplate.update(iDsbCfgAdcQueryDao.findById(UPDATE_CLIENT.getValue()).get().getVcQueryStatement(), cliente.getNombre(), cliente.getApellido(),cliente.getEmail(), cliente.getCrateAt(), cliente.getId());
        }
        status.setComplete();
        return "redirect:listar";
    }

    @RequestMapping(value = "/ambientes", method = RequestMethod.GET)
    public String listarAmbientes(Model model){
        model.addAttribute("titulo", "Listado de ambientes");
        model.addAttribute("ambientes", jdbcTemplate.query(iDsbCfgAdcQueryDao.findById(GET_ALL_ENVIROREMENTS.getValue()).get().getVcQueryStatement(), new BeanPropertyRowMapper<>(DataSheet.class)));
        return "ambientes";
    }

}
