package cl.duoc.api_compras.service;

import cl.duoc.api_compras.model.Orden;
import cl.duoc.api_compras.repository.OrdenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class OrdenService {
    private final OrdenRepository repository;

    //listar todos los ordenes
    public List<Orden> listaDeOrden(){
        return repository.findAll();
    }


    //listar un orden en particular
    public Optional<Orden> buscarPorId(Long id){
        return repository.findById(id);
    }


    //registrar orden
    public Orden ingresarOrden(Orden orden){
        return repository.save(orden);
    }

    //actualizar orden
    public Orden actualizarOrden(Long id, Orden orden) {
        return repository.findById(id)
                .map(existente -> {
                    orden.setId(id);
                    return repository.save(orden);
                })
                .orElseThrow(() -> new RuntimeException("Orden con id=" + id + " no encontrada"));
    }

    //eliminar orden
    public void eliminarOrden(Long id){
        if (repository.existsById(id)){
            repository.deleteById(id);
        }else {
            throw new RuntimeException("Orden con id=" + id + " no encontrada");
        }
    }


}
