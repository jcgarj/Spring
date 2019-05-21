package mx.com.aion.data.models.dao;

import mx.com.aion.data.models.entity.Cliente;
import org.springframework.data.repository.CrudRepository;

public interface IClienteDao extends CrudRepository<Cliente, Long> {


}