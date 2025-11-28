<%-- 
    Document   : validar-organizaciones
    Created on : 20 nov 2025, 21:26:44
    Author     : flori
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.Organizacion" %>
<%@ page import="java.util.List" %>
<%
    @SuppressWarnings("unchecked")
    List<Organizacion> organizaciones = (List<Organizacion>) request.getAttribute("organizaciones");
    Integer totalPendientes = (Integer) request.getAttribute("totalPendientes");
    if (totalPendientes == null) totalPendientes = 0;
    
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Validar Organizaciones - SGVC</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f5f7fa;
        }
        
        .navbar {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .navbar-brand {
            font-size: 1.5rem;
            font-weight: 700;
        }
        
        .navbar-menu a {
            color: white;
            text-decoration: none;
            padding: 8px 16px;
            border-radius: 8px;
            transition: background 0.3s;
        }
        
        .navbar-menu a:hover {
            background: rgba(255, 255, 255, 0.2);
        }
        
        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }
        
        .header {
            background: white;
            padding: 30px;
            border-radius: 15px 15px 0 0;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .header h1 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .header p {
            color: #666;
        }
        
        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .content {
            background: white;
            padding: 30px;
            border-radius: 0 0 15px 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        
        .empty-state .icon {
            font-size: 5rem;
            margin-bottom: 20px;
        }
        
        .org-card {
            background: #f8f9fa;
            border: 2px solid #e0e0e0;
            border-radius: 12px;
            padding: 25px;
            margin-bottom: 20px;
            transition: all 0.3s;
        }
        
        .org-card:hover {
            border-color: #f5576c;
            box-shadow: 0 4px 15px rgba(245, 87, 108, 0.1);
        }
        
        .org-header {
            display: flex;
            justify-content: space-between;
            align-items: start;
            margin-bottom: 20px;
        }
        
        .org-title {
            flex: 1;
        }
        
        .org-title h3 {
            color: #333;
            font-size: 1.4rem;
            margin-bottom: 5px;
        }
        
        .org-title .nit {
            color: #666;
            font-size: 0.95rem;
        }
        
        .badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 600;
        }
        
        .badge-pending {
            background: #fff3cd;
            color: #856404;
        }
        
        .org-details {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 15px;
            margin-bottom: 20px;
        }
        
        .detail-item {
            display: flex;
            align-items: center;
            gap: 10px;
            color: #555;
        }
        
        .detail-item .icon {
            color: #f5576c;
            font-size: 1.2rem;
        }
        
        .org-actions {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s;
        }
        
        .btn-approve {
            background: #28a745;
            color: white;
        }
        
        .btn-approve:hover {
            background: #218838;
            transform: translateY(-2px);
        }
        
        .btn-reject {
            background: #dc3545;
            color: white;
        }
        
        .btn-reject:hover {
            background: #c82333;
            transform: translateY(-2px);
        }
        
        .btn-view {
            background: #667eea;
            color: white;
        }
        
        .btn-view:hover {
            background: #5568d3;
        }
        
        .btn-back {
            display: inline-block;
            padding: 10px 20px;
            background: #6c757d;
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: background 0.2s;
        }
        
        .btn-back:hover {
            background: #5a6268;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">⚙️ SGVC Admin</div>
        <div class="navbar-menu">
            <a href="dashboard-admin.jsp">← Volver al Dashboard</a>
        </div>
    </nav>
    
    <div class="container">
        <div class="header">
            <h1>✅ Validar Organizaciones</h1>
            <p>Revisa y aprueba las solicitudes de registro de organizaciones</p>
        </div>
        
        <div class="content">
            <% if (mensaje != null) { %>
                <div class="alert alert-success">✓ <%= mensaje %></div>
            <% } %>
            
            <% if (error != null) { %>
                <div class="alert alert-error">❌ <%= error %></div>
            <% } %>
            
            <% if (organizaciones == null || organizaciones.isEmpty()) { %>
                <div class="empty-state">
                    <div class="icon">📭</div>
                    <h2>No hay organizaciones pendientes</h2>
                    <p>Todas las solicitudes han sido procesadas</p>
                    <br>
                    <a href="dashboard-admin.jsp" class="btn-back">Volver al Dashboard</a>
                </div>
            <% } else { %>
                <% for (Organizacion org : organizaciones) { %>
                    <div class="org-card">
                        <div class="org-header">
                            <div class="org-title">
                                <h3><%= org.getNombre() %></h3>
                                <p class="nit">NIT: <%= org.getNit() %></p>
                            </div>
                            <span class="badge badge-pending">⏳ Pendiente</span>
                        </div>
                        
                        <div class="org-details">
                            <div class="detail-item">
                                <span class="icon">👤</span>
                                <span><strong>Representante:</strong> <%= org.getRepresentante() %></span>
                            </div>
                            <div class="detail-item">
                                <span class="icon">📧</span>
                                <span><%= org.getUsuario() != null ? org.getUsuario().getEmail() : "N/A" %></span>
                            </div>
                            <div class="detail-item">
                                <span class="icon">📱</span>
                                <span><%= org.getTelefono() != null ? org.getTelefono() : "No especificado" %></span>
                            </div>
                            <div class="detail-item">
                                <span class="icon">🏢</span>
                                <span><strong>Tipo:</strong> <%= org.getTipo() != null ? org.getTipo() : "No especificado" %></span>
                            </div>
                            <div class="detail-item">
                                <span class="icon">📍</span>
                                <span><%= org.getDireccion() != null ? org.getDireccion() : "No especificada" %></span>
                            </div>
                        </div>
                        
                        <div class="org-actions">
                            <form action="OrganizacionControl" method="POST" style="display: inline;">
                                <input type="hidden" name="accion" value="validar">
                                <input type="hidden" name="id" value="<%= org.getIdentificador() %>">
                                <input type="hidden" name="decision" value="aprobar">
                                <button type="submit" class="btn btn-approve" 
                                        onclick="return confirm('¿Aprobar esta organización?')">
                                    ✓ Aprobar
                                </button>
                            </form>
                            
                            <form action="OrganizacionControl" method="POST" style="display: inline;">
                                <input type="hidden" name="accion" value="validar">
                                <input type="hidden" name="id" value="<%= org.getIdentificador() %>">
                                <input type="hidden" name="decision" value="rechazar">
                                <button type="submit" class="btn btn-reject" 
                                        onclick="return confirm('¿Rechazar esta organización?')">
                                    ✗ Rechazar
                                </button>
                            </form>
                        </div>
                    </div>
                <% } %>
            <% } %>
        </div>
    </div>
</body>
</html>