package com.juanjojmnz.conversormonedas.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conversor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String monedaOrigen;
    private String monedaDestino;
    private double cantidad;
    private double resultado;

    private LocalDateTime fechaHora;
}
