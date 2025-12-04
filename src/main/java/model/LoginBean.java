package model; // O el paquete donde lo tengas (bean o model)

import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;

import domain.Driver;
import businessLogic.BLFacade; // Importamos la interfaz

@Named("login")
@SessionScoped
public class LoginBean implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String email;
    private String password;
    

    @Inject
    private BLFacade facade; 
    
    private Driver driverLogeado = null; 

    public LoginBean() {
    }

    public String login() {        
        try {
            Driver driverEncontrado = facade.getDriver(email);

            if (driverEncontrado != null && driverEncontrado.getPassword().equals(this.password)) {
                
                this.driverLogeado = driverEncontrado;
                System.out.println("Login correcto: " + driverLogeado.getName());
                
                return "DriverMenu?faces-redirect=true";
                
            } else {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email o contraseña incorrectos"));
                return null; 
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error interno"));
            return null;
        }
    }
    
    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "Index?faces-redirect=true";
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Driver getDriverLogeado() { return driverLogeado; }
}