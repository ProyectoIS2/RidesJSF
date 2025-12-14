package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation;
import domain.Ride;

@Named("cheapRides")
@ViewScoped
public class CheapRidesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private float priceLimit; 
    
    private List<Ride> cheapRides; 

    private BLFacade facade = new BLFacadeImplementation(); 

    @PostConstruct
    public void init() {
        this.cheapRides = new ArrayList<>();
        this.priceLimit = (float) 0.0; 
    }

    public void search() {
        try {
            System.out.println("Buscando viajes por menos de: " + priceLimit);
            
            this.cheapRides = facade.getCheaperRides(priceLimit);
            
            if (cheapRides.isEmpty()) {
                addMessage(FacesMessage.SEVERITY_WARN, "No hay viajes por ese precio.");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Error al buscar viajes.");
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, null));
    }


    public List<Ride> getCheapRides() {
        return cheapRides;
    }
    public double getPriceLimit() {
        return priceLimit;
    }
    public void setPriceLimit(double priceLimit) {
        this.priceLimit = (float) priceLimit;
    }
}