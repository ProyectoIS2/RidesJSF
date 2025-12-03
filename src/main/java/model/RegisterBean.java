package model; // Asegúrate de que este paquete coincide con tu estructura

import java.io.Serializable;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject; // NECESARIO para conectar con DataService
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

// Importamos tus clases
import domain.Driver;

@Named("register") // Esto permite usar #{register.email} en el XHTML
@RequestScoped
public class RegisterBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String email;
    private String name;
    private String password;

    // --- Inyección del Almacén de Datos ---
    // Esto busca la clase DataService (@ApplicationScoped) automáticamente
    @Inject
    private DataService dataService;

    public RegisterBean() {
    }

    // Método de Acción (El que llama el botón) ---
    public String register() {
        try {
            // Verificar si el DataService está funcionando
            if (dataService == null) {
                System.out.println("ERROR CRÍTICO: DataService es null. Revisa que DataService tenga @Named y @ApplicationScoped");
                addMessage(FacesMessage.SEVERITY_ERROR, "Error interno del servidor.");
                return null;
            }

            // Comprobar si el usuario ya existe
            if (dataService.existeUsuario(this.email)) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error: Este email ya está registrado.");
                return null;
            }

            // Crear el objeto Driver (Usando el constructor de 3 parámetros)
            Driver nuevoConductor = new Driver(this.email, this.name, this.password);
            
            // Guardar en el almacén compartido (la base de datos simulada)
            dataService.guardarUsuario(nuevoConductor);
            
            System.out.println("DEBUG: Usuario registrado -> " + nuevoConductor.getEmail());

            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            addMessage(FacesMessage.SEVERITY_INFO, "Registro correcto. Inicia sesión.");

            // 6. Redirigir al Login
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