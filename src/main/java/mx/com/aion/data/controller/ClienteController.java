package mx.com.aion.data.controller;

import mx.com.aion.data.models.dao.IClienteDao;
import mx.com.aion.data.models.entity.Cliente;
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

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    private String sql = "select * from clientes";
    private String sql2 = "select * from clientes where id = ?";
    private String sql3 = "delete from clientes where id = ?";
    private String sql4 = "insert into clientes(id, nombre, apellido, email, create_at) values (?,?,?,?,?)";
    private String sql5 = "update clientes set nombre = ?, apellido = ?, email = ?, create_at = ? where id = ?";

    @Autowired
    private IClienteDao clienteService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(Model model){
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Cliente.class)));
        return "listar";
    }

    @RequestMapping(value = "/index")
    public String index(Model model){
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Cliente.class)));
        return "index";
    }

    @RequestMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Cliente.class)));
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
        if(id > 0 ){
            cliente = jdbcTemplate.queryForObject(sql2, new Object[]{id}, new BeanPropertyRowMapper<>(Cliente.class));
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
            System.out.println(jdbcTemplate.update(sql3, new Object[]{id}));//El rowmapper aplica para selects
        }
        return "redirect:/listar";
    }

    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model model, SessionStatus status){
        if (result.hasErrors()){
            model.addAttribute("titulo", "Formulario de Cliente");
            return "form";
        }
        if (jdbcTemplate.queryForObject(sql2, new Object[]{cliente.getId()}, new BeanPropertyRowMapper<>(Cliente.class)).getId() == cliente.getId()) {
            jdbcTemplate.update(sql5, new Object[]{
                    cliente.getNombre(), cliente.getApellido() ,cliente.getEmail(), cliente.getCrateAt(), cliente.getId()
            });
        }else {
            jdbcTemplate.update(sql4, new Object[]{
                    cliente.getId(), cliente.getNombre(), cliente.getApellido() ,cliente.getEmail(), cliente.getCrateAt()
            });
        }
        status.setComplete();
        return "redirect:listar";
    }

}
