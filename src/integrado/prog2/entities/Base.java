/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Base {

    private static long contadorId = 1;

    private Long id;
    private boolean eliminado;
    private LocalDateTime createdAt;

    public Base() {
        this.id = contadorId++;
        this.eliminado = false;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Base otraEntidad = (Base) obj;
        return Objects.equals(id, otraEntidad.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
