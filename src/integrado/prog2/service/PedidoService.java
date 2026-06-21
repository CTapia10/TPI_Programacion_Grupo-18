

package integrado.prog2.service;


import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.StockInvalidoException;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {

    private List<Pedido> pedidos;

    public PedidoService() {
        this.pedidos = new ArrayList<>();
    }

    public Pedido guardarPedido(Pedido pedido) throws DatoInvalidoException, StockInvalidoException {
        if (pedido == null) {
            throw new DatoInvalidoException("El pedido no puede ser nulo.");
        }

        if (pedido.getUsuario() == null || pedido.getUsuario().isEliminado()) {
            throw new DatoInvalidoException("El pedido debe tener un usuario activo.");
        }

        if (pedido.getDetallesActivos().isEmpty()) {
            throw new DatoInvalidoException("El pedido debe tener al menos un detalle.");
        }

        for (DetallePedido detalle : pedido.getDetallesActivos()) {
            Producto producto = detalle.getProducto();

            if (producto == null || producto.isEliminado()) {
                throw new DatoInvalidoException("Uno de los productos del pedido no es valido.");
            }

            if (producto.getStock() < detalle.getCantidad()) {
                throw new StockInvalidoException("Stock insuficiente para el producto: " + producto.getNombre());
            }
        }

        pedido.calcularTotal();

        for (DetallePedido detalle : pedido.getDetallesActivos()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() - detalle.getCantidad());
        }

        pedidos.add(pedido);
        pedido.getUsuario().addPedido(pedido);

        return pedido;
    }

    public List<Pedido> listarActivos() {
        List<Pedido> activos = new ArrayList<>();

        for (Pedido pedido : pedidos) {
            if (!pedido.isEliminado()) {
                activos.add(pedido);
            }
        }

        return activos;
    }

    public Pedido buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Pedido pedido : pedidos) {
            if (pedido.getId().equals(id) && !pedido.isEliminado()) {
                return pedido;
            }
        }

        throw new EntidadNoEncontradaException("No se encontro un pedido activo con ID: " + id);
    }

    public void actualizarPedido(Long id, Estado estado, FormaPago formaPago)
            throws EntidadNoEncontradaException, DatoInvalidoException {

        Pedido pedido = buscarPorId(id);

        if (estado == null && formaPago == null) {
            throw new DatoInvalidoException("Debe modificar al menos un dato del pedido.");
        }

        if (estado != null) {
            pedido.setEstado(estado);
        }

        if (formaPago != null) {
            pedido.setFormaPago(formaPago);
        }
    }

    public void eliminarPedido(Long id) throws EntidadNoEncontradaException {
        Pedido pedido = buscarPorId(id);
        pedido.setEliminado(true);

        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setEliminado(true);
        }
    }
}
