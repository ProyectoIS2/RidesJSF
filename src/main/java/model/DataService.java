package model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import domain.Driver;

@Named
@ApplicationScoped
public class DataService implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Map<String, Driver> usuariosDB = new HashMap<>();

    public DataService() {
        usuariosDB.put("test@test.com", new Driver("test@test.com", "Tester", "123"));
    }

    public void guardarUsuario(Driver d) {
        usuariosDB.put(d.getEmail(), d);
    }

    public Driver buscarUsuario(String email) {
        return usuariosDB.get(email);
    }
    
    public boolean existeUsuario(String email) {
        return usuariosDB.containsKey(email);
    }

    public java.util.Collection<Driver> getAllDrivers() {
        return usuariosDB.values();
    }

    // Simula la consulta SELECT DISTINCT from FROM Ride
    public java.util.List<String> getDepartCities() {
        java.util.List<String> cities = new java.util.ArrayList<>();
        
        // Recorremos todos los conductores almacenados
        for (Driver d : usuariosDB.values()) {
            // Recorremos sus viajes
            for (domain.Ride r : d.getRides()) {
                String origen = r.getFrom();
                // Si la ciudad no está ya en la lista, la añadimos
                if (origen != null && !cities.contains(origen)) {
                    cities.add(origen);
                }
            }
        }
        return cities;
    }

    // Simula la consulta SELECT DISTINCT to FROM Ride WHERE from = ?
    public java.util.List<String> getDestinationCities(String from) {
        java.util.List<String> cities = new java.util.ArrayList<>();
        
        if (from == null) return cities;

        // Recorremos conductores y sus viajes
        for (Driver d : usuariosDB.values()) {
            for (domain.Ride r : d.getRides()) {
                // Si el viaje sale de donde nosotros queremos...
                if (r.getFrom().equals(from)) {
                    String destino = r.getTo();
                    // ... y el destino no está en la lista, lo añadimos
                    if (destino != null && !cities.contains(destino)) {
                        cities.add(destino);
                    }
                }
            }
        }
        return cities;
    }

    // Simula: SELECT * FROM Ride WHERE ...
    public java.util.List<domain.Ride> getRides(String from, String to, java.util.Date date) {
        java.util.List<domain.Ride> res = new java.util.ArrayList<>();
        
       
        // Formateador para comparar solo el día (ignorando horas)
        java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("dd/MM/yyyy");
        String fechaBuscadaStr = (date != null) ? fmt.format(date) : "NULA";
        // Recorrer todos los conductores
        for (Driver d : usuariosDB.values()) {
            
            // Verificar si tiene viajes
            java.util.List<domain.Ride> viajesConductor = d.getRides();
            if (viajesConductor == null || viajesConductor.isEmpty()) {
                continue;
            }            
            // Recorremos sus viajes
            for (domain.Ride r : viajesConductor) {
                String fechaViajeStr = fmt.format(r.getDate());
                // Comprobamos Origen
                if (!r.getFrom().equals(from)) continue;
                
                // Comprobamos Destino
                if (!r.getTo().equals(to)) continue;
                
                // Comprobamos Fecha
                if (!fechaViajeStr.equals(fechaBuscadaStr)) continue;
                
                // SI LLEGA AQUÍ, ES QUE COINCIDE
                res.add(r);
            }
        }
        return res;
    }
}