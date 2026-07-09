package cl.duoc.api_compras.Service;

import cl.duoc.api_compras.model.Orden;
import cl.duoc.api_compras.repository.OrdenRepository;
import cl.duoc.api_compras.service.OrdenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenServiceTest {

    @Mock
    private OrdenRepository repository;

    @InjectMocks
    private OrdenService service;

    private Orden orden;

    @BeforeEach
    void setUp() {
        orden = new Orden(1L, "FAB001", 10, "PENDIENTE", "Proveedor SPA", new Date());
    }

    @Test
    void ListarOrden_ok() {
        when(repository.findAll()).thenReturn(List.of(orden));

        List<Orden> resultado = service.listaDeOrden();

        assertEquals(1, resultado.size());
        assertEquals(orden, resultado.get(0));
        verify(repository, times(1)).findAll();
    }

    @Test
    void LIstarPorId_Ok() {
        when(repository.findById(1L)).thenReturn(Optional.of(orden));

        Optional<Orden> resultado = service.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(orden.getIdFabricante(), resultado.get().getIdFabricante());
        verify(repository).findById(1L);
    }

    @Test
    void Buscar_PorId_Bad() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Orden> resultado = service.buscarPorId(99L);

        assertTrue(resultado.isEmpty());
        verify(repository).findById(99L);
    }

    @Test
    void BuscarPorIdFabricante_ok() {
        when(repository.findByIdFabricante("FAB001")).thenReturn(List.of(orden));

        List<Orden> resultado = service.buscarPorIdFabricante("FAB001");

        assertEquals(1, resultado.size());
        assertEquals("FAB001", resultado.get(0).getIdFabricante());
        verify(repository).findByIdFabricante("FAB001");
    }

    @Test
    void Ingresar_Orden_Ok() {
        when(repository.save(orden)).thenReturn(orden);

        Orden resultado = service.ingresarOrden(orden);

        assertNotNull(resultado);
        assertEquals(orden, resultado);
        verify(repository).save(orden);
    }

    @Test
    void Actualizar_Orden_Ok() {
        Orden ordenActualizada = new Orden(null, "FAB002", 20, "EN_TRANSITO", "Proveedor X", LocalDate.now() );
        when(repository.findById(1L)).thenReturn(Optional.of(orden));
        when(repository.save(any(Orden.class))).thenReturn(ordenActualizada);

        Orden resultado = service.actualizarOrden(1L, ordenActualizada);

        assertEquals(1L, ordenActualizada.getId());
        assertEquals("EN_TRANSITO", resultado.getEstado());
        verify(repository).findById(1L);
        verify(repository).save(ordenActualizada);
    }

    @Test
    void Actualizar_Orden_Bad() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.actualizarOrden(99L, orden));

        assertEquals("Orden con id=99 no encontrada", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void DeleteById_ok() {
        when(repository.existsById(1L)).thenReturn(true);

        service.eliminarOrden(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void DeleteById_bad() {
        when(repository.existsById(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.eliminarOrden(99L));

        assertEquals("Orden con id=99 no encontrada", ex.getMessage());
        verify(repository, never()).deleteById(any());
    }
}