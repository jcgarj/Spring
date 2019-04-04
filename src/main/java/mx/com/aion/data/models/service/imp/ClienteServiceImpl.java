package mx.com.aion.data.models.service.imp;

import mx.com.aion.data.models.dao.IClienteDao;
import mx.com.aion.data.models.entity.Cliente;
import mx.com.aion.data.models.service.IClineteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteServiceImpl implements IClineteService {

    @Autowired
    private IClienteDao clienteDao;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> findAll(){
        return (List<Cliente>)clienteDao.findAll();
    }
    @Override
    @Transactional
    public void save(Cliente cliente){
        clienteDao.save(cliente);
    }
    @Override
    @Transactional(readOnly = true)
    public Cliente findOne(Long id){
        return clienteDao.findById(id).orElse(null);
    }
    @Override
    @Transactional
    public void delete(Long id){
        clienteDao.deleteById(id);
    }
}
