package model; // O tu paquete model

import java.io.Serializable;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import jakarta.faces.application.FacesMessage;
import domain.Driver;

@Named("login")
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String email;
    private String password;
    
    @Inject
    private DataService dataService;
    
    // Variable para guardar quién está conectado
    private Driver driverLogeado = null; 

    public LoginBean() {
    }


    public String login() {       
        Driver driverEncontrado = null;
   //     if ("test@test.com".equals(email)) {
   //          driverEncontrado = new Driver("test@test.com", "Test User", "123");
   //      }
        if (dataService.existeUsuario(email)!= false) {
        	driverEncontrado = dataService.buscarUsuario(email);
        }
        else {
        	return null;
        }

        if (driverEncontrado != null && driverEncontrado.getPassword().equals(this.password)) {
            this.driverLogeado = driverEncontrado;
            return "DriverMenu?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email o contraseña incorrectos"));
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