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
import Config.MongoDBConnection;
import Modelo.Organizacion;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación MongoDB del DAO de Organización
 */
public class OrganizacionMongoDAO implements IOrganizacionDAO {
    
    private MongoCollection<Document> collection;
    
    public OrganizacionMongoDAO() {
        MongoDatabase database = MongoDBConnection.getInstance().getDatabase();
        this.collection = database.getCollection("organizaciones");
    }
    
    @Override
    public String crear(Organizacion organizacion) throws Exception {
        Document doc = new Document()
            .append("id_usuario", organizacion.getId())
            .append("nombre", organizacion.getNombre())
            .append("nit", organizacion.getNit())
            .append("direccion", organizacion.getDireccion())
            .append("telefono", organizacion.getTelefono())
            .append("tipo", organizacion.getTipo())
            .append("representante", organizacion.getRepresentante())
            .append("estado_validacion", organizacion.getEstadoValidacion());
        
        collection.insertOne(doc);
        return doc.getObjectId("_id").toString();
    }
    
    @Override
    public Organizacion buscarPorIdUsuario(String idUsuario) throws Exception {
        Document doc = collection.find(Filters.eq("id_usuario", idUsuario)).first();
        return doc != null ? mapearOrganizacion(doc) : null;
    }
    
    @Override
    public Organizacion buscarPorNIT(String nit) throws Exception {
        Document doc = collection.find(Filters.eq("nit", nit)).first();
        return doc != null ? mapearOrganizacion(doc) : null;
    }
    
    @Override
    public List<Organizacion> listarTodas() throws Exception {
        List<Organizacion> organizaciones = new ArrayList<>();
        for (Document doc : collection.find()) {
            organizaciones.add(mapearOrganizacion(doc));
        }
        return organizaciones;
    }
    
    @Override
    public List<Organizacion> listarPorEstadoValidacion(String estadoValidacion) throws Exception {
        List<Organizacion> organizaciones = new ArrayList<>();
        for (Document doc : collection.find(Filters.eq("estado_validacion", estadoValidacion))) {
            organizaciones.add(mapearOrganizacion(doc));
        }
        return organizaciones;
    }
    
    @Override
    public List<Organizacion> buscarPorNombre(String termino) throws Exception {
        List<Organizacion> organizaciones = new ArrayList<>();
        for (Document doc : collection.find(Filters.regex("nombre", termino, "i"))) {
            organizaciones.add(mapearOrganizacion(doc));
        }
        return organizaciones;
    }
    
    @Override
    public void actualizar(Organizacion organizacion) throws Exception {
        collection.updateOne(
            Filters.eq("id_usuario", organizacion.getId()),
            new Document("$set", new Document()
                .append("nombre", organizacion.getNombre())
                .append("direccion", organizacion.getDireccion())
                .append("telefono", organizacion.getTelefono())
                .append("tipo", organizacion.getTipo())
                .append("representante", organizacion.getRepresentante()))
        );
    }
    
    @Override
    public void cambiarEstadoValidacion(String idUsuario, String nuevoEstado) throws Exception {
        collection.updateOne(
            Filters.eq("id_usuario", idUsuario),
            new Document("$set", new Document("estado_validacion", nuevoEstado))
        );
    }
    
    @Override
    public void eliminar(String idUsuario) throws Exception {
        collection.deleteOne(Filters.eq("id_usuario", idUsuario));
    }
    
    @Override
    public boolean existeNIT(String nit) throws Exception {
        return collection.countDocuments(Filters.eq("nit", nit)) > 0;
    }
    
    @Override
    public boolean existe(String idUsuario) throws Exception {
        return collection.countDocuments(Filters.eq("id_usuario", idUsuario)) > 0;
    }
    
    @Override
    public int contar() throws Exception {
        return (int) collection.countDocuments();
    }
    
    @Override
    public int contarPorEstadoValidacion(String estadoValidacion) throws Exception {
        return (int) collection.countDocuments(Filters.eq("estado_validacion", estadoValidacion));
    }
    
    private Organizacion mapearOrganizacion(Document doc) {
        return new Organizacion(
            (Integer) null,
            doc.getString("nombre"),
            doc.getString("direccion"),
            doc.getString("telefono"),
            doc.getString("tipo"),
            doc.getString("nit"),
            doc.getString("representante"),
            doc.getString("estado_validacion")
        );
    }
}