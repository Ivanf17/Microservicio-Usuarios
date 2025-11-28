/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Modelo.Organizacion;
import Servicio.OrganizacionServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrganizacionControl", urlPatterns = {"/OrganizacionControl"})
public class OrganizacionControl extends HttpServlet {
    
    private OrganizacionServicio organizacionServicio;
    
    @Override
    public void init() throws ServletException {
        this.organizacionServicio = new OrganizacionServicio();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null || accion.isEmpty()) {
            accion = "mostrarRegistro";
        }
        
        try {
            switch (accion) {
                case "mostrarRegistro":
                    mostrarFormularioRegistro(request, response);
                    break;
                case "registrar":
                    registrarOrganizacion(request, response);
                    break;
                case "listar":
                    listarOrganizaciones(request, response);
                    break;
                case "listarPendientes":
                    listarPendientes(request, response);
                    break;
                case "ver":
                    verOrganizacion(request, response);
                    break;
                case "validar":
                    validarOrganizacion(request, response);
                    break;
                case "mostrarEditar":
                    mostrarFormularioEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarOrganizacion(request, response);
                    break;
                case "eliminar":
                    eliminarOrganizacion(request, response);
                    break;
                case "miPerfil":
                    verMiPerfil(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("registro-organizacion.jsp").forward(request, response);
        }
    }

    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("registro-organizacion.jsp").forward(request, response);
    }
    

    private void registrarOrganizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            System.out.println("=== REGISTRO DE ORGANIZACIÓN ===");
            
            // Datos del usuario
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmarPassword = request.getParameter("confirmarPassword");
            
            // Datos de la organización
            String nombre = request.getParameter("nombre");
            String nit = request.getParameter("nit");
            String representante = request.getParameter("representante");
            String direccion = request.getParameter("direccion");
            String telefono = request.getParameter("telefono");
            String tipo = request.getParameter("tipo");
            
            System.out.println("Username: " + username);
            System.out.println("Organización: " + nombre);
            System.out.println("NIT: " + nit);
            
            // Validar contraseñas
            if (!password.equals(confirmarPassword)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            
            // Crear organización
            Organizacion organizacion = new Organizacion(
                username, email, password,
                nombre, direccion, telefono,
                tipo, nit, representante
            );
            
            organizacionServicio.registrarOrganizacion(organizacion);
            
            request.setAttribute("mensaje", 
                "¡Registro exitoso! Tu solicitud está pendiente de validación por el administrador. " +
                "Recibirás un correo cuando sea aprobada.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("✗ Error en registro: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            
            // Mantener los datos en el formulario
            request.setAttribute("username", request.getParameter("username"));
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("nombre", request.getParameter("nombre"));
            request.setAttribute("nit", request.getParameter("nit"));
            request.setAttribute("representante", request.getParameter("representante"));
            request.setAttribute("direccion", request.getParameter("direccion"));
            request.setAttribute("telefono", request.getParameter("telefono"));
            request.setAttribute("tipo", request.getParameter("tipo"));
            
            request.getRequestDispatcher("registro-organizacion.jsp").forward(request, response);
        }
    }
    
    private void listarOrganizaciones(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (!verificarAccesoAdmin(request, response)) {
            return;
        }
        
        try {
            List<Organizacion> organizaciones = organizacionServicio.listarTodas();
            
            request.setAttribute("organizaciones", organizaciones);
            request.setAttribute("totalOrganizaciones", organizaciones.size());
            
            // Contar por estado
            long pendientes = organizaciones.stream()
                .filter(o -> "Pendiente".equals(o.getEstadoValidacion())).count();
            long aprobadas = organizaciones.stream()
                .filter(o -> "Aprobado".equals(o.getEstadoValidacion())).count();
            long rechazadas = organizaciones.stream()
                .filter(o -> "Rechazado".equals(o.getEstadoValidacion())).count();
            
            request.setAttribute("pendientes", pendientes);
            request.setAttribute("aprobadas", aprobadas);
            request.setAttribute("rechazadas", rechazadas);
            
            request.getRequestDispatcher("admin/listar-organizaciones.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar organizaciones: " + e.getMessage());
            request.getRequestDispatcher("dashboard-admin.jsp").forward(request, response);
        }
    }
    
