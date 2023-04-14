package view;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import controller.LibretaDirecciones;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import model.Persona;
import util.ConectorSql;
import util.UtilidadDeFechas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class VistaPersonaController {

	/**
	 * Tabla de las personas
	 */
	@FXML
	private TableView<Persona> tablaPersonas;
	
	/**
	 * Columna con el nombre de las personas
	 */
	@FXML
	private TableColumn<?, ?> nombreColumn;
	
	/**
	 * Columna con el apellido de las personas
	 */
	@FXML
	private TableColumn<?, ?> apellidosColumn;

	/**
	 * Etiqueta nombre
	 */
	@FXML
	private Label nombreLabel;
	
	/**
	 * Etiqueta apellidos
	 */
	@FXML
	private Label apellidosLabel;
	
	/**
	 * Etiqueta direcci�n
	 */
	@FXML
	private Label direccionLabel;
	
	/**
	 * Etiqueta c�digo postal
	 */
	@FXML
	private Label codigoPostalLabel;
	
	/**
	 * Etiqueta ciudad
	 */
	@FXML
	private Label ciudadLabel;
	
	/**
	 * Etiqueta fechaDeNacimiento
	 */
	@FXML
	private Label fechaDeNacimientoLabel;

	/**
	 *  Referencia a la clase principal
	 */
	private LibretaDirecciones libretaDirecciones;

	/**
	 *  El constructor es llamado ANTES del m�todo initialize
	 */
	public VistaPersonaController() {
	}

	/**
	 *  Inicializa la clase controller y es llamado justo despu�s de cargar el FMXL
	 */
	@FXML
	private void initialize() {

		// Inicializo la tabla con las dos primeras columnas
		String nombre = "nombre";
		String apellidos = "apellidos";
		nombreColumn.setCellValueFactory(new PropertyValueFactory<>(nombre));
		apellidosColumn.setCellValueFactory(new PropertyValueFactory<>(apellidos));

		// Borro los detalles de la persona
		mostrarDetallesPersona(null);

		// Escucho cambios en la selecci�n de la tabla y muestro los detalles en caso de
		// cambio
		tablaPersonas.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mostrarDetallesPersona((Persona) newValue));
	}

	/**
	 * Es llamado por la apliaci�n principal para tener una referencia de vuelta de si mismo
	 * @param libretaDirecciones
	 */
	public void setLibretaDirecciones(LibretaDirecciones libretaDirecciones) {

		this.libretaDirecciones = libretaDirecciones;

		// A�ado la lista obervable a la tabla
		tablaPersonas.setItems(libretaDirecciones.getDatosPersona());
	}

	/**
	 *  Muestra los detalles de la persona seleccionada
	 * @param persona
	 */
	private void mostrarDetallesPersona(Persona persona) {

		if (persona != null) {
			// Relleno los labels desde el objeto persona
			nombreLabel.setText(persona.getNombre());
			apellidosLabel.setText(persona.getApellidos());
			direccionLabel.setText(persona.getDireccion());
			codigoPostalLabel.setText(Integer.toString(persona.getCodigoPostal()));
			ciudadLabel.setText(persona.getCiudad());
			fechaDeNacimientoLabel.setText(UtilidadDeFechas.formato(persona.getFechaDeNacimiento())); // fechaDeNacimientoLabel.setText(...);
		} else {
			// Persona es null, vac�o todos los labels.
			nombreLabel.setText("");
			apellidosLabel.setText("");
			direccionLabel.setText("");
			codigoPostalLabel.setText("");
			ciudadLabel.setText("");
			fechaDeNacimientoLabel.setText("");
		}
	}
	
	/**
	 * Borro la persona seleccionada cuando el usuario hace clic en el bot�n de Borrar
	 */
    @FXML
    private void borrarPersona() {
    	
    	Persona persona = tablaPersonas.getItems().get(tablaPersonas.getSelectionModel().getSelectedIndex());
    	
        //Capturo el indice seleccionado y borro su item asociado de la tabla
        int indiceSeleccionado = tablaPersonas.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0){
            //Borro item
            tablaPersonas.getItems().remove(indiceSeleccionado);
            
            Alert alerta = new Alert(AlertType.CONFIRMATION);
            alerta.setTitle("Borrar Persona de la Base de datos");
            alerta.setHeaderText("�Dese borrar la persona de la Base de datos?");
            
            Optional<ButtonType> result = alerta.showAndWait();
            if (result.get() == ButtonType.OK){
               ConectorSql.borrarPersona(persona.getId());
            }
            
        } else {
            //Muestro alerta
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Atenci�n");
            alerta.setHeaderText("Persona no seleccionada");
            alerta.setContentText("Por favor, selecciona una persona de la tabla");
            alerta.showAndWait();
                        
        }    
    }
    
    /**
     * Muestro el di�logo editar persona cuando el usuario hace clic en el bot�n de Crear
     */
    @FXML
    private void crearPersona() {
        Persona temporal = new Persona();
        boolean guardarClicked = libretaDirecciones.muestraEditarPersona(temporal);
        if (guardarClicked) {
            libretaDirecciones.getDatosPersona().add(temporal);
        }
    }
    
    /**
     * Muestro el di�logo editar persona cuando el usuario hace clic en el bot�n de Editar
     */
    @FXML
    private void editarPersona() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            boolean guardarClicked = libretaDirecciones.muestraEditarPersona(seleccionada);
            if (guardarClicked) {
                mostrarDetallesPersona(seleccionada);
            }

        } else {
            //Muestro alerta
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Alerta");
            alerta.setHeaderText("Persona no seleccionada");
            alerta.setContentText("Por favor, selecciona una persona");
            alerta.showAndWait();
        }
    }
    

	/**
	 * Crear PDF a partir de un registro de los datos de la aplicaci�n.
	 * @throws IOException
	 */
	public void crearPDFPersona() throws IOException {
		
		// Indicamos a la persona seleccionada
		Persona persona = tablaPersonas.getSelectionModel().getSelectedItem();

		try {
			// Creo un nuevo documento, una p�gina y la a�ado
			PDDocument documento = new PDDocument();
			PDPage pagina = new PDPage();
			documento.addPage(pagina);
			documento.getPage(0);
			
			// Insertamos imagen
			String imgPath = "src/jasper/leaf_banner_violet.png";
			PDImageXObject pdImage = PDImageXObject.createFromFile(imgPath, documento);
			
            int iw = pdImage.getWidth();
            int ih = pdImage.getHeight();
            float offset = 20f;

            PDPageContentStream cont = new PDPageContentStream(documento, pagina);
            cont.drawImage(pdImage, offset, offset, iw, ih);
            cont.close();

			// Inicio un nuevo stream de contenido
			PDPageContentStream contentStream = new PDPageContentStream(documento, pagina, PDPageContentStream.AppendMode.APPEND, true);

			// Establezco la posici�n Y de la primera l�ena y el tipo de fuente
			int linea = 700;

			// Inicio un nuevo texto y escribo los datos
			contentStream.beginText();
			contentStream.setFont(PDType1Font.TIMES_BOLD, 25);
			contentStream.setLeading(14.5f);
			contentStream.newLineAtOffset(25, linea);
			contentStream.setFont(PDType1Font.TIMES_ROMAN, 12);
			contentStream.showText("Informe Personal");
			contentStream.newLine();
			contentStream.showText("Nombre: " + persona.getNombre() + " " + persona.getApellidos());
			contentStream.showText("| Fecha nac.: " + persona.getFechaDeNacimiento());
			contentStream.newLine();
			contentStream.showText("| Direcci�n: " + persona.getDireccion() + ", " + persona.getCiudad());
			contentStream.newLine();
			contentStream.endText();	

			// Cierro el content stream
			contentStream.close();

			// Inicio el file chooser
			FileChooser fileChooser = new FileChooser();

			// Filtro para la extensi�n
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
			fileChooser.getExtensionFilters().add(extFilter);

			// Muestro el di�logo de guardar
			File archivo = fileChooser.showSaveDialog(LibretaDirecciones.getPrimaryStage());

			if (archivo != null) {

				// Me aseguro de que tiene la extensi�n correcta y si no la cambio
				String extension = "";
				if (!archivo.getPath().endsWith(extension)) {
					extension = ".pdf";
				}
				// Escribo en el archivo y lo cierro
				archivo = new File(archivo.getPath() + extension);
				documento.save(archivo);
				documento.close();

			}

			// Abro el archivo en el visor de PDF del sistema
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().open(archivo);
				} catch (IOException ex) {
				}
			}
		} catch(NullPointerException e) {
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle(LibretaDirecciones.resourceBundle.getString("error"));
			alerta.setHeaderText(LibretaDirecciones.resourceBundle.getString("error.guardar"));
			alerta.showAndWait();
		}
		

	}
	public void crearJasperPersona(Persona persona) {
		if(persona != null) {
			Map<Integer, Object> p = new HashMap<>();
			p.put(persona.getId(), persona);
			
		}
	}


}