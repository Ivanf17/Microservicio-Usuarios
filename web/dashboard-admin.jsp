<%-- 
    Document   : dashboard-admin
    Created on : 20 nov 2025, 21:25:57
    Author     : flori
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null || !"Administrador".equals(usuario.getRol())) {
        response.sendRedirect("login.jsp");
        return;
    }
    String username = usuario.getUsername();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Administrador - SGVC</title>
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
        
        .navbar-menu {
            display: flex;
            gap: 20px;
            align-items: center;
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
        
        .user-info {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        
        .user-avatar {
            width: 40px;
            height: 40px;
            background: white;
            color: #f5576c;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 700;
            font-size: 1.2rem;
        }
        
        .container {
            max-width: 1200px;
            margin: 40px auto;
            padding: 0 20px;
        }
        
        .welcome {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 30px;
        }
        
        .welcome h1 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .welcome p {
            color: #666;
            font-size: 1.1rem;
        }
        
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: white;
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            display: flex;
            align-items: center;
            gap: 20px;
        }
        
        .stat-icon {
            width: 60px;
            height: 60px;
            border-radius: 12px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 2rem;
        }
        
        .stat-icon.red {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        .stat-icon.blue {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .stat-icon.green {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        }
        
        .stat-icon.orange {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
        }
        
        .stat-info h3 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 5px;
        }
        
        .stat-info p {
            color: #666;
            font-size: 0.95rem;
        }
        
        .actions {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 20px;
        }
        
        .action-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            text-align: center;
            transition: transform 0.3s, box-shadow 0.3s;
            cursor: pointer;
        }
        
        .action-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0,0,0,0.15);
        }
        
        .action-card .icon {
            font-size: 4rem;
            margin-bottom: 15px;
        }
        
        .action-card h3 {
            color: #333;
            margin-bottom: 10px;
            font-size: 1.3rem;
        }
        
        .action-card p {
            color: #666;
            margin-bottom: 20px;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
            text-decoration: none;
            border-radius: 8px;
            font-weight: 600;
            transition: transform 0.2s;
        }
        
        .btn:hover {
            transform: translateY(-2px);
        }
        
        .btn-logout {
            background: #e53e3e;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
        }
        
        .btn-logout:hover {
            background: #c53030;
        }
        
        .badge {
            display: inline-block;
            padding: 4px 12px;
            background: #e53e3e;
            color: white;
            border-radius: 12px;
            font-size: 0.8rem;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">⚙️ SGVC Admin</div>
        <div class="navbar-menu">
            <a href="dashboard-admin.jsp">Inicio</a>
            <a href="UsuarioControl?accion=listar">Usuarios</a>
            <a href="OrganizacionControl?accion=listarPendientes">Validaciones <span class="badge">0</span></a>
            <div class="user-info">
                <div class="user-avatar"><%= username.substring(0, 1).toUpperCase() %></div>
                <span><%= username %></span>
                <form action="AuthControl" method="POST" style="display: inline;">
                    <input type="hidden" name="accion" value="logout">
                    <button type="submit" class="btn-logout">Salir</button>
                </form>
            </div>
        </div>
    </nav>
    
    <div class="container">
        <div class="welcome">
            <h1>Panel de Administración ⚙️</h1>
            <p>Gestiona usuarios, organizaciones y supervisa el sistema</p>
        </div>
        
        <div class="stats">
            <div class="stat-card">
                <div class="stat-icon blue">👥</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Total Usuarios</p>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon green">🤝</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Voluntarios</p>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon orange">🏢</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Organizaciones</p>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon red">⏳</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Pendientes Validación</p>
                </div>
            </div>
        </div>
        
        <div class="actions">
            <div class="action-card" onclick="location.href='UsuarioControl?accion=listar'">
                <div class="icon">👥</div>
                <h3>Gestionar Usuarios</h3>
                <p>Ver, editar y administrar todos los usuarios</p>
                <a href="UsuarioControl?accion=listar" class="btn">Gestionar</a>
            </div>
            
            <div class="action-card" onclick="location.href='OrganizacionControl?accion=listarPendientes'">
                <div class="icon">✅</div>
                <h3>Validar Organizaciones</h3>
                <p>Aprobar o rechazar organizaciones registradas</p>
                <a href="OrganizacionControl?accion=listarPendientes" class="btn">Validar</a>
            </div>
            
            <div class="action-card" onclick="location.href='VoluntarioControl?accion=listar'">
                <div class="icon">🤝</div>
                <h3>Ver Voluntarios</h3>
                <p>Consulta la lista de voluntarios registrados</p>
                <a href="VoluntarioControl?accion=listar" class="btn">Ver Lista</a>
            </div>
            
            <div class="action-card" onclick="location.href='OrganizacionControl?accion=listar'">
                <div class="icon">🏢</div>
                <h3>Ver Organizaciones</h3>
                <p>Consulta todas las organizaciones</p>
                <a href="OrganizacionControl?accion=listar" class="btn">Ver Lista</a>
            </div>
            
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">📊</div>
                <h3>Reportes</h3>
                <p>Genera reportes consolidados del sistema</p>
                <a href="#" class="btn">Ver Reportes</a>
            </div>
            
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">🚨</div>
                <h3>Gestionar Denuncias</h3>
                <p>Revisa y gestiona las denuncias recibidas</p>
                <a href="#" class="btn">Gestionar</a>
            </div>
            
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">🎓</div>
                <h3>Generar Certificados</h3>
                <p>Emite certificados para voluntarios</p>
                <a href="#" class="btn">Generar</a>
            </div>
            
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">⚙️</div>
                <h3>Configuración</h3>
                <p>Ajustes y configuración del sistema</p>
                <a href="#" class="btn">Configurar</a>
            </div>
        </div>
    </div>
</body>
</html>
