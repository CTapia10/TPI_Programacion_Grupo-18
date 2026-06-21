
package integrado.prog2.service;


import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {
    

    private List<Producto> productos;
    private CategoriaService categoriaService;

    public ProductoService(CategoriaService categoriaService) {
        this.productos = new ArrayList<>();
        this.categoriaService = categoriaService;
    }

    public Producto crearProducto(String nombre, String descripcion, Double precio, int stock,
            String imagen, boolean disponible, Long idCategoria)
            throws DatoInvalidoException, EntidadNoEncontradaException {

        validarTexto(nombre, "nombre");
        validarPrecio(precio);
        validarStock(stock);

        Categoria categoria = categoriaService.buscarPorId(idCategoria);

        Producto producto = new Producto(nombre.trim(), precio, descripcion.trim(), stock, imagen.trim(), disponible, categoria);
        productos.add(producto);
        categoria.addProducto(producto);

        return producto;
    }

    public List<Producto> listarActivos() {
        List<Producto> activos = new ArrayList<>();

        for (Producto producto : productos) {
            if (!producto.isEliminado()) {
                activos.add(producto);
            }
        }

        return activos;
    }

    public Producto buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Producto producto : productos) {
            if (producto.getId().equals(id) && !producto.isEliminado()) {
                return producto;
            }
        }

        throw new EntidadNoEncontradaException("No se encontro un producto activo con ID: " + id);
    }

    public void editarProducto(Long id, String nombre, String descripcion, Double precio,
            Integer stock, String imagen, Boolean disponible, Long idCategoria)
            throws EntidadNoEncontradaException, DatoInvalidoException {

        Producto producto = buscarPorId(id);

        if (nombre != null && !nombre.isBlank()) {
            producto.setNombre(nombre.trim());
        }

        if (descripcion != null && !descripcion.isBlank()) {
            producto.setDescripcion(descripcion.trim());
        }

        if (precio != null) {
            validarPrecio(precio);
            producto.setPrecio(precio);
        }

        if (stock != null) {
            validarStock(stock);
            producto.setStock(stock);
        }

        if (imagen != null && !imagen.isBlank()) {
            producto.setImagen(imagen.trim());
        }

        if (disponible != null) {
            producto.setDisponible(disponible);
        }

        if (idCategoria != null) {
            Categoria nuevaCategoria = categoriaService.buscarPorId(idCategoria);

            if (producto.getCategoria() != null) {
                producto.getCategoria().getProductos().remove(producto);
            }

            producto.setCategoria(nuevaCategoria);
            nuevaCategoria.addProducto(producto);
        }
    }

    public void eliminarProducto(Long id) throws EntidadNoEncontradaException {
        Producto producto = buscarPorId(id);
        producto.setEliminado(true);
    }

    private void validarTexto(String texto, String campo) throws DatoInvalidoException {
        if (texto == null || texto.isBlank()) {
            throw new DatoInvalidoException("El campo " + campo + " no puede estar vacio.");
        }
    }

    private void validarPrecio(Double precio) throws DatoInvalidoException {
        if (precio == null || precio < 0) {
            throw new DatoInvalidoException("El precio no puede ser negativo.");
        }
    }

    private void validarStock(Integer stock) throws DatoInvalidoException {
        if (stock == null || stock < 0) {
            throw new DatoInvalidoException("El stock no puede ser negativo.");
        }
    }
}
