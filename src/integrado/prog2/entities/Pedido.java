/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.entities;

import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.StockInvalidoException;
import integrado.prog2.interfaces.Calculable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pedido extends Base implements Calculable {

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private Usuario usuario;
    private List<DetallePedido> detalles;

    public Pedido(Usuario usuario, FormaPago formaPago) {
        super();
        this.fecha = LocalDate.now();
        this.estado = Estado.PENDIENTE;
        this.total = 0.0;
        this.formaPago = formaPago;
        this.usuario = usuario;
        this.detalles = new ArrayList<>();
    }

    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto)
            throws DatoInvalidoException, StockInvalidoException {

        if (producto == null) {
            throw new DatoInvalidoException("El producto no puede ser nulo.");
        }

        if (producto.isEliminado()) {
            throw new DatoInvalidoException("El producto esta eliminado.");
        }

        if (!producto.isDisponible()) {
            throw new DatoInvalidoException("El producto no esta disponible.");
        }

        if (cantidad <= 0) {
            throw new DatoInvalidoException("La cantidad debe ser mayor a cero.");
        }

        if (precioUnitario == null || precioUnitario < 0) {
            throw new DatoInvalidoException("El precio unitario no puede ser negativo.");
        }

        DetallePedido detalleExistente = findeDetallePedidoByProducto(producto);

        if (detalleExistente != null) {
            int nuevaCantidad = detalleExistente.getCantidad() + cantidad;

            if (producto.getStock() < nuevaCantidad) {
                throw new StockInvalidoException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            detalleExistente.setCantidad(nuevaCantidad);
            detalleExistente.setSubtotal(nuevaCantidad * precioUnitario);
        } else {
            if (producto.getStock() < cantidad) {
                throw new StockInvalidoException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            Double subtotal = cantidad * precioUnitario;
            DetallePedido detalle = new DetallePedido(cantidad, subtotal, producto);
            detalles.add(detalle);
        }

        calcularTotal();
    }

    public DetallePedido findeDetallePedidoByProducto(Producto producto) {
        if (producto == null) {
            return null;
        }

        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado() && detalle.getProducto().equals(producto)) {
                return detalle;
            }
        }

        return null;
    }

    public boolean deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findeDetallePedidoByProducto(producto);

        if (detalle != null) {
            detalle.setEliminado(true);
            calcularTotal();
            return true;
        }

        return false;
    }

    @Override
    public Double calcularTotal() {
        Double suma = 0.0;

        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()) {
                suma += detalle.getSubtotal();
            }
        }

        this.total = suma;
        return total;
    }

    public List<DetallePedido> getDetallesActivos() {
        List<DetallePedido> activos = new ArrayList<>();

        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()) {
                activos.add(detalle);
            }
        }

        return activos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    @Override
    public String toString() {
        String nombreUsuario = usuario != null
                ? usuario.getNombre() + " " + usuario.getApellido()
                : "Sin usuario";

        return "ID: " + getId()
                + " | Usuario: " + nombreUsuario
                + " | Fecha: " + fecha
                + " | Estado: " + estado
                + " | Forma de pago: " + formaPago
                + " | Total: $" + total;
    }
}

