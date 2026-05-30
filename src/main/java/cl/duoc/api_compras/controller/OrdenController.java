package cl.duoc.api_compras.controller;

import cl.duoc.api_compras.model.Orden;
import cl.duoc.api_compras.service.OrdenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/proveedores")
public class OrdenController {

    @Autowired
    private OrdenService service;

    //ingresar una orden
    @PostMapping
    public ResponseEntity<Orden> ingresar(@Valid @RequestBody Orden orden){
        Orden nuevaOrden = service.ingresarOrden(orden);
        if (nuevaOrden!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden);
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
    }

    //listarOrden
    @GetMapping
    public ResponseEntity<List<Orden>> listaOrden() {
        return ResponseEntity.ok(service.listaDeOrden());
    }

    //busqueda de orden por ID
    @GetMapping("/{id}")
    public ResponseEntity <Orden> listarUnaOrden(@PathVariable Long id){
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @GetMapping("/fabricante/{idFabricante}")
    public ResponseEntity<List<Orden>> buscarPorIdFabricante(@PathVariable String idFabricante) {
        List<Orden> ordenes = service.buscarPorIdFabricante(idFabricante);
        return ResponseEntity.ok(ordenes);
    }



    //actualizacion de orden por Id
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarOrden(@Valid @PathVariable Long id, @RequestBody Orden orden){
        try{
            Orden ordenActualizar = service.actualizarOrden(id, orden);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Orden actualizada con exito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //eliminar orden por Id
    @DeleteMapping("/{id}")
    public ResponseEntity eliminarOrden(@PathVariable Long id){
        try {
            service.eliminarOrden(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body("Orden con el ID "+id+ " eliminada exitosamente");
        }
        catch(RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID no encontado o ya se ha eliminado");
        }
    }

}