    private void listarPendientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (!verificarAccesoAdmin(request, response)) {
            return;
        }
        
        try {
            List<Organizacion> pendientes = organizacionServicio.listarPendientes();
            
            request.setAttribute("organizaciones", pendientes);
            request.setAttribute("totalPendientes", pendientes.size());
            
            request.getRequestDispatcher("admin/validar-organizaciones.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar organizaciones: " + e.getMessage());
            request.getRequestDispatcher("dashboard-admin.jsp").forward(request, response);
        }
    }
    
    private void verOrganizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String idUsuario = request.getParameter("id");
            
            if (idUsuario == null || idUsuario.isEmpty()) {
                throw new IllegalArgumentException("ID de usuario requerido");
            }
            
            Organizacion organizacion = organizacionServicio.buscarPorIdUsuario(idUsuario);
            
            request.setAttribute("organizacion", organizacion);
            request.getRequestDispatcher("admin/ver-organizacion.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarOrganizaciones(request, response);
        }
    }

    private void validarOrganizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (!verificarAccesoAdmin(request, response)) {
            return;
        }
        
        try {
            String idUsuario = request.getParameter("id");
            String decision = request.getParameter("decision"); // "aprobar" o "rechazar"
            String motivo = request.getParameter("motivo");
            
            if (idUsuario == null || decision == null) {
                throw new IllegalArgumentException("Datos incompletos");
            }
            
            boolean aprobada = "aprobar".equals(decision);
            
            organizacionServicio.validarOrganizacion(idUsuario, aprobada, motivo);
            
            String mensaje = aprobada ? 
                "Organización aprobada exitosamente" : 
                "Organización rechazada";
            
            request.setAttribute("mensaje", mensaje);
            listarPendientes(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarPendientes(request, response);
        }
    }

    private void verMiPerfil(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            String idUsuario = (String) session.getAttribute("idUsuario");
            Organizacion organizacion = organizacionServicio.buscarPorIdUsuario(idUsuario);
            
            request.setAttribute("organizacion", organizacion);
            request.getRequestDispatcher("organizacion/mi-perfil.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("dashboard-organizacion.jsp").forward(request, response);
        }
    }
    

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            String idUsuario = request.getParameter("id");
            
            if (idUsuario == null || idUsuario.isEmpty()) {
                idUsuario = (String) session.getAttribute("idUsuario");
            }
            
            Organizacion organizacion = organizacionServicio.buscarPorIdUsuario(idUsuario);
            
            request.setAttribute("organizacion", organizacion);
            
            String rol = (String) session.getAttribute("rol");
            if ("Administrador".equals(rol)) {
                request.getRequestDispatcher("admin/editar-organizacion.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("organizacion/editar-perfil.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            response.sendRedirect("dashboard-organizacion.jsp");
        }
    }
    

    private void actualizarOrganizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String idUsuario = request.getParameter("id");
            String nombre = request.getParameter("nombre");
            String direccion = request.getParameter("direccion");
            String telefono = request.getParameter("telefono");
            String tipo = request.getParameter("tipo");
            String representante = request.getParameter("representante");
            
            Organizacion organizacion = organizacionServicio.buscarPorIdUsuario(idUsuario);
            
            organizacion.setNombre(nombre);
            organizacion.setDireccion(direccion);
            organizacion.setTelefono(telefono);
            organizacion.setTipo(tipo);
            organizacion.setRepresentante(representante);
            
            organizacionServicio.actualizar(organizacion);
            
            request.setAttribute("mensaje", "Perfil actualizado exitosamente");
            verMiPerfil(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            mostrarFormularioEdicion(request, response);
        }
    }
    

    private void eliminarOrganizacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        if (!verificarAccesoAdmin(request, response)) {
            return;
        }
        
        try {
            String idUsuario = request.getParameter("id");
            
            if (idUsuario == null || idUsuario.isEmpty()) {
                throw new IllegalArgumentException("ID de usuario requerido");
            }
            
            organizacionServicio.eliminar(idUsuario);
            
            request.setAttribute("mensaje", "Organización eliminada exitosamente");
            listarOrganizaciones(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarOrganizaciones(request, response);
        }
    }
    

    private boolean verificarAccesoAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || !"Administrador".equals(session.getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return false;
        }
        
        return true;
    }
}