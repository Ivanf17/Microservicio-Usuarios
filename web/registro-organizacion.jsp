<%-- 
    Document   : registro-organizacion
    Created on : 20 nov 2025, 21:20:37
    Author     : flori
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registro Organización - SGVC</title>
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
            padding: 40px 20px;
        }
        
        .container {
            max-width: 700px;
            margin: 0 auto;
            background: white;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            padding: 40px;
        }
        
        .header {
            text-align: center;
            margin-bottom: 30px;
        }
        
        .header h1 {
            color: #333;
            font-size: 2rem;
            margin-bottom: 10px;
        }
        
        .header p {
            color: #666;
            font-size: 1.1rem;
        }
        
        .alert {
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 25px;
            font-size: 0.95rem;
        }
        
        .alert-error {
            background: #fee;
            color: #c33;
            border: 1px solid #fcc;
        }
        
        .alert-info {
            background: #e3f2fd;
            color: #1976d2;
            border: 1px solid #90caf9;
        }
        
        .section-title {
            color: #667eea;
            font-size: 1.2rem;
            font-weight: 600;
            margin: 25px 0 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #e0e0e0;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 500;
        }
        
        .form-group label .required {
            color: #e53e3e;
        }
        
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 8px;
            font-size: 1rem;
            transition: all 0.3s;
        }
        
        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
        }
        
        .form-hint {
            font-size: 0.85rem;
            color: #888;
            margin-top: 5px;
        }
        
        .btn-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        
        .btn {
            flex: 1;
            padding: 14px;
            border: none;
            border-radius: 8px;
            font-size: 1.1rem;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.2s;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.3);
        }
        
        .btn-secondary {
            background: white;
            color: #667eea;
            border: 2px solid #667eea;
        }
        
        .btn-secondary:hover {
            background: #f5f5f5;
        }
        
        .login-link {
            text-align: center;
            margin-top: 20px;
            color: #666;
        }
        
        .login-link a {
            color: #667eea;
            text-decoration: none;
            font-weight: 600;
        }
        
        .login-link a:hover {
            text-decoration: underline;
        }
        
        @media (max-width: 768px) {
            .form-row {
                grid-template-columns: 1fr;
            }
            
            .btn-group {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🏢 Registro de Organización</h1>
            <p>Publica proyectos y gestiona voluntarios</p>
        </div>
        
        <div class="alert alert-info">
            ℹ️ Tu solicitud será revisada por un administrador antes de poder publicar proyectos.
        </div>
        
        <% 
            String error = (String) request.getAttribute("error");
            String mensaje = (String) request.getAttribute("mensaje");
        %>
        
        <% if (error != null) { %>
            <div class="alert alert-error">❌ <%= error %></div>
        <% } %>
        
        <form action="OrganizacionControl" method="POST">
            <input type="hidden" name="accion" value="registrar">
            
            <div class="section-title">📋 Información de la Cuenta</div>
            
            <div class="form-group">
                <label for="username">Usuario <span class="required">*</span></label>
                <input type="text" id="username" name="username" 
                       value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : "" %>"
                       required pattern="[a-zA-Z0-9_]{3,20}"
                       placeholder="ej: fundacion123">
                <div class="form-hint">3-20 caracteres (letras, números y guión bajo)</div>
            </div>
            
            <div class="form-group">
                <label for="email">Email Institucional <span class="required">*</span></label>
                <input type="email" id="email" name="email"
                       value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : "" %>"
                       required placeholder="contacto@organizacion.com">
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="password">Contraseña <span class="required">*</span></label>
                    <input type="password" id="password" name="password" 
                           required minlength="6" placeholder="Mínimo 6 caracteres">
                </div>
                
                <div class="form-group">
                    <label for="confirmarPassword">Confirmar Contraseña <span class="required">*</span></label>
                    <input type="password" id="confirmarPassword" name="confirmarPassword" 
                           required minlength="6" placeholder="Repite la contraseña">
                </div>
            </div>
            
            <div class="section-title">🏢 Información Institucional</div>
            
            <div class="form-group">
                <label for="nombre">Nombre de la Organización <span class="required">*</span></label>
                <input type="text" id="nombre" name="nombre"
                       value="<%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : "" %>"
                       required placeholder="Fundación XYZ">
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="nit">NIT <span class="required">*</span></label>
                    <input type="text" id="nit" name="nit"
                           value="<%= request.getAttribute("nit") != null ? request.getAttribute("nit") : "" %>"
                           required pattern="[0-9]{9,15}" placeholder="900123456">
                    <div class="form-hint">9-15 dígitos</div>
                </div>
                
                <div class="form-group">
                    <label for="tipo">Tipo de Organización</label>
                    <select id="tipo" name="tipo">
                        <option value="ONG">ONG</option>
                        <option value="Fundación">Fundación</option>
                        <option value="Asociación">Asociación</option>
                        <option value="Corporación">Corporación</option>
                        <option value="Otro">Otro</option>
                    </select>
                </div>
            </div>
            
            <div class="form-group">
                <label for="representante">Representante Legal <span class="required">*</span></label>
                <input type="text" id="representante" name="representante"
                       value="<%= request.getAttribute("representante") != null ? request.getAttribute("representante") : "" %>"
                       required placeholder="Nombre del representante">
            </div>
            
            <div class="form-group">
                <label for="direccion">Dirección</label>
                <input type="text" id="direccion" name="direccion"
                       value="<%= request.getAttribute("direccion") != null ? request.getAttribute("direccion") : "" %>"
                       placeholder="Calle 123 #45-67">
            </div>
            
            <div class="form-group">
                <label for="telefono">Teléfono de Contacto</label>
                <input type="tel" id="telefono" name="telefono"
                       value="<%= request.getAttribute("telefono") != null ? request.getAttribute("telefono") : "" %>"
                       pattern="[0-9]{7,15}" placeholder="6012345678">
                <div class="form-hint">7-15 dígitos</div>
            </div>
            
            <div class="btn-group">
                <button type="submit" class="btn btn-primary">✓ Enviar Solicitud</button>
                <button type="button" class="btn btn-secondary" onclick="location.href='login.jsp'">
                    Cancelar
                </button>
            </div>
        </form>
        
        <div class="login-link">
            ¿Ya tienes cuenta? <a href="login.jsp">Inicia sesión aquí</a>
        </div>
    </div>
    
    <script>
        // Validar que las contraseñas coincidan
        document.querySelector('form').addEventListener('submit', function(e) {
            const password = document.getElementById('password').value;
            const confirmarPassword = document.getElementById('confirmarPassword').value;
            
            if (password !== confirmarPassword) {
                e.preventDefault();
                alert('Las contraseñas no coinciden');
            }
        });
    </script>
</body>
</html>
