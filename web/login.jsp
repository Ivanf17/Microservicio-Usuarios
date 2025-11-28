<%-- 
    Document   : Login
    Created on : 20 nov 2025, 16:44:30
    Author     : flori
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Iniciar Sesión - SGVC</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 20px;
        }
        
        .login-container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            overflow: hidden;
            width: 100%;
            max-width: 900px;
            display: flex;
        }
        
        .login-left {
            flex: 1;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 60px 40px;
            color: white;
            display: flex;
            flex-direction: column;
            justify-content: center;
        }
        
        .login-left h1 {
            font-size: 2.5rem;
            margin-bottom: 20px;
        }
        
        .login-left p {
            font-size: 1.1rem;
            line-height: 1.6;
            margin-bottom: 30px;
            opacity: 0.9;
        }
        
        .login-right {
            flex: 1;
            padding: 60px 40px;
        }
        
        .login-right h2 {
            color: #333;
            margin-bottom: 10px;
            font-size: 2rem;
        }
        
        .login-right .subtitle {
            color: #666;
            margin-bottom: 30px;
        }
        
        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-size: 0.95rem;
        }
        
        .alert-error {
            background: #fee;
            color: #c33;
            border: 1px solid #fcc;
        }
        
        .alert-success {
            background: #efe;
            color: #3c3;
            border: 1px solid #cfc;
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
        }
        
        .form-group input {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1rem;
            transition: all 0.3s;
        }
        
        .form-group input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .btn {
            width: 100%;
            padding: 14px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 8px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        
        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }
        
        .btn:active {
            transform: translateY(0);
        }
        
        .links {
            margin-top: 25px;
            text-align: center;
        }
        
        .links a {
            color: #667eea;
            text-decoration: none;
            font-weight: 500;
            transition: color 0.2s;
        }
        
        .links a:hover {
            color: #764ba2;
            text-decoration: underline;
        }
        
        .register-section {
            margin-top: 30px;
            padding-top: 30px;
            border-top: 1px solid #e0e0e0;
            text-align: center;
        }
        
        .register-section p {
            margin-bottom: 15px;
            color: #666;
        }
        
        .register-buttons {
            display: flex;
            gap: 10px;
        }
        
        .btn-secondary {
            flex: 1;
            padding: 12px;
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
            border-radius: 8px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s;
        }
        
        .btn-secondary:hover {
            background: #667eea;
            color: white;
        }
        
        @media (max-width: 768px) {
            .login-container {
                flex-direction: column;
            }
            
            .login-left {
                padding: 40px 30px;
            }
            
            .login-left h1 {
                font-size: 2rem;
            }
            
            .login-right {
                padding: 40px 30px;
            }
        }
    </style>
</head>
<body>
    <div class="login-container">
        <div class="login-left">
            <h1>🤝 SGVC</h1>
            <p>Sistema de Gestión de Voluntariado Comunitario</p>
            <p>Conectamos voluntarios con organizaciones para crear un impacto positivo en nuestra comunidad.</p>
        </div>
        
        <div class="login-right">
            <h2>Iniciar Sesión</h2>
            <p class="subtitle">¡Bienvenido de nuevo!</p>
            
            <% 
                String error = (String) request.getAttribute("error");
                String mensaje = (String) request.getAttribute("mensaje");
                String username = (String) request.getAttribute("username");
                if (username == null) username = "";
            %>
            
            <% if (error != null) { %>
                <div class="alert alert-error">❌ <%= error %></div>
            <% } %>
            
            <% if (mensaje != null) { %>
                <div class="alert alert-success">✓ <%= mensaje %></div>
            <% } %>
            
            <form action="AuthControl" method="POST">
                <input type="hidden" name="accion" value="login">
                
                <div class="form-group">
                    <label for="username">Usuario</label>
                    <input type="text" id="username" name="username" 
                           value="<%= username %>" required autofocus 
                           placeholder="Ingresa tu usuario">
                </div>
                
                <div class="form-group">
                    <label for="password">Contraseña</label>
                    <input type="password" id="password" name="password" 
                           required placeholder="Ingresa tu contraseña">
                </div>
                
                <button type="submit" class="btn">Iniciar Sesión</button>
            </form>
            
            <div class="links">
                <a href="AuthControl?accion=mostrarRecuperar">¿Olvidaste tu contraseña?</a>
            </div>
            
            <div class="register-section">
                <p>¿No tienes cuenta? ¡Regístrate!</p>
                <div class="register-buttons">
                    <button class="btn-secondary" onclick="location.href='VoluntarioControl?accion=mostrarRegistro'">
                        👤 Soy Voluntario
                    </button>
                    <button class="btn-secondary" onclick="location.href='OrganizacionControl?accion=mostrarRegistro'">
                        🏢 Soy Organización
                    </button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
