package mx.com.aion.data.models.service;

import mx.com.aion.data.models.entity.Cliente;
import java.util.List;

public interface IClineteService {

    public List findAll();

    public void save(Cliente cliente);

    public Cliente findOne(Long id);

    public void delete(Long id);
}
