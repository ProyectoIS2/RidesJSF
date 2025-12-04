package model; // Asegúrate de que este paquete coincide con tu estructura

import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject; // NECESARIO para conectar con DataService
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

// Importamos tus clases
import domain.Driver;
import businessLogic.BLFacade;

@Named("register") // Esto permite usar #{register.email} en el XHTML
@RequestScoped
public class RegisterBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String name;
    private String password;

    // --- Inyección del Almacén de Datos ---
    @Inject
    private BLFacade facade;

    public RegisterBean() {
    }

    // Método de Acción (El que llama el botón) ---
    public String register() {
        try {
            // Verificar si el facade está funcionando
            if (facade == null) {
                System.out.println("ERROR CRÍTICO: BLFacade es null.");
                addMessage(FacesMessage.SEVERITY_ERROR, "Error interno del servidor.");
                return null;
            }

            // Comprobar si el usuario ya existe
            Driver driverExistente = facade.getDriver(this.email);
            if (driverExistente != null) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error: Este email ya está registrado.");
                return null;
            }

            Driver nuevoConductor = new Driver(this.email, this.name, this.password);

            facade.createDriver(this.email, this.name, this.password);

            System.out.println("DEBUG: Usuario registrado -> " + nuevoConductor.getEmail());

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            addMessage(FacesMessage.SEVERITY_INFO, "Registro correcto. Inicia sesión.");

            // Redirigir al Login
            return "Login?faces-redirect=true";

        } catch (Exception e) {
            e.printStackTrace();
            addMessage(FacesMessage.SEVERITY_ERROR, "Error al registrar: " + e.getMessage());
            return null;
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, null));
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}