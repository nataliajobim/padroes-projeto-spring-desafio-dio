package nataliajobim.padroesprojetospring.service.impl;

import nataliajobim.padroesprojetospring.model.Cliente;
import nataliajobim.padroesprojetospring.model.ClienteRepository;
import nataliajobim.padroesprojetospring.model.Endereco;
import nataliajobim.padroesprojetospring.model.EnderecoRepository;
import nataliajobim.padroesprojetospring.service.ClienteService;
import nataliajobim.padroesprojetospring.service.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos(){
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id){
       Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente){
        salvarClienteComCep(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente){
        Optional<Cliente> cliente1 = clienteRepository.findById(id);
        if (cliente1.isPresent()){
            salvarClienteComCep(cliente);
        }
    }

    public void deletar(Long id){
        clienteRepository.deleteById(id);
    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        Endereco endereco = enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }
}
