package model;

import java.io.Serializable;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import businessLogic.BLFacade;
import businessLogic.BLFacadeImplementation; // Tu implementación real
import domain.Driver;
import domain.Ride;

@Named("myRides")
@ViewScoped
public class MyRidesBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Ride> myRides; //para que coincida con el valor myRides.myRides del xhtml

    @Inject
    private LoginBean loginBean;

    private BLFacade facade = new BLFacadeImplementation(); 

    @PostConstruct
    public void init() {
        Driver conductor = loginBean.getDriverLogeado();
        
        if (conductor != null) {
            try {
            	this.myRides = facade.getRidesByDriver(conductor);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al recuperar los viajes del conductor.");
            }
        }
    }

    public List<Ride> getMyRides() {
        return myRides;
    }
}