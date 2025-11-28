/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

/**
 *
 * @author flori
 */

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import Config.MongoDBConnection;
import Modelo.Usuario;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Implementación MongoDB del DAO de Usuario
 */
public class UsuarioMongoDAO implements IUsuarioDAO {
    
    private MongoCollection<Document> collection;
    
    public UsuarioMongoDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection("usuarios");
    }
    
    @Override
    public String crear(Usuario usuario) throws Exception {
        Document doc = new Document()
            .append("username", usuario.getUsername())
            .append("email", usuario.getEmail())
            .append("password", usuario.getPassword())
            .append("rol", usuario.getRol())
            .append("estado", usuario.getEstado())
            .append("fecha_creacion", new Date());
        
        collection.insertOne(doc);
        
        String idGenerado = doc.getObjectId("_id").toString();
        usuario.setId(idGenerado);
        
        System.out.println("✓ [MongoDB] Usuario creado con ID: " + idGenerado);
        return idGenerado;
    }
    
    @Override
    public Usuario buscarPorId(String id) throws Exception {
        Document doc = collection.find(Filters.eq("_id", new ObjectId(id))).first();
        return doc != null ? mapearUsuario(doc) : null;
    }
    
    @Override
    public Usuario buscarPorUsername(String username) throws Exception {
        Document doc = collection.find(Filters.eq("username", username)).first();
        return doc != null ? mapearUsuario(doc) : null;
    }
    
    @Override
    public Usuario buscarPorEmail(String email) throws Exception {
        Document doc = collection.find(Filters.eq("email", email)).first();
        return doc != null ? mapearUsuario(doc) : null;
    }
    
    @Override
    public Usuario buscarPorToken(String token) throws Exception {
        Document doc = collection.find(Filters.eq("token_recuperacion", token)).first();
        return doc != null ? mapearUsuario(doc) : null;
    }
    
    @Override
    public List<Usuario> listarTodos() throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        for (Document doc : collection.find()) {
            usuarios.add(mapearUsuario(doc));
        }
        return usuarios;
    }
    
    @Override
    public List<Usuario> listarPorRol(String rol) throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("rol", rol))) {
            usuarios.add(mapearUsuario(doc));
        }
        return usuarios;
    }
    
    @Override
    public List<Usuario> listarPorEstado(String estado) throws Exception {
        List<Usuario> usuarios = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("estado", estado))) {
            usuarios.add(mapearUsuario(doc));
        }
        return usuarios;
    }
    
    @Override
    public void actualizar(Usuario usuario) throws Exception {
        collection.updateOne(
            Filters.eq("_id", new ObjectId(usuario.getId())),
            Updates.combine(
                Updates.set("username", usuario.getUsername()),
                Updates.set("email", usuario.getEmail()),
                Updates.set("password", usuario.getPassword()),
                Updates.set("rol", usuario.getRol()),
                Updates.set("estado", usuario.getEstado())
            )
        );
        System.out.println("✓ [MongoDB] Usuario actualizado: " + usuario.getUsername());
    }
    
    @Override
    public void actualizarPassword(String id, String nuevaPassword) throws Exception {
        collection.updateOne(
            Filters.eq("_id", new ObjectId(id)),
            Updates.set("password", nuevaPassword)
        );
    }
    
    @Override
    public void actualizarUltimaConexion(String id) throws Exception {
        collection.updateOne(
            Filters.eq("_id", new ObjectId(id)),
            Updates.set("ultima_conexion", new Date())
        );
    }
    
    @Override
    public void cambiarEstado(String id, String nuevoEstado) throws Exception {
        collection.updateOne(
            Filters.eq("_id", new ObjectId(id)),
            Updates.set("estado", nuevoEstado)
        );
    }
    
    @Override
    public void guardarTokenRecuperacion(String id, String token, LocalDateTime expiracion) throws Exception {
        Date fechaExp = Date.from(expiracion.atZone(ZoneId.systemDefault()).toInstant());
        collection.updateOne(
            Filters.eq("_id", new ObjectId(id)),
            Updates.combine(
                Updates.set("token_recuperacion", token),
                Updates.set("token_expiracion", fechaExp)
            )
        );
    }
    
    @Override
    public void limpiarToken(String id) throws Exception {
        collection.updateOne(
            Filters.eq("_id", new ObjectId(id)),
            Updates.combine(
                Updates.unset("token_recuperacion"),
                Updates.unset("token_expiracion")
            )
        );
    }
    
    @Override
    public void eliminar(String id) throws Exception {
        collection.deleteOne(Filters.eq("_id", new ObjectId(id)));
        System.out.println("✓ [MongoDB] Usuario eliminado");
    }
    
    @Override
    public boolean existeUsername(String username) throws Exception {
        return collection.countDocuments(Filters.eq("username", username)) > 0;
    }
    
    @Override
    public boolean existeEmail(String email) throws Exception {
        return collection.countDocuments(Filters.eq("email", email)) > 0;
    }
    
    @Override
    public int contar() throws Exception {
        return (int) collection.countDocuments();
    }
    
    @Override
    public int contarPorRol(String rol) throws Exception {
        return (int) collection.countDocuments(Filters.eq("rol", rol));
    }
    
    private Usuario mapearUsuario(Document doc) {
        Usuario usuario = new Usuario();
        usuario.setId(doc.getObjectId("_id").toString());
        usuario.setUsername(doc.getString("username"));
        usuario.setEmail(doc.getString("email"));
        usuario.setPassword(doc.getString("password"));
        usuario.setRol(doc.getString("rol"));
        usuario.setEstado(doc.getString("estado"));
        
        Date fechaCreacion = doc.getDate("fecha_creacion");
        if (fechaCreacion != null) {
            usuario.setFechaCreacion(LocalDateTime.ofInstant(
                fechaCreacion.toInstant(), ZoneId.systemDefault()));
        }
        
        return usuario;
    }
}
