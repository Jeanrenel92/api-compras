package cl.duoc.api_compras.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "ORDEN_DE_COMPRAS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;


    @NotBlank(message = "El Id_Fabricante no puede estar vacío")
    @Column(name = "ID_FABRICANTE", nullable = false)
    private String idFabricante;


    @Positive(message = "El campo cantidad no acepta valor negativo")
    @Column(name = "CANTIDAD", nullable = false)
    private Integer unidad;


    @NotBlank(message = "El estado debe ser: PENDIENTE, EN_TRANSITO, ADUANA, ENTREGADA")
    @Pattern(regexp = "PENDIENTE|EN_TRANSITO|ADUANA|ENTREGADA",
            message = "El estado debe ser: PENDIENTE, EN_TRANSITO, ADUANA, ENTREGADA")
    @Column(name = "ESTADO", nullable = false)
    private String estado;


    @NotBlank(message = "El nombre del proveedor no puede estar vacío")
    @Column(name = "NOMBRE_PROVEEDOR")
    private String nomProveedor;


    @NotNull(message = "Este campo es obligatorio")
    @Column(name = "FECHA_SOLICITUD")
    private Date fechaOrden;
}
