<%-- 
    Document   : dashboard-voluntario
    Created on : 20 nov 2025, 21:24:31
    Author     : flori
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Modelo.Usuario" %>
<%
    Usuario usuario = (Usuario) session.getAttribute("usuario");
    if (usuario == null || !"Voluntario".equals(usuario.getRol())) {
        response.sendRedirect("login.jsp");
        return;
    }
    String username = usuario.getUsername();
    String email = usuario.getEmail();
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard Voluntario - SGVC</title>
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
            color: #667eea;
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
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
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
        
        .stat-icon.blue {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        .stat-icon.green {
            background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
        }
        
        .stat-icon.orange {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        .stat-icon.purple {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
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
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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
    </style>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-brand">🤝 SGVC</div>
        <div class="navbar-menu">
            <a href="dashboard-voluntario.jsp">Inicio</a>
            <a href="VoluntarioControl?accion=miPerfil">Mi Perfil</a>
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
            <h1>¡Bienvenido, <%= username %>! 👋</h1>
            <p>Gestiona tus actividades de voluntariado desde aquí</p>
        </div>
        
        <div class="stats">
            <div class="stat-card">
                <div class="stat-icon blue">📋</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Proyectos Activos</p>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon green">✓</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Proyectos Completados</p>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon orange">⏱️</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Horas Voluntariado</p>
                </div>
            </div>
            
            <div class="stat-card">
                <div class="stat-icon purple">🏆</div>
                <div class="stat-info">
                    <h3>0</h3>
                    <p>Certificados</p>
                </div>
            </div>
        </div>
        
        <div class="actions">
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">🔍</div>
                <h3>Explorar Proyectos</h3>
                <p>Busca proyectos que te interesen y postúlate</p>
                <a href="#" class="btn">Explorar</a>
            </div>
            
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">📝</div>
                <h3>Mis Postulaciones</h3>
                <p>Revisa el estado de tus postulaciones</p>
                <a href="#" class="btn">Ver Postulaciones</a>
            </div>
            
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">📊</div>
                <h3>Mi Historial</h3>
                <p>Consulta tu historial de participación</p>
                <a href="#" class="btn">Ver Historial</a>
            </div>
            
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">🎓</div>
                <h3>Mis Certificados</h3>
                <p>Descarga tus certificados de participación</p>
                <a href="#" class="btn">Ver Certificados</a>
            </div>
            
            <div class="action-card" onclick="location.href='VoluntarioControl?accion=miPerfil'">
                <div class="icon">👤</div>
                <h3>Mi Perfil</h3>
                <p>Actualiza tu información personal</p>
                <a href="VoluntarioControl?accion=miPerfil" class="btn">Ver Perfil</a>
            </div>
            
            <div class="action-card" onclick="location.href='#'">
                <div class="icon">📢</div>
                <h3>Denunciar</h3>
                <p>Reporta comportamientos inapropiados</p>
                <a href="#" class="btn">Hacer Denuncia</a>
            </div>
        </div>
    </div>
</body>
</html>
