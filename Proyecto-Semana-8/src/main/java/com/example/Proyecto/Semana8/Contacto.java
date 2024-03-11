package com.example.Proyecto.Semana8;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author jorge
 */
@Data
@Entity
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;
    private String nombre;
    private LocalDateTime fecha;

    @PrePersist
    public void antesDePersistir() {
        fecha = LocalDateTime.now(); // Asigna la fecha actual antes de persistir
    }

}
