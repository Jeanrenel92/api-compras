package cl.duoc.api_compras.Model;

import cl.duoc.api_compras.model.Orden;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class OrdenTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void closeFactory() {
        factory.close();
    }

    private Orden ordenValida() {
        return new Orden(1L, "FAB001", 10, "PENDIENTE", "Proveedor SPA", LocalDate.now());
    }

    @Test
    void ordenValida_sinViolaciones() {
        Set<ConstraintViolation<Orden>> violaciones = validator.validate(ordenValida());
        assertTrue(violaciones.isEmpty());
    }

    @Test
    void idFabricante_vacio_generaViolacion() {
        Orden orden = ordenValida();
        orden.setIdFabricante("");

        Set<ConstraintViolation<Orden>> violaciones = validator.validate(orden);

        assertFalse(violaciones.isEmpty());
        assertEquals("El Id_Fabricante no puede estar vacío",
                violaciones.iterator().next().getMessage());
    }

    @Test
    void unidad_negativa_generaViolacion() {
        Orden orden = ordenValida();
        orden.setUnidad(-5);

        Set<ConstraintViolation<Orden>> violaciones = validator.validate(orden);

        assertFalse(violaciones.isEmpty());
        assertEquals("El campo cantidad no acepta valor negativo",
                violaciones.iterator().next().getMessage());
    }

    @Test
    void estado_invalido_generaViolacion() {
        Orden orden = ordenValida();
        orden.setEstado("DESPACHADA");

        Set<ConstraintViolation<Orden>> violaciones = validator.validate(orden);

        assertFalse(violaciones.isEmpty());
        assertEquals("El estado debe ser: PENDIENTE, EN_TRANSITO, ADUANA, ENTREGADA",
                violaciones.iterator().next().getMessage());
    }

    @Test
    void estado_vacio_generaViolacion() {
        Orden orden = ordenValida();
        orden.setEstado("");

        Set<ConstraintViolation<Orden>> violaciones = validator.validate(orden);

        assertFalse(violaciones.isEmpty());
    }

    @Test
    void nomProveedor_vacio_generaViolacion() {
        Orden orden = ordenValida();
        orden.setNomProveedor("");

        Set<ConstraintViolation<Orden>> violaciones = validator.validate(orden);

        assertFalse(violaciones.isEmpty());
        assertEquals("El nombre del proveedor no puede estar vacío",
                violaciones.iterator().next().getMessage());
    }

    @Test
    void gettersYSetters_funcionanCorrectamente() {
        Orden orden = new Orden();
        LocalDate fecha = LocalDate.now();

        orden.setId(1L);
        orden.setIdFabricante("FAB002");
        orden.setUnidad(15);
        orden.setEstado("ADUANA");
        orden.setNomProveedor("Proveedor Test");
        orden.setFechaOrden(fecha);

        assertEquals(1L, orden.getId());
        assertEquals("FAB002", orden.getIdFabricante());
        assertEquals(15, orden.getUnidad());
        assertEquals("ADUANA", orden.getEstado());
        assertEquals("Proveedor Test", orden.getNomProveedor());
        assertEquals(fecha, orden.getFechaOrden());
    }
}