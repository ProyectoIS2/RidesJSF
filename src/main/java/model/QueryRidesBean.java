package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.primefaces.event.SelectEvent;

import domain.Ride;
import model.DataService;

@Named("queryRides")
@ViewScoped // Mantiene los datos mientras no cambies de página (necesario para AJAX)
public class QueryRidesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String departCity;
    private List<String> departCities;
    
    private String destinationCity;
    private List<String> destinationCities = new ArrayList<>();
    
    private Date rideDate;
    private List<Ride> foundRides = new ArrayList<>();

    @Inject
    private DataService dataService;

    // --- Inicialización ---
    @PostConstruct
    public void init() {
        // Al cargar la página, se cogen las ciudades de origen disponibles
        this.departCities = dataService.getDepartCities();
        if (this.departCities != null && !this.departCities.isEmpty()) {
            this.departCity = this.departCities.get(0);            
            this.destinationCities = dataService.getDestinationCities(this.departCity);
        } else {
	        this.destinationCities = new ArrayList<>();
	        this.foundRides = new ArrayList<>();
        }
    }


    public void departCitySelected(AjaxBehaviorEvent event) {
        this.destinationCity = null;
        if (this.departCity != null && !this.departCity.isEmpty()) {
            this.destinationCities = dataService.getDestinationCities(this.departCity);
        } else {
            this.destinationCities.clear();
        }
    }

    public void doSearch() {
        System.out.println("BOTÓN PULSADO: Iniciando búsqueda...");
        this.foundRides.clear();

        // Validamos que el usuario haya metido todo
        if (this.departCity == null || this.destinationCity == null || this.rideDate == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Por favor, selecciona origen, destino y fecha.");
            return;
        }

        System.out.println("Parametros: " + departCity + " -> " + destinationCity + " [" + rideDate + "]");

        this.foundRides = dataService.getRides(departCity, destinationCity, rideDate);
        if (this.foundRides.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_INFO, "No se encontraron viajes con esos criterios.");
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, null));
    }
    
    public String getDepartCity() { return departCity; }
    public void setDepartCity(String departCity) { this.departCity = departCity; }

    public List<String> getDepartCities() { return departCities; }
    public void setDepartCities(List<String> departCities) { this.departCities = departCities; }

    public String getDestinationCity() { return destinationCity; }
    public void setDestinationCity(String destinationCity) { this.destinationCity = destinationCity; }

    public List<String> getDestinationCities() { return destinationCities; }
    public void setDestinationCities(List<String> destinationCities) { this.destinationCities = destinationCities; }

    public Date getRideDate() { return rideDate; }
    public void setRideDate(Date rideDate) { this.rideDate = rideDate; }

    public List<Ride> getFoundRides() { return foundRides; }
    public void setFoundRides(List<Ride> foundRides) { this.foundRides = foundRides; }
}