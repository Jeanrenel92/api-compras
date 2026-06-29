package cl.duoc.api_compras.controller;

import cl.duoc.api_compras.model.Orden;
import cl.duoc.api_compras.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/proveedores")
@Tag(name = "Ordenes", description = "API para la gestión de ordenes de compra")
public class OrdenController {

    @Autowired
    private OrdenService service;

    //ingresar una orden
    @PostMapping
    @Operation(summary = "Registrar una nueva orden", description = "Permite ingresar una nueva orden en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")})
    public ResponseEntity<Orden> ingresar(@Valid @RequestBody Orden orden){
        Orden nuevaOrden = service.ingresarOrden(orden);
        if (nuevaOrden!=null){
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaOrden);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    //listarOrden
    @GetMapping
    @Operation(summary = "Listar órdenes de compra", description = "Retorna todas las órdenes de compra registradas en el sistema, sin filtros")
    @ApiResponse(responseCode = "200", description = "Lista de órdenes obtenida correctamente")
    public ResponseEntity<List<Orden>> listaOrden() {
        return ResponseEntity.ok(service.listaDeOrden());
    }

    //busqueda de orden por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar orden por ID", description = "Obtiene una orden específica según su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orden encontrada"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
    public ResponseEntity <Orden> listarUnaOrden(@PathVariable Long id){
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/fabricante/{idFabricante}")
    @Operation(
            summary = "Buscar órdenes por fabricante",
            description = "Obtiene todas las órdenes asociadas a un fabricante")
    @ApiResponse(responseCode = "200", description = "Órdene encontrada")
    public ResponseEntity<List<Orden>> buscarPorIdFabricante(@Parameter(description = "ID del fabricante", example = "FAB001") @PathVariable String idFabricante) {
        List<Orden> ordenes = service.buscarPorIdFabricante(idFabricante);
        return ResponseEntity.ok(ordenes);
    }

    
    //actualizacion de orden por Id

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una orden", description = "Actualiza la información de una orden existente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Orden actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
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
    @Operation(summary = "Eliminar una orden", description = "Elimina una orden según su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Orden eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada")
    })
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
