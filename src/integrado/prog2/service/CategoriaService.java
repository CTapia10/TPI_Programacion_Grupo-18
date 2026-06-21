/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.service;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {

    private List<Categoria> categorias;

    public CategoriaService() {
        this.categorias = new ArrayList<>();
    }

    public Categoria crearCategoria(String nombre, String descripcion) throws DatoInvalidoException {
        validarTexto(nombre, "nombre");

        if (existeNombre(nombre, null)) {
            throw new DatoInvalidoException("Ya existe una categoria con ese nombre.");
        }

        Categoria categoria = new Categoria(nombre.trim(), descripcion.trim());
        categorias.add(categoria);

        return categoria;
    }

    public List<Categoria> listarActivas() {
        List<Categoria> activas = new ArrayList<>();

        for (Categoria categoria : categorias) {
            if (!categoria.isEliminado()) {
                activas.add(categoria);
            }
        }

        return activas;
    }

    public Categoria buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id) && !categoria.isEliminado()) {
                return categoria;
            }
        }

        throw new EntidadNoEncontradaException("No se encontro una categoria activa con ID: " + id);
    }

    public void editarCategoria(Long id, String nombre, String descripcion)
            throws EntidadNoEncontradaException, DatoInvalidoException {

        Categoria categoria = buscarPorId(id);

        if (nombre != null && !nombre.isBlank()) {
            if (existeNombre(nombre, categoria.getId())) {
                throw new DatoInvalidoException("Ya existe otra categoria con ese nombre.");
            }

            categoria.setNombre(nombre.trim());
        }

        if (descripcion != null && !descripcion.isBlank()) {
            categoria.setDescripcion(descripcion.trim());
        }
    }

    public void eliminarCategoria(Long id)
            throws EntidadNoEncontradaException, DatoInvalidoException {

        Categoria categoria = buscarPorId(id);

        for (Producto producto : categoria.getProductos()) {
            if (!producto.isEliminado()) {
                throw new DatoInvalidoException("No se puede eliminar la categoria porque tiene productos activos asociados.");
            }
        }

        categoria.setEliminado(true);
    }

    private boolean existeNombre(String nombre, Long idActual) {
        for (Categoria categoria : categorias) {
            boolean mismoNombre = categoria.getNombre().equalsIgnoreCase(nombre.trim());
            boolean distintoId = idActual == null || !categoria.getId().equals(idActual);

            if (mismoNombre && distintoId && !categoria.isEliminado()) {
                return true;
            }
        }

        return false;
    }

    private void validarTexto(String texto, String campo) throws DatoInvalidoException {
        if (texto == null || texto.isBlank()) {
            throw new DatoInvalidoException("El campo " + campo + " no puede estar vacio.");
        }
    }
}

