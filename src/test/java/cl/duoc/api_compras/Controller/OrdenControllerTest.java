package cl.duoc.api_compras.Controller;

import cl.duoc.api_compras.controller.OrdenController;
import cl.duoc.api_compras.model.Orden;
import cl.duoc.api_compras.service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenControllerTest {

    @Mock
    private OrdenService service;

    @InjectMocks
    private OrdenController controller;

    private Orden orden;

    @BeforeEach
    void setUp() {
        orden = new Orden(1L, "FAB001", 10, "PENDIENTE", "Proveedor SPA", LocalDate.now() );
    }

    @Test
    void OrdenCreado_exito() {
        when(service.ingresarOrden(orden)).thenReturn(orden);

        ResponseEntity<Orden> respuesta = controller.ingresar(orden);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(orden, respuesta.getBody());
        verify(service).ingresarOrden(orden);
    }

    @Test
    void ordenIngresado_bad() {
        when(service.ingresarOrden(orden)).thenReturn(null);

        ResponseEntity<Orden> respuesta = controller.ingresar(orden);

        assertEquals(HttpStatus.BAD_REQUEST, respuesta.getStatusCode());
    }

    @Test
    void ListarOrdenes_exito() {
        when(service.listaDeOrden()).thenReturn(List.of(orden));

        ResponseEntity<List<Orden>> respuesta = controller.listaOrden();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(service).listaDeOrden();
    }

    @Test
    void ListarOrden_exito() {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(orden));

        ResponseEntity<Orden> respuesta = controller.listarUnaOrden(1L);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(orden, respuesta.getBody());
    }

    @Test
    void ListarOrden_bad() {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        ResponseEntity<Orden> respuesta = controller.listarUnaOrden(99L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    @Test
    void Buscar_porID_OK() {
        when(service.buscarPorIdFabricante("FAB001")).thenReturn(List.of(orden));

        ResponseEntity<List<Orden>> respuesta = controller.buscarPorIdFabricante("FAB001");

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(1, respuesta.getBody().size());
        verify(service).buscarPorIdFabricante("FAB001");
    }

    @Test
    void Actualizacion_Ok() {
        when(service.actualizarOrden(1L, orden)).thenReturn(orden);

        ResponseEntity<?> respuesta = controller.actualizarOrden(1L, orden);

        assertEquals(HttpStatus.ACCEPTED, respuesta.getStatusCode());
        assertEquals("Orden actualizada con exito", respuesta.getBody());
    }

    @Test
    void Actualizacion_Bad() {
        when(service.actualizarOrden(99L, orden)).thenThrow(new RuntimeException("Orden no encontrada"));

        ResponseEntity<?> respuesta = controller.actualizarOrden(99L, orden);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
    }

    @Test
    void Delete_Ok() {
        doNothing().when(service).eliminarOrden(1L);

        ResponseEntity respuesta = controller.eliminarOrden(1L);

        assertEquals(HttpStatus.ACCEPTED, respuesta.getStatusCode());
        assertEquals("Orden con el ID 1 eliminada exitosamente", respuesta.getBody());
    }

    @Test
    void Delete_Bad() {
        doThrow(new RuntimeException("no encontrada")).when(service).eliminarOrden(99L);

        ResponseEntity respuesta = controller.eliminarOrden(99L);

        assertEquals(HttpStatus.NOT_FOUND, respuesta.getStatusCode());
        assertEquals("ID no encontado o ya se ha eliminado", respuesta.getBody());
    }
}
