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

import domain.Ride;
import businessLogic.BLFacade;

@Named("queryRides")
@ViewScoped
public class QueryRidesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String departCity;
    private List<String> departCities;
    
    private String destinationCity;
    private List<String> destinationCities = new ArrayList<>();
    
    private Date rideDate;
    private List<Ride> foundRides = new ArrayList<>();

    @Inject
    private BLFacade facade;

    @PostConstruct
    public void init() {
        try {
            this.departCities = facade.getDepartCities();
            
            if (this.departCities != null && !this.departCities.isEmpty()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Error conectando con la base de datos.");
        }
    }

    public void departCitySelected(AjaxBehaviorEvent event) {
        this.destinationCity = null;
        this.destinationCities.clear();
        this.foundRides.clear(); 

        if (this.departCity != null && !this.departCity.isEmpty()) {
            try {
                this.destinationCities = facade.getDestinationCities(this.departCity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void doSearch() {
        this.foundRides.clear();

        if (this.departCity == null || this.departCity.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Selecciona una ciudad de origen.");
            return;
        }
        if (this.destinationCity == null || this.destinationCity.isEmpty()) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Selecciona una ciudad de destino.");
            return;
        }
        if (this.rideDate == null) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Selecciona una fecha.");
            return;
        }

        System.out.println("Buscando: " + departCity + " -> " + destinationCity + " [" + rideDate + "]");

        try {
            this.foundRides = facade.getRides(departCity, destinationCity, rideDate);
            
            if (this.foundRides.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_INFO, "No hay viajes para esa fecha.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Error al buscar viajes.");
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