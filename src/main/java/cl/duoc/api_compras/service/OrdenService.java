package cl.duoc.api_compras.service;

import cl.duoc.api_compras.model.Orden;
import cl.duoc.api_compras.repository.OrdenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class OrdenService {
    private final OrdenRepository repository;

    //listar todos los ordenes
    public List<Orden> listaDeOrden(){
        log.debug("[Service] Consultando todas las órdenes");
        List<Orden> ordenes = repository.findAll();
        log.debug("[Service] {} órdenes obtenidas", ordenes.size());
        return ordenes;
    }


    //listar un orden en particular
    public Optional<Orden> buscarPorId(Long id){
        log.debug("[Service] Buscando orden id={}", id);
        Optional<Orden> orden = repository.findById(id);
        if (orden.isEmpty()) {
            log.warn("[Service] Orden id={} no encontrada", id);
        }
        return orden;
    }



    //Metodo para el micro servicio inventario
    public List<Orden> buscarPorIdFabricante(String idFabricante) {
        log.info("[Service] Consulta externa (api-inventarios) - buscando órdenes por idFabricante={}", idFabricante);
        List<Orden> ordenes = repository.findByIdFabricante(idFabricante);
        log.debug("[Service] {} órdenes encontradas para idFabricante={}", ordenes.size(), idFabricante);
        return ordenes;
    }

    //registrar orden
    public Orden ingresarOrden(Orden orden){
        log.info("[Service] Registrando nueva orden - idFabricante={}, unidad={}, estado={}, proveedor={}",
                orden.getIdFabricante(), orden.getUnidad(), orden.getEstado(), orden.getNomProveedor());
        Orden guardada = repository.save(orden);
        log.info("[Service] Orden registrada id={}", guardada.getId());
        return guardada;
    }

    //actualizar orden de compra
    public Orden actualizarOrden(Long id, Orden orden) {
        log.info("[Service] Actualizando orden id={}", id);
        return repository.findById(id)
                .map(existente -> {
                    orden.setId(id);
                    Orden actualizada = repository.save(orden);
                    log.info("[Service] Orden id={} actualizada - nuevo estado={}", id, actualizada.getEstado());
                    return actualizada;
                })
                .orElseThrow(() -> {
                    log.error("[Service] Actualización fallida - orden id={} no encontrada", id);
                    return new RuntimeException("Orden con id=" + id + " no encontrada");
                });
    }

    //eliminar orden de compra
    public void eliminarOrden(Long id){
        log.info("[Service] Eliminando orden id={}", id);
        if (repository.existsById(id)){
            repository.deleteById(id);
            log.info("[Service] Orden id={} eliminada", id);
        }else {
            log.error("[Service] Eliminación fallida - orden id={} no encontrada", id);
            throw new RuntimeException("Orden con id=" + id + " no encontrada");
        }
    }


}