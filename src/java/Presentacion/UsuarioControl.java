/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

/**
 *
 * @author flori
 */

import Modelo.Usuario;
import Servicio.UsuarioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UsuarioControl", urlPatterns = {"/UsuarioControl"})
public class UsuarioControl extends HttpServlet {
    
    private UsuarioServicio usuarioServicio;
    
    @Override
    public void init() throws ServletException {
        this.usuarioServicio = new UsuarioServicio();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar sesión y rol de administrador
        if (!verificarAccesoAdmin(request, response)) {
            return;
        }
        
        String accion = request.getParameter("accion");
        
        if (accion == null || accion.isEmpty()) {
            accion = "listar";
        }
        
        try {
            switch (accion) {
                case "listar":
                    listarUsuarios(request, response);
                    break;
                case "ver":
                    verUsuario(request, response);
                    break;
                case "mostrarEditar":
                    mostrarFormularioEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarUsuario(request, response);
                    break;
                case "cambiarEstado":
                    cambiarEstado(request, response);
                    break;
                case "eliminar":
                    eliminarUsuario(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            listarUsuarios(request, response);
        }
    }

    private boolean verificarAccesoAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("usuario") == null) {
            response.sendRedirect("login.jsp");
            return false;
        }
        
        String rol = (String) session.getAttribute("rol");
        
        if (!"Administrador".equals(rol)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                "Acceso denegado. Solo administradores pueden acceder a esta función.");
            return false;
        }
        
        return true;
    }
    
    private void listarUsuarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            List<Usuario> usuarios = usuarioServicio.listarTodos();
            
            request.setAttribute("usuarios", usuarios);
            request.setAttribute("totalUsuarios", usuarios.size());
            
            // Contar por rol
            long voluntarios = usuarios.stream().filter(u -> "Voluntario".equals(u.getRol())).count();
            long organizaciones = usuarios.stream().filter(u -> "Organizacion".equals(u.getRol())).count();
            long administradores = usuarios.stream().filter(u -> "Administrador".equals(u.getRol())).count();
            
            request.setAttribute("totalVoluntarios", voluntarios);
            request.setAttribute("totalOrganizaciones", organizaciones);
            request.setAttribute("totalAdministradores", administradores);
            
            request.getRequestDispatcher("admin/listar-usuarios.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar usuarios: " + e.getMessage());
            request.getRequestDispatcher("dashboard-admin.jsp").forward(request, response);
        }
    }
    
    private void verUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String id = request.getParameter("id");
            
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ID de usuario requerido");
            }
            
            Usuario usuario = usuarioServicio.buscarPorId(id);
            
            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("admin/ver-usuario.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarUsuarios(request, response);
        }
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String id = request.getParameter("id");
            
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ID de usuario requerido");
            }
            
            Usuario usuario = usuarioServicio.buscarPorId(id);
            
            request.setAttribute("usuario", usuario);
            request.getRequestDispatcher("admin/editar-usuario.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarUsuarios(request, response);
        }
    }
    
    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String id = request.getParameter("id");
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String rol = request.getParameter("rol");
            String estado = request.getParameter("estado");
            
            // Buscar usuario existente
            Usuario usuario = usuarioServicio.buscarPorId(id);
            
            // Actualizar campos
            usuario.setUsername(username);
            usuario.setEmail(email);
            usuario.setRol(rol);
            usuario.setEstado(estado);
            
            // Solo actualizar password si se proporciona uno nuevo
            if (password != null && !password.trim().isEmpty()) {
                usuario.setPassword(password);
            }
            
            usuarioServicio.actualizar(usuario);
            
            request.setAttribute("mensaje", "Usuario actualizado exitosamente");
            listarUsuarios(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            mostrarFormularioEdicion(request, response);
        }
    }
    

    private void cambiarEstado(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String id = request.getParameter("id");
            String nuevoEstado = request.getParameter("estado");
            
            if (id == null || nuevoEstado == null) {
                throw new IllegalArgumentException("ID y estado requeridos");
            }
            
            usuarioServicio.cambiarEstado(id, nuevoEstado);
            
            request.setAttribute("mensaje", "Estado cambiado a: " + nuevoEstado);
            listarUsuarios(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarUsuarios(request, response);
        }
    }
    

    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String id = request.getParameter("id");
            
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("ID de usuario requerido");
            }
            
            // Verificar que no se está eliminando a sí mismo
            HttpSession session = request.getSession(false);
            String idUsuarioActual = (String) session.getAttribute("idUsuario");
            
            if (id.equals(idUsuarioActual)) {
                throw new IllegalArgumentException("No puedes eliminar tu propio usuario");
            }
            
            usuarioServicio.eliminar(id);
            
            request.setAttribute("mensaje", "Usuario eliminado exitosamente");
            listarUsuarios(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarUsuarios(request, response);
        }
    }
}