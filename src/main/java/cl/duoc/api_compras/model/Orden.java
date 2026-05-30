package cl.duoc.api_compras.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


    @Column(name = "ID_FABRICANTE", nullable = false)
    private String idFabricante;

    @Column(name = "CANTIDAD", nullable = false)
    @Positive
    private Integer cantidad;

    @NotBlank
    @Column(name = "ESTADO", nullable = false)
    private String estado;


    @NotBlank
    @Column(name = "FECHA")
    private Date fechaOrden;

    @NotBlank
    @Column(name = "NOMBRE_PROVEEDOR")
    private String nomProveedor;

}
