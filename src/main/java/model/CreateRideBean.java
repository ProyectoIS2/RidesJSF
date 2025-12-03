package model;

import java.io.Serializable;
import java.util.Date;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import domain.Driver;
import domain.Ride;


@Named("createRide") // para q coincida con #{createRide...} del XHTML
@RequestScoped
public class CreateRideBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String departCity;
    private String arrivalCity;
    private float price;   
    private int seats;     
    private Date rideDate; 
    
    @Inject
    private DataService dataService; //despues cambiarlo para lo del hibernate

    @Inject
    private LoginBean loginBean;

    public CreateRideBean() {
    }

    public String createRide() {
        try {
            Driver conductorActual = loginBean.getDriverLogeado();
            if (conductorActual == null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error: No estás logueado.");
                return "Login?faces-redirect=true";
            }

            // para validar que la fecha sea posterior a hoy
            if (rideDate.before(new Date())) {
                addMessage(FacesMessage.SEVERITY_ERROR, "La fecha debe ser posterior a hoy.");
                return null;
            }

            // para validar que origen y destino no sean iguales
            if (departCity.equalsIgnoreCase(arrivalCity)) {
                addMessage(FacesMessage.SEVERITY_ERROR, "El origen y el destino no pueden ser iguales.");
                return null;
            }
            
            //el conductorActual.addRide puede ir sin guardarse en una variable, pero se queda así porque me funciona y no quiero jugarmela jeje
            Ride nuevoViaje = conductorActual.addRide(departCity, arrivalCity, rideDate, seats, price);

            // Como hemos modificado la lista de viajes del conductor, avisamos al dataservice
            dataService.guardarUsuario(conductorActual);

            System.out.println("Viaje creado: " + departCity + " -> " + arrivalCity + " por " + conductorActual.getName());

            // 5. Mensaje de éxito
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            addMessage(FacesMessage.SEVERITY_INFO, "Viaje creado correctamente.");

            // redirigir, para volver al menu
            return "DriverMenu?faces-redirect=true"; 

        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Error al crear viaje: " + e.getMessage());
            return null;
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, null));
    }

    public String getDepartCity() { return departCity; }
    public void setDepartCity(String departCity) { this.departCity = departCity; }

    public String getArrivalCity() { return arrivalCity; }
    public void setArrivalCity(String arrivalCity) { this.arrivalCity = arrivalCity; }

    public float getPrice() { return price; }
    public void setPrice(float price) { this.price = price; }

    public int getSeats() { return seats; }
    public void setSeats(int seats) { this.seats = seats; }

    public Date getRideDate() { return rideDate; }
    public void setRideDate(Date rideDate) { this.rideDate = rideDate; }
}