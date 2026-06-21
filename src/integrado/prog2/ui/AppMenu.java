/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.ui;

import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import integrado.prog2.exception.StockInvalidoException;
import integrado.prog2.service.CategoriaService;
import integrado.prog2.service.PedidoService;
import integrado.prog2.service.ProductoService;
import integrado.prog2.service.UsuarioService;
import java.util.List;
import java.util.Scanner;

public class AppMenu {

    private Scanner scanner;
    private CategoriaService categoriaService;
    private ProductoService productoService;
    private UsuarioService usuarioService;
    private PedidoService pedidoService;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        this.categoriaService = new CategoriaService();
        this.productoService = new ProductoService(categoriaService);
        this.usuarioService = new UsuarioService();
        this.pedidoService = new PedidoService();
    }

    public void iniciar() {
        int opcion;

        do {
            System.out.println("\n=== SISTEMA DE PEDIDOS (FOOD STORE) ===");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");

            opcion = leerInt("Seleccione: ");

            switch (opcion) {
                case 1:
                    menuCategorias();
                    break;
                case 2:
                    menuProductos();
                    break;
                case 3:
                    menuUsuarios();
                    break;
                case 4:
                    menuPedidos();
                    break;
                case 0:
                    System.out.println("Sistema finalizado.");
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }

    private void menuCategorias() {
        int opcion;

        do {
            System.out.println("\n--- MENU CATEGORIAS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");

            opcion = leerInt("Seleccione: ");

            switch (opcion) {
                case 1:
                    listarCategorias();
                    break;
                case 2:
                    crearCategoria();
                    break;
                case 3:
                    editarCategoria();
                    break;
                case 4:
                    eliminarCategoria();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }

    private void listarCategorias() {
        List<Categoria> categorias = categoriaService.listarActivas();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
            return;
        }

        System.out.println("\nCategorias activas:");
        for (Categoria categoria : categorias) {
            System.out.println(categoria);
        }
    }

    private void crearCategoria() {
        try {
            String nombre = leerTexto("Nombre: ");
            String descripcion = leerTexto("Descripcion: ");

            Categoria categoria = categoriaService.crearCategoria(nombre, descripcion);
            System.out.println("Categoria creada correctamente. ID generado: " + categoria.getId());

        } catch (DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarCategoria() {
        try {
            listarCategorias();
            Long id = leerLong("Ingrese ID de la categoria a editar: ");

            String nombre = leerTextoOpcional("Nuevo nombre ENTER para mantener: ");
            String descripcion = leerTextoOpcional("Nueva descripcion ENTER para mantener: ");

            categoriaService.editarCategoria(id, nombre, descripcion);
            System.out.println("Categoria actualizada correctamente.");

        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarCategoria() {
        try {
            listarCategorias();
            Long id = leerLong("Ingrese ID de la categoria a eliminar: ");

            if (confirmar("Confirma la baja logica? S/N: ")) {
                categoriaService.eliminarCategoria(id);
                System.out.println("Categoria eliminada logicamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }

        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void menuProductos() {
        int opcion;

        do {
            System.out.println("\n--- MENU PRODUCTOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");

            opcion = leerInt("Seleccione: ");

            switch (opcion) {
                case 1:
                    listarProductos();
                    break;
                case 2:
                    crearProducto();
                    break;
                case 3:
                    editarProducto();
                    break;
                case 4:
                    eliminarProducto();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }

    private void listarProductos() {
        List<Producto> productos = productoService.listarActivos();

        if (productos.isEmpty()) {
            System.out.println("No hay productos cargados.");
            return;
        }

        System.out.println("\nProductos activos:");
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }

    private void crearProducto() {
        try {
            if (categoriaService.listarActivas().isEmpty()) {
                System.out.println("Primero debe cargar al menos una categoria.");
                return;
            }

            listarCategorias();

            String nombre = leerTexto("Nombre: ");
            String descripcion = leerTexto("Descripcion: ");
            Double precio = leerDouble("Precio: ");
            int stock = leerInt("Stock: ");
            String imagen = leerTexto("Imagen: ");
            boolean disponible = leerBooleanSN("Esta disponible? S/N: ");
            Long idCategoria = leerLong("ID de categoria: ");

            Producto producto = productoService.crearProducto(nombre, descripcion, precio, stock, imagen, disponible, idCategoria);
            System.out.println("Producto creado correctamente. ID generado: " + producto.getId());

        } catch (DatoInvalidoException | EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarProducto() {
        try {
            listarProductos();

            Long id = leerLong("Ingrese ID del producto a editar: ");

            String nombre = leerTextoOpcional("Nuevo nombre ENTER para mantener: ");
            String descripcion = leerTextoOpcional("Nueva descripcion ENTER para mantener: ");
            Double precio = leerDoubleOpcional("Nuevo precio ENTER para mantener: ");
            Integer stock = leerIntOpcional("Nuevo stock ENTER para mantener: ");
            String imagen = leerTextoOpcional("Nueva imagen ENTER para mantener: ");
            Boolean disponible = leerBooleanOpcional("Disponibilidad S/N o ENTER para mantener: ");

            listarCategorias();
            Long idCategoria = leerLongOpcional("Nueva categoria ID ENTER para mantener: ");

            productoService.editarProducto(id, nombre, descripcion, precio, stock, imagen, disponible, idCategoria);
            System.out.println("Producto actualizado correctamente.");

        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        try {
            listarProductos();

            Long id = leerLong("Ingrese ID del producto a eliminar: ");

            if (confirmar("Confirma la baja logica? S/N: ")) {
                productoService.eliminarProducto(id);
                System.out.println("Producto eliminado logicamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }

        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void menuUsuarios() {
        int opcion;

        do {
            System.out.println("\n--- MENU USUARIOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear");
            System.out.println("3. Editar");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");

            opcion = leerInt("Seleccione: ");

            switch (opcion) {
                case 1:
                    listarUsuarios();
                    break;
                case 2:
                    crearUsuario();
                    break;
                case 3:
                    editarUsuario();
                    break;
                case 4:
                    eliminarUsuario();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }

    private void listarUsuarios() {
        List<Usuario> usuarios = usuarioService.listarActivos();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
            return;
        }

        System.out.println("\nUsuarios activos:");
        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }

    private void crearUsuario() {
        try {
            String nombre = leerTexto("Nombre: ");
            String apellido = leerTexto("Apellido: ");
            String mail = leerTexto("Mail: ");
            String celular = leerTexto("Celular: ");
            String contrasena = leerTexto("Contrasena: ");
            Rol rol = seleccionarRol();

            Usuario usuario = usuarioService.crearUsuario(nombre, apellido, mail, celular, contrasena, rol);
            System.out.println("Usuario creado correctamente. ID generado: " + usuario.getId());

        } catch (DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void editarUsuario() {
        try {
            listarUsuarios();

            Long id = leerLong("Ingrese ID del usuario a editar: ");

            String nombre = leerTextoOpcional("Nuevo nombre ENTER para mantener: ");
            String apellido = leerTextoOpcional("Nuevo apellido ENTER para mantener: ");
            String mail = leerTextoOpcional("Nuevo mail ENTER para mantener: ");
            String celular = leerTextoOpcional("Nuevo celular ENTER para mantener: ");
            String contrasena = leerTextoOpcional("Nueva contrasena ENTER para mantener: ");

            Rol rol = null;
            if (confirmar("Desea modificar el rol? S/N: ")) {
                rol = seleccionarRol();
            }

            usuarioService.editarUsuario(id, nombre, apellido, mail, celular, contrasena, rol);
            System.out.println("Usuario actualizado correctamente.");

        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        try {
            listarUsuarios();

            Long id = leerLong("Ingrese ID del usuario a eliminar: ");

            if (confirmar("Confirma la baja logica? S/N: ")) {
                usuarioService.eliminarUsuario(id);
                System.out.println("Usuario eliminado logicamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }

        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void menuPedidos() {
        int opcion;

        do {
            System.out.println("\n--- MENU PEDIDOS ---");
            System.out.println("1. Listar");
            System.out.println("2. Crear pedido con detalles");
            System.out.println("3. Actualizar estado / forma de pago");
            System.out.println("4. Eliminar");
            System.out.println("0. Volver");

            opcion = leerInt("Seleccione: ");

            switch (opcion) {
                case 1:
                    listarPedidos();
                    break;
                case 2:
                    crearPedido();
                    break;
                case 3:
                    actualizarPedido();
                    break;
                case 4:
                    eliminarPedido();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opcion invalida.");
            }

        } while (opcion != 0);
    }

    private void listarPedidos() {
        List<Pedido> pedidos = pedidoService.listarActivos();

        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos cargados.");
            return;
        }

        System.out.println("\nPedidos activos:");
        for (Pedido pedido : pedidos) {
            System.out.println(pedido);

            for (DetallePedido detalle : pedido.getDetallesActivos()) {
                System.out.println("   - " + detalle);
            }
        }
    }

    private void crearPedido() {
        try {
            if (usuarioService.listarActivos().isEmpty()) {
                System.out.println("Primero debe cargar al menos un usuario.");
                return;
            }

            if (productoService.listarActivos().isEmpty()) {
                System.out.println("Primero debe cargar al menos un producto.");
                return;
            }

            listarUsuarios();
            Long idUsuario = leerLong("ID del usuario: ");
            Usuario usuario = usuarioService.buscarPorId(idUsuario);

            FormaPago formaPago = seleccionarFormaPago();

            Pedido pedido = new Pedido(usuario, formaPago);

            boolean agregarOtro;

            do {
                listarProductos();
                Long idProducto = leerLong("ID del producto: ");
                Producto producto = productoService.buscarPorId(idProducto);

                int cantidad = leerInt("Cantidad: ");

                pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);

                System.out.println("Detalle agregado correctamente.");
                agregarOtro = confirmar("Desea agregar otro producto al pedido? S/N: ");

            } while (agregarOtro);

            pedidoService.guardarPedido(pedido);

            System.out.println("Pedido creado correctamente. ID generado: " + pedido.getId());
            System.out.println("Total del pedido: $" + pedido.getTotal());

        } catch (EntidadNoEncontradaException | DatoInvalidoException | StockInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("La creacion del pedido fue cancelada para evitar datos inconsistentes.");
        }
    }

    private void actualizarPedido() {
        try {
            listarPedidos();

            Long id = leerLong("Ingrese ID del pedido a actualizar: ");

            Estado estado = null;
            FormaPago formaPago = null;

            if (confirmar("Desea cambiar el estado? S/N: ")) {
                estado = seleccionarEstado();
            }

            if (confirmar("Desea cambiar la forma de pago? S/N: ")) {
                formaPago = seleccionarFormaPago();
            }

            pedidoService.actualizarPedido(id, estado, formaPago);
            System.out.println("Pedido actualizado correctamente.");

        } catch (EntidadNoEncontradaException | DatoInvalidoException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void eliminarPedido() {
        try {
            listarPedidos();

            Long id = leerLong("Ingrese ID del pedido a eliminar: ");

            if (confirmar("Confirma la baja logica? S/N: ")) {
                pedidoService.eliminarPedido(id);
                System.out.println("Pedido eliminado logicamente.");
            } else {
                System.out.println("Operacion cancelada.");
            }

        } catch (EntidadNoEncontradaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Rol seleccionarRol() {
        while (true) {
            System.out.println("Seleccione rol:");
            System.out.println("1. ADMIN");
            System.out.println("2. USUARIO");

            int opcion = leerInt("Opcion: ");

            switch (opcion) {
                case 1:
                    return Rol.ADMIN;
                case 2:
                    return Rol.USUARIO;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    private Estado seleccionarEstado() {
        while (true) {
            System.out.println("Seleccione estado:");
            System.out.println("1. PENDIENTE");
            System.out.println("2. CONFIRMADO");
            System.out.println("3. TERMINADO");
            System.out.println("4. CANCELADO");

            int opcion = leerInt("Opcion: ");

            switch (opcion) {
                case 1:
                    return Estado.PENDIENTE;
                case 2:
                    return Estado.CONFIRMADO;
                case 3:
                    return Estado.TERMINADO;
                case 4:
                    return Estado.CANCELADO;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    private FormaPago seleccionarFormaPago() {
        while (true) {
            System.out.println("Seleccione forma de pago:");
            System.out.println("1. TARJETA");
            System.out.println("2. TRANSFERENCIA");
            System.out.println("3. EFECTIVO");

            int opcion = leerInt("Opcion: ");

            switch (opcion) {
                case 1:
                    return FormaPago.TARJETA;
                case 2:
                    return FormaPago.TRANSFERENCIA;
                case 3:
                    return FormaPago.EFECTIVO;
                default:
                    System.out.println("Opcion invalida.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine();

            if (texto != null && !texto.isBlank()) {
                return texto.trim();
            }

            System.out.println("El dato no puede estar vacio.");
        }
    }

    private String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        String texto = scanner.nextLine();

        if (texto == null || texto.isBlank()) {
            return null;
        }

        return texto.trim();
    }

    private int leerInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String texto = scanner.nextLine();
                return Integer.parseInt(texto.trim());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero valido.");
            }
        }
    }

    private Integer leerIntOpcional(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String texto = scanner.nextLine();

                if (texto == null || texto.isBlank()) {
                    return null;
                }

                return Integer.parseInt(texto.trim());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero entero valido.");
            }
        }
    }

    private Long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String texto = scanner.nextLine();
                return Long.parseLong(texto.trim());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un ID valido.");
            }
        }
    }

    private Long leerLongOpcional(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String texto = scanner.nextLine();

                if (texto == null || texto.isBlank()) {
                    return null;
                }

                return Long.parseLong(texto.trim());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un ID valido.");
            }
        }
    }

    private Double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String texto = scanner.nextLine().replace(",", ".");
                return Double.parseDouble(texto.trim());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero decimal valido.");
            }
        }
    }

    private Double leerDoubleOpcional(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String texto = scanner.nextLine().replace(",", ".");

                if (texto == null || texto.isBlank()) {
                    return null;
                }

                return Double.parseDouble(texto.trim());

            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero decimal valido.");
            }
        }
    }

    private boolean leerBooleanSN(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();

            if (texto.equalsIgnoreCase("S")) {
                return true;
            }

            if (texto.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Debe ingresar S o N.");
        }
    }

    private Boolean leerBooleanOpcional(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();

            if (texto.isBlank()) {
                return null;
            }

            if (texto.equalsIgnoreCase("S")) {
                return true;
            }

            if (texto.equalsIgnoreCase("N")) {
                return false;
            }

            System.out.println("Debe ingresar S, N o ENTER.");
        }
    }

    private boolean confirmar(String mensaje) {
        return leerBooleanSN(mensaje);
    }
}
