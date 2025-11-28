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
import Modelo.Voluntario;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class VoluntarioMongoDAO implements IVoluntarioDAO {
    
    private MongoCollection<Document> collection;
    
    public VoluntarioMongoDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection("voluntarios");
    }
    
    @Override
    public String crear(Voluntario voluntario) throws Exception {
        Document doc = new Document()
            .append("id_usuario", voluntario.getId())
            .append("nombre", voluntario.getNombre())
            .append("apellido", voluntario.getApellido())
            .append("direccion", voluntario.getDireccion())
            .append("telefono", voluntario.getTelefono());
        
        if (voluntario.getFechaNacimiento() != null) {
            Date fecha = Date.from(voluntario.getFechaNacimiento()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
            doc.append("fecha_nacimiento", fecha);
        }
        
        collection.insertOne(doc);
        
        String idGenerado = doc.getObjectId("_id").toString();
        System.out.println("✓ [MongoDB] Voluntario creado");
        return idGenerado;
    }
    
    @Override
    public Voluntario buscarPorIdUsuario(String idUsuario) throws Exception {
        Document doc = collection.find(Filters.eq("id_usuario", idUsuario)).first();
        return doc != null ? mapearVoluntario(doc) : null;
    }
    
    @Override
    public List<Voluntario> listarTodos() throws Exception {
        List<Voluntario> voluntarios = new ArrayList<>();
        for (Document doc : collection.find()) {
            voluntarios.add(mapearVoluntario(doc));
        }
        return voluntarios;
    }
    
    @Override
    public List<Voluntario> buscarPorNombre(String termino) throws Exception {
        List<Voluntario> voluntarios = new ArrayList<>();
        for (Document doc : collection.find(Filters.regex("nombre", termino, "i"))) {
            voluntarios.add(mapearVoluntario(doc));
        }
        return voluntarios;
    }
    
    @Override
    public void actualizar(Voluntario voluntario) throws Exception {
        Document update = new Document()
            .append("nombre", voluntario.getNombre())
            .append("apellido", voluntario.getApellido())
            .append("direccion", voluntario.getDireccion())
            .append("telefono", voluntario.getTelefono());
        
        if (voluntario.getFechaNacimiento() != null) {
            Date fecha = Date.from(voluntario.getFechaNacimiento()
                .atStartOfDay(ZoneId.systemDefault()).toInstant());
            update.append("fecha_nacimiento", fecha);
        }
        
        collection.updateOne(
            Filters.eq("id_usuario", voluntario.getId()),
            new Document("$set", update)
        );
    }
    
    @Override
    public void eliminar(String idUsuario) throws Exception {
        collection.deleteOne(Filters.eq("id_usuario", idUsuario));
    }
    
    @Override
    public boolean existe(String idUsuario) throws Exception {
        return collection.countDocuments(Filters.eq("id_usuario", idUsuario)) > 0;
    }
    
    @Override
    public int contar() throws Exception {
        return (int) collection.countDocuments();
    }
    
    private Voluntario mapearVoluntario(Document doc) {
        String idUsuario = doc.getString("id_usuario");
        String nombre = doc.getString("nombre");
        String apellido = doc.getString("apellido");
        String direccion = doc.getString("direccion");
        String telefono = doc.getString("telefono");
        
        LocalDate fechaNacimiento = null;
        Date fecha = doc.getDate("fecha_nacimiento");
        if (fecha != null) {
            fechaNacimiento = fecha.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        }
        
        return new Voluntario((Integer) null, nombre, apellido, fechaNacimiento, direccion, telefono);
    }
}
