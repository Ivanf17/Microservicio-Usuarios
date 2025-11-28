<%-- 
    Document   : recuperar-password
    Created on : 20 nov 2025, 21:27:23
    Author     : flori
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Recuperar Contraseña - SGVC</title>
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
        
        .container {
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
            max-width: 500px;
            width: 100%;
        }
        
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .header .icon {
            font-size: 4rem;
            margin-bottom: 15px;
        }
        
        .header h1 {
            color: #333;
            font-size: 1.8rem;
            margin-bottom: 10px;
        }
        
        .header p {
            color: #666;
            font-size: 1rem;
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
        
        .token-display {
            background: #f8f9fa;
            border: 2px dashed #667eea;
            border-radius: 8px;
            padding: 15px;
            margin: 20px 0;
            text-align: center;
        }
        
        .token-display code {
            background: white;
            padding: 8px 15px;
            border-radius: 5px;
            font-family: 'Courier New', monospace;
            font-size: 0.9rem;
            color: #667eea;
            font-weight: 600;
        }
        
        .info-box {
            background: #e3f2fd;
            border-left: 4px solid #2196f3;
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            color: #1565c0;
            font-size: 0.95rem;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <div class="icon">🔑</div>
            <h1>Recuperar Contraseña</h1>
            <p>Te ayudaremos a recuperar el acceso a tu cuenta</p>
        </div>
        
        <% 
            String error = (String) request.getAttribute("error");
            String mensaje = (String) request.getAttribute("mensaje");
            String token = (String) request.getAttribute("token");
        %>
        
        <% if (error != null) { %>
            <div class="alert alert-error">❌ <%= error %></div>
        <% } %>
        
        <% if (mensaje != null) { %>
            <div class="alert alert-success">✓ <%= mensaje %></div>
            
            <% if (token != null) { %>
                <div class="info-box">
                    ℹ️ <strong>Modo de prueba:</strong> En producción, este token se enviaría por correo electrónico.
                </div>
                
                <div class="token-display">
                    <p style="margin-bottom: 10px; color: #666;">Token de recuperación:</p>
                    <code><%= token %></code>
                    <p style="margin-top: 10px; font-size: 0.85rem; color: #888;">
                        Copia este token para restablecer tu contraseña
                    </p>
                </div>
                
                <form action="AuthControl" method="POST">
                    <input type="hidden" name="accion" value="restablecerPassword">
                    
                    <div class="form-group">
                        <label for="token">Token de Recuperación</label>
                        <input type="text" id="token" name="token" 
                               value="<%= token %>" required readonly>
                    </div>
                    
                    <div class="form-group">
                        <label for="nuevaPassword">Nueva Contraseña</label>
                        <input type="password" id="nuevaPassword" name="nuevaPassword" 
                               required minlength="6" placeholder="Mínimo 6 caracteres">
                    </div>
                    
                    <div class="form-group">
                        <label for="confirmarPassword">Confirmar Contraseña</label>
                        <input type="password" id="confirmarPassword" name="confirmarPassword" 
                               required minlength="6" placeholder="Repite la contraseña">
                    </div>
                    
                    <button type="submit" class="btn">Restablecer Contraseña</button>
                </form>
            <% } %>
        <% } else { %>
            <form action="AuthControl" method="POST">
                <input type="hidden" name="accion" value="solicitarRecuperar">
                
                <div class="form-group">
                    <label for="email">Correo Electrónico</label>
                    <input type="email" id="email" name="email" 
                           required placeholder="correo@ejemplo.com" autofocus>
                    <small style="color: #888; display: block; margin-top: 5px;">
                        Te enviaremos instrucciones para recuperar tu contraseña
                    </small>
                </div>
                
                <button type="submit" class="btn">Enviar Instrucciones</button>
            </form>
        <% } %>
        
        <div class="links">
            <a href="login.jsp">← Volver al inicio de sesión</a>
        </div>
    </div>
    
    <script>
        // Validar que las contraseñas coincidan
        const form = document.querySelector('form[action*="restablecerPassword"]');
        if (form) {
            form.addEventListener('submit', function(e) {
                const nuevaPassword = document.getElementById('nuevaPassword').value;
                const confirmarPassword = document.getElementById('confirmarPassword').value;
                
                if (nuevaPassword !== confirmarPassword) {
                    e.preventDefault();
                    alert('Las contraseñas no coinciden');
                }
            });
        }
    </script>
</body>
</html>