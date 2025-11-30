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
import Modelo.Voluntario;
import Servicio.VoluntarioServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;


@WebServlet(name = "VoluntarioControl", urlPatterns = {"/VoluntarioControl"})
public class VoluntarioControl extends HttpServlet {
    
    private VoluntarioServicio voluntarioServicio;
    
    @Override
    public void init() throws ServletException {
        this.voluntarioServicio = new VoluntarioServicio();
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
                    registrarVoluntario(request, response);
                    break;
                case "listar":
                    listarVoluntarios(request, response);
                    break;
                case "ver":
                    verVoluntario(request, response);
                    break;
                case "mostrarEditar":
                    mostrarFormularioEdicion(request, response);
                    break;
                case "actualizar":
                    actualizarVoluntario(request, response);
                    break;
                case "eliminar":
                    eliminarVoluntario(request, response);
                    break;
                case "miPerfil":
                    verMiPerfil(request, response);
                    break;
                case "dashboard":
                    mostrarDashboardVoluntario(request, response);
                break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("registro-voluntario.jsp").forward(request, response);
        }
    }
    
    private void mostrarFormularioRegistro(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("registro-voluntario.jsp").forward(request, response);
    }
    
    private void registrarVoluntario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            System.out.println("=== REGISTRO DE VOLUNTARIO ===");
            
            // Datos del usuario
            String username = request.getParameter("username");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String confirmarPassword = request.getParameter("confirmarPassword");
            
            // Datos del voluntario
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String fechaNacimientoStr = request.getParameter("fechaNacimiento");
            String direccion = request.getParameter("direccion");
            String telefono = request.getParameter("telefono");
            
            System.out.println("Username: " + username);
            System.out.println("Nombre: " + nombre + " " + apellido);
            
            // Validar contraseñas
            if (!password.equals(confirmarPassword)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            
            // Convertir fecha
            LocalDate fechaNacimiento = null;
            if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
                fechaNacimiento = LocalDate.parse(fechaNacimientoStr);
            }
            
            // Crear voluntario
            Voluntario voluntario = new Voluntario(
                username, email, password,
                nombre, apellido, fechaNacimiento,
                direccion, telefono
            );
            
            voluntarioServicio.registrarVoluntario(voluntario);
            
            request.setAttribute("mensaje", 
                "¡Registro exitoso! Ya puedes iniciar sesión con tu usuario y contraseña.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("✗ Error en registro: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            
            // Mantener los datos en el formulario
            request.setAttribute("username", request.getParameter("username"));
            request.setAttribute("email", request.getParameter("email"));
            request.setAttribute("nombre", request.getParameter("nombre"));
            request.setAttribute("apellido", request.getParameter("apellido"));
            request.setAttribute("fechaNacimiento", request.getParameter("fechaNacimiento"));
            request.setAttribute("direccion", request.getParameter("direccion"));
            request.setAttribute("telefono", request.getParameter("telefono"));
            
            request.getRequestDispatcher("registro-voluntario.jsp").forward(request, response);
        }
    }

    private void listarVoluntarios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Verificar que sea administrador
        HttpSession session = request.getSession(false);
        if (session == null || !"Administrador".equals(session.getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        
        try {
            List<Voluntario> voluntarios = voluntarioServicio.listarTodos();
            
            request.setAttribute("voluntarios", voluntarios);
            request.setAttribute("totalVoluntarios", voluntarios.size());
            
            request.getRequestDispatcher("admin/listar-voluntarios.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar voluntarios: " + e.getMessage());
            request.getRequestDispatcher("dashboard-admin.jsp").forward(request, response);
        }
    }

    private void verVoluntario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String idUsuario = request.getParameter("id");
            
            if (idUsuario == null || idUsuario.isEmpty()) {
                throw new IllegalArgumentException("ID de usuario requerido");
            }
            
            Voluntario voluntario = voluntarioServicio.buscarPorIdUsuario(idUsuario);
            
            request.setAttribute("voluntario", voluntario);
            request.getRequestDispatcher("admin/ver-voluntario.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarVoluntarios(request, response);
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
            Voluntario voluntario = voluntarioServicio.buscarPorIdUsuario(idUsuario);
            
            request.setAttribute("voluntario", voluntario);
            request.getRequestDispatcher("voluntario/mi-perfil.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            String proyectosActivos = contarProyectosActivos();
            request.setAttribute("proyectosActivos", proyectosActivos);

            request.getRequestDispatcher("dashboard-voluntario.jsp").forward(request, response);
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
            
            // Si no se proporciona ID, usar el del usuario actual
            if (idUsuario == null || idUsuario.isEmpty()) {
                idUsuario = (String) session.getAttribute("idUsuario");
            }
            
            Voluntario voluntario = voluntarioServicio.buscarPorIdUsuario(idUsuario);
            
            request.setAttribute("voluntario", voluntario);
            
            // Determinar qué vista mostrar según el rol
            String rol = (String) session.getAttribute("rol");
            if ("Administrador".equals(rol)) {
                request.getRequestDispatcher("admin/editar-voluntario.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("voluntario/editar-perfil.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            response.sendRedirect("dashboard-voluntario.jsp");
        }
    }
    

    private void actualizarVoluntario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String idUsuario = request.getParameter("id");
            String nombre = request.getParameter("nombre");
            String apellido = request.getParameter("apellido");
            String fechaNacimientoStr = request.getParameter("fechaNacimiento");
            String direccion = request.getParameter("direccion");
            String telefono = request.getParameter("telefono");
            
            // Buscar voluntario existente
            Voluntario voluntario = voluntarioServicio.buscarPorIdUsuario(idUsuario);
            
            // Actualizar datos
            voluntario.setNombre(nombre);
            voluntario.setApellido(apellido);
            voluntario.setDireccion(direccion);
            voluntario.setTelefono(telefono);
            
            if (fechaNacimientoStr != null && !fechaNacimientoStr.isEmpty()) {
                voluntario.setFechaNacimiento(LocalDate.parse(fechaNacimientoStr));
            }
            
            voluntarioServicio.actualizar(voluntario);
            
            request.setAttribute("mensaje", "Perfil actualizado exitosamente");
            verMiPerfil(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            mostrarFormularioEdicion(request, response);
        }
    }
    

    private void eliminarVoluntario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || !"Administrador".equals(session.getAttribute("rol"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acceso denegado");
            return;
        }
        
        try {
            String idUsuario = request.getParameter("id");
            
            if (idUsuario == null || idUsuario.isEmpty()) {
                throw new IllegalArgumentException("ID de usuario requerido");
            }
            
            voluntarioServicio.eliminar(idUsuario);
            
            request.setAttribute("mensaje", "Voluntario eliminado exitosamente");
            listarVoluntarios(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            listarVoluntarios(request, response);
        }
    }
    
    private void mostrarDashboardVoluntario(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String proyectosActivos = contarProyectosActivos();
    request.setAttribute("proyectosActivos", proyectosActivos);

    request.getRequestDispatcher("dashboard-voluntario.jsp").forward(request, response);
}

    
    private String contarProyectosActivos() {
    try {
        URL url = new URL("http://localhost:8081/MS-Proyectos/ProyectoControl");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        String parametros = "accion=contarActivos";
        con.getOutputStream().write(parametros.getBytes("UTF-8"));

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String respuesta = in.readLine();
        in.close();

        return respuesta; 
    } catch (Exception e) {
        e.printStackTrace();
        return "0";
    }
}

}
