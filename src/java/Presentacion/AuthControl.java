/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

import Modelo.Usuario;
import Modelo.LoginDTO;
import Servicio.AuthServicio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebServlet(name = "AuthControl", urlPatterns = {"/AuthControl"})
public class AuthControl extends HttpServlet {
    
    private AuthServicio authServicio;
    
    @Override
    public void init() throws ServletException {
        this.authServicio = new AuthServicio();
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
            accion = "mostrarLogin";
        }
        
        try {
            switch (accion) {
                case "mostrarLogin":
                    mostrarLogin(request, response);
                    break;
                case "login":
                    iniciarSesion(request, response);
                    break;
                case "logout":
                    cerrarSesion(request, response);
                    break;
                case "mostrarRecuperar":
                    mostrarRecuperarPassword(request, response);
                    break;
                case "solicitarRecuperar":
                    solicitarRecuperacion(request, response);
                    break;
                case "restablecerPassword":
                    restablecerPassword(request, response);
                    break;
                case "cambiarPassword":
                    cambiarPassword(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    private void mostrarLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
    
    private void iniciarSesion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            
            System.out.println("=== INTENTO DE LOGIN ===");
            System.out.println("Username: " + username);
            
            LoginDTO loginDTO = new LoginDTO(username, password);
            Usuario usuario = authServicio.iniciarSesion(loginDTO);
            
            // Crear sesión
            HttpSession session = request.getSession(true);
            session.setAttribute("usuario", usuario);
            session.setAttribute("idUsuario", usuario.getIdentificador());
            session.setAttribute("username", usuario.getUsername());
            session.setAttribute("rol", usuario.getRol());
            session.setAttribute("email", usuario.getEmail());
            
            System.out.println("✓ Sesión creada para: " + usuario.getUsername());
            
            // Redirigir según rol
            String dashboard = getDashboardPorRol(usuario.getRol());
            response.sendRedirect(dashboard);
            
        } catch (Exception e) {
            System.err.println("✗ Error en login: " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.setAttribute("username", request.getParameter("username"));
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
    
    private void cerrarSesion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            String username = (String) session.getAttribute("username");
            session.invalidate();
            System.out.println("✓ Sesión cerrada para: " + username);
        }
        
        response.sendRedirect("login.jsp");
    }
    

    private void mostrarRecuperarPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("recuperar-password.jsp").forward(request, response);
    }
    

    private void solicitarRecuperacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String email = request.getParameter("email");
            
            String token = authServicio.solicitarRecuperacionPassword(email);
            
            request.setAttribute("mensaje", 
                "Se ha enviado un correo a " + email + " con instrucciones para recuperar tu contraseña.");
            request.setAttribute("token", token); // Solo para pruebas
            
            request.getRequestDispatcher("recuperar-password.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("recuperar-password.jsp").forward(request, response);
        }
    }

    private void restablecerPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String token = request.getParameter("token");
            String nuevaPassword = request.getParameter("nuevaPassword");
            String confirmarPassword = request.getParameter("confirmarPassword");
            
            if (!nuevaPassword.equals(confirmarPassword)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            
            authServicio.restablecerPassword(token, nuevaPassword);
            
            request.setAttribute("mensaje", "Contraseña restablecida exitosamente. Ya puedes iniciar sesión.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("recuperar-password.jsp").forward(request, response);
        }
    }
    

    private void cambiarPassword(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("idUsuario") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            String idUsuario = (String) session.getAttribute("idUsuario");
            String passwordActual = request.getParameter("passwordActual");
            String nuevaPassword = request.getParameter("nuevaPassword");
            String confirmarPassword = request.getParameter("confirmarPassword");
            
            if (!nuevaPassword.equals(confirmarPassword)) {
                throw new IllegalArgumentException("Las contraseñas no coinciden");
            }
            
            authServicio.cambiarPassword(idUsuario, passwordActual, nuevaPassword);
            
            request.setAttribute("mensaje", "Contraseña cambiada exitosamente");
            
            String rol = (String) session.getAttribute("rol");
            String dashboard = getDashboardPorRol(rol);
            response.sendRedirect(dashboard);
            
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            String rol = (String) session.getAttribute("rol");
            String dashboard = getDashboardPorRol(rol);
            response.sendRedirect(dashboard);
        }
    }
    
    private String getDashboardPorRol(String rol) {
        switch (rol) {
            case "Voluntario":
                return "VoluntarioControl?accion=dashboard";
            case "Organizacion":
                return "OrganizacionControl?accion=miPerfil";
            case "Administrador":
                return "dashboard-admin.jsp";
            default:
                return "index.jsp";
        }
    }
}
