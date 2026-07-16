package com.citt.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Despacho {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idDespacho;

    @NotNull(message = "La fecha de despacho es obligatoria")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fechaDespacho;

    @NotBlank(message = "La patente del camión es obligatoria")
    private String patenteCamion;

    @NotNull(message = "El número de intentos es obligatorio")
    @Min(value = 0, message = "Los intentos no pueden ser negativos")
    private Integer intento = 0;

    @NotNull(message = "El ID de compra es obligatorio")
    private Long idCompra;

    @NotBlank(message = "La dirección de compra es obligatoria")
    private String direccionCompra;

    @NotNull(message = "El valor de compra es obligatorio")
    @Positive(message = "El valor de compra debe ser mayor que cero")
    private Long valorCompra;

    @NotNull(message = "El estado despachado es obligatorio")
    private Boolean despachado = false;
}
