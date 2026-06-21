/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.service;

import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.DatoInvalidoException;
import integrado.prog2.exception.EntidadNoEncontradaException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private List<Usuario> usuarios;

    public UsuarioService() {
        this.usuarios = new ArrayList<>();
    }

    public Usuario crearUsuario(String nombre, String apellido, String mail, String celular,
            String contrasena, Rol rol) throws DatoInvalidoException {

        validarTexto(nombre, "nombre");
        validarTexto(apellido, "apellido");
        validarMail(mail);

        if (existeMail(mail, null)) {
            throw new DatoInvalidoException("Ya existe un usuario con ese mail.");
        }

        Usuario usuario = new Usuario(nombre.trim(), apellido.trim(), mail.trim(), celular.trim(), contrasena.trim(), rol);
        usuarios.add(usuario);

        return usuario;
    }

    public List<Usuario> listarActivos() {
        List<Usuario> activos = new ArrayList<>();

        for (Usuario usuario : usuarios) {
            if (!usuario.isEliminado()) {
                activos.add(usuario);
            }
        }

        return activos;
    }

    public Usuario buscarPorId(Long id) throws EntidadNoEncontradaException {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id) && !usuario.isEliminado()) {
                return usuario;
            }
        }

        throw new EntidadNoEncontradaException("No se encontro un usuario activo con ID: " + id);
    }

    public void editarUsuario(Long id, String nombre, String apellido, String mail,
            String celular, String contrasena, Rol rol)
            throws EntidadNoEncontradaException, DatoInvalidoException {

        Usuario usuario = buscarPorId(id);

        if (nombre != null && !nombre.isBlank()) {
            usuario.setNombre(nombre.trim());
        }

        if (apellido != null && !apellido.isBlank()) {
            usuario.setApellido(apellido.trim());
        }

        if (mail != null && !mail.isBlank()) {
            validarMail(mail);

            if (existeMail(mail, usuario.getId())) {
                throw new DatoInvalidoException("Ya existe otro usuario con ese mail.");
            }

            usuario.setMail(mail.trim());
        }

        if (celular != null && !celular.isBlank()) {
            usuario.setCelular(celular.trim());
        }

        if (contrasena != null && !contrasena.isBlank()) {
            usuario.setContrasena(contrasena.trim());
        }

        if (rol != null) {
            usuario.setRol(rol);
        }
    }

    public void eliminarUsuario(Long id) throws EntidadNoEncontradaException {
        Usuario usuario = buscarPorId(id);
        usuario.setEliminado(true);
    }

    private boolean existeMail(String mail, Long idActual) {
        for (Usuario usuario : usuarios) {
            boolean mismoMail = usuario.getMail().equalsIgnoreCase(mail.trim());
            boolean distintoId = idActual == null || !usuario.getId().equals(idActual);

            if (mismoMail && distintoId) {
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

    private void validarMail(String mail) throws DatoInvalidoException {
        if (mail == null || mail.isBlank()) {
            throw new DatoInvalidoException("El mail no puede estar vacio.");
        }

        if (!mail.contains("@")) {
            throw new DatoInvalidoException("El mail debe tener un formato valido.");
        }
    }
}

