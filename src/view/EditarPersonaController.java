package view;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Persona;
import util.ConectorSql;
import util.UtilidadDeFechas;

public class EditarPersonaController {
    
    /**
     * TextField para introducir nombre
     */
    @FXML
    private TextField nombreTextField;
    
    /**
     * TextField para introducir apellidos
     */
    @FXML
    private TextField apellidosTextField;
    
    /**
     * TextField para introducir direccion
     */
    @FXML
    private TextField direccionTextField;
    
    /**
     * TextField para introducir codigo postal
     */
    @FXML
    private TextField codigoPostalTextField;
    
    /**
     * TextField para introducir ciudad
     */
    @FXML
    private TextField ciudadTextField;
    
    /**
     * TextField para introducir fecha de nacimiento
     */
    @FXML
    private TextField fechaDeNacimientoTextField;
    
    /**
     * Escenario de edici�n
     */
    private Stage escenarioEdicion;
    
    /**
     *  Referencia a la clase persona
     */
    private Persona persona; 
    
    /**
     * Referencia al bot�n guardar
     */
    private boolean guardarClicked = false;
    
    /**
     * Inicializa la clase controller y es llamado justo despu�s de cargar el archivo FXML
     */
    @FXML
    private void initialize() {
    }
    
    /**
     * Establece el escenario de edici�n
     * @param escenarioEdicion
     */
    public void setEscenarioEdicion(Stage escenarioEdicion) {
        this.escenarioEdicion = escenarioEdicion;
    }
    
    /**
     * Establece la persona a editar
     * @param persona
     */
    public void setPersona(Persona persona) {
        this.persona = persona;

        nombreTextField.setText(persona.getNombre());
        apellidosTextField.setText(persona.getApellidos());
        direccionTextField.setText(persona.getDireccion());
        codigoPostalTextField.setText(Integer.toString(persona.getCodigoPostal()));
        ciudadTextField.setText(persona.getCiudad());
        fechaDeNacimientoTextField.setText(UtilidadDeFechas.formato(persona.getFechaDeNacimiento()));
        fechaDeNacimientoTextField.setPromptText("dd/mm/yyyy");
        
    }
    
    /**
     * Devuelve true si se ha pulsado Guardar, si no devuelve false
     * @return
     */
    public boolean isGuardarClicked() {
        return guardarClicked;
    }
    
    /**
     * LLamado cuando se pulsa Guardar
     */
    @FXML
    private void guardar() {
        
        if (datosValidos()) {
            
            //Asigno datos a propiedades de persona
            persona.setNombre(nombreTextField.getText());
            persona.setApellidos(apellidosTextField.getText());
            persona.setDireccion(direccionTextField.getText());
            persona.setCodigoPostal(Integer.parseInt(codigoPostalTextField.getText()));
            persona.setCiudad(ciudadTextField.getText());
            persona.setFechaDeNacimiento(UtilidadDeFechas.convertir(fechaDeNacimientoTextField.getText()));
            
            if(persona.getId() == 0) {
            	try {
					ConectorSql.insertPersona(persona);
				} catch (SQLException e) {

		            Alert alerta = new Alert(Alert.AlertType.ERROR);
		    		alerta.setTitle("Error en la inserci�n");
		    		alerta.setHeaderText("La inserci�n no pudo realizarse.");
		    		alerta.setContentText(e.toString());
		    		alerta.showAndWait();
				}
            } else {
                ConectorSql.updatePersona(persona);

            }
            

            guardarClicked = true; //Cambio valor booleano
            escenarioEdicion.close(); //Cierro el escenario de edici�n
            
        }
    }
    
    /**
     * LLamado cuando se pulsa Cancelar
     */
    @FXML
    private void cancelar() {
        escenarioEdicion.close();
    }
    
    /**
     * Validaci�n de datos
     * @return
     */
    private boolean datosValidos(){
        
        //Inicializo string para mensajes
        String mensajeError = "";

        //Compruebo los campos
        if (nombreTextField.getText() == null || nombreTextField.getText().length() == 0) {
            mensajeError += "Nombre no v�lido.\n"; 
        }
        if (apellidosTextField.getText() == null || apellidosTextField.getText().length() == 0) {
            mensajeError += "Apellidos no v�lidos.\n"; 
        }
        if (direccionTextField.getText() == null || direccionTextField.getText().length() == 0) {
            mensajeError += "Direcci�n no v�lida.\n"; 
        }

        if (codigoPostalTextField.getText() == null || codigoPostalTextField.getText().length() == 0) {
            mensajeError += "C�digo postal no v�lido.\n"; 
        } else {
            //Convierto el c�digo postal a entero
            try {
                Integer.parseInt(codigoPostalTextField.getText());
            } catch (NumberFormatException e) {
                mensajeError += "C�digo postal no v�lido (debe ser un entero).\n"; 
            }
        }

        if (ciudadTextField.getText() == null || ciudadTextField.getText().length() == 0) {
            mensajeError += "Ciudad no v�lida.\n"; 
        }

        if (fechaDeNacimientoTextField.getText() == null || fechaDeNacimientoTextField.getText().length() == 0) {
            mensajeError += "Fecha de nacimiento no v�lida.\n";
        } else {
            if (!UtilidadDeFechas.fechaValida(fechaDeNacimientoTextField.getText())) {
                mensajeError += "Fecha de nacimiento no v�lida (debe tener formato dd/mm/yyyy).\n";
            }
        }

        //Si no hay errores devuelvo true, si no, una alerta con los errores y false
        if (mensajeError.length() == 0) {
            return true;
        } else {
            //Muestro alerta y devuelvo false
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Datos no v�lidos");
            alerta.setContentText("Por favor, corrige los errores");
            alerta.showAndWait();
            return false;
        }
        
    }
    
}