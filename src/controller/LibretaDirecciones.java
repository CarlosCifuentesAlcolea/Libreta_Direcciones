
package controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Empaquetador;
import model.Persona;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import util.ConectorSql;
import util.JasperConector;
import view.EditarPersonaController;
import view.VistaEstadisticasController;
import view.VistaPersonaController;
import view.VistaPrincipalController;

/**
 * Clase LibretaDirecciones principal de la aplicación.
 * @author angel
 * @version 1.0 Tutorial version
 * @version 1.1 Performanced version
 * @version 1.2 Documented Version.
 */
public class LibretaDirecciones extends Application {

	/**
	 * Observable con la lista de personas actual
	 */
	private static ObservableList<Persona> datosPersonas = FXCollections.observableArrayList();
	
	/**
	 * Escenario principal de la parte gráfica de la Aplicación
	 */
	private static Stage escenarioPrincipal;
	
	/**
	 * Layout principal de la aplicación
	 */
	private BorderPane layoutPrincipal;
	
	/**
	 * Vistas gráficas utilizadas por la aplicación 
	 */
	private AnchorPane vistaPersona, editarPersona, vistaEstadisticas, vistaAbout;
	
	/**
	 * Conector MySQL de la aplicación para conexiones con la base de datos
	 */
	private static ConectorSql conector;
	
	/**
	 * Bundle utilizado para la selección de recursos
	 */
	public static ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/mensajes");

	/**
	 * Constructor por defecto de la clase LibretaDirecciones
	 */
	public LibretaDirecciones() {}

	/**
	 *  Método para devolver los datos como lista observable de personas.
	 * @return getDatosPersona
	 */
	public ObservableList<Persona> getDatosPersona() {
		return datosPersonas;
	}

	/**
	 * Método start que inicia la aplicación
	 */
	@Override
	public void start(Stage escenarioPrincipal) {

		LibretaDirecciones.escenarioPrincipal = escenarioPrincipal;
		LibretaDirecciones.escenarioPrincipal.setTitle(resourceBundle.getString("libretadirecciones.title"));
		LibretaDirecciones.escenarioPrincipal.getIcons().add(new Image(this.getClass().getResourceAsStream("../img/libretaDirecciones.png")));

		try {
			conector = new ConectorSql("jdbc:mysql://127.0.0.1:3306/dam?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "angel", "7zxdfg=0");
		} catch (SQLException e) {
			
		}
		//inicializo el layout principal
		initLayoutPrincipal();
		//muestro la vista principal
		muestraVistaPersona();

	}

	/**
	 * Inicia el layout de la vista principal.
	 */
	public void initLayoutPrincipal() {

		// Cargo el layout principal a partir de la vista VistaPrincipal.fxml
		FXMLLoader loader = new FXMLLoader();
		URL location = LibretaDirecciones.class.getResource("../view/VistaPrincipal.fxml");
		loader.setResources(resourceBundle);
		loader.setLocation(location);
		try {
			layoutPrincipal = loader.load();
		} catch (IOException ex) {
			Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Cargo la escena que contiene ese layout principal
		Scene escena = new Scene(layoutPrincipal);
		escenarioPrincipal.setScene(escena);

		// Doy al controlador acceso a la aplicación principal
		VistaPrincipalController controller = loader.getController();
		controller.setLibretaDirecciones(this);

		// Muestro la escena
		escenarioPrincipal.show();
		
		//Cargo personas de la base de datos (borrando las anteriores)
		datosPersonas.clear();
		try {
			datosPersonas.addAll(conector.getPersonas());
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Selecciona un idioma y carga de nuevo la vista con todos los textos cambiados
	 * por el idioma definido.
	 * @param idioma Idioma elegido por el usuario
	 */
	public void elegirIdioma(String idioma) {
		switch(idioma) {
		case "es":
			Locale.setDefault(new Locale("es"));
			resourceBundle = ResourceBundle.getBundle("i18n/mensajes");
			break;
		case "en":
			Locale.setDefault(Locale.ENGLISH);
			resourceBundle = ResourceBundle.getBundle("i18n/mensajes_en");
			break;
		case "fr":
			Locale.setDefault(Locale.FRENCH);
			resourceBundle = ResourceBundle.getBundle("i18n/mensajes_fr");
			break;
		default:
			break;
		}
		
		initLayoutPrincipal();
		muestraVistaPersona();
	}

	/**
	 * Muestra la vista con la persona seleccionada.
	 */
	public void muestraVistaPersona() {

		// Cargo la vista persona a partir de VistaPersona.fxml
		FXMLLoader loader = new FXMLLoader();
		URL location = LibretaDirecciones.class.getResource("../view/VistaPersona.fxml");
		loader.setResources(resourceBundle);
		loader.setLocation(location);
		try {
			vistaPersona = loader.load();
			
		} catch (IOException ex) {
			Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
		}

		// Añado la vista al centro del layoutPrincipal
		layoutPrincipal.setCenter(vistaPersona);

		// Doy acceso al controlador VistaPersonaCOntroller a LibretaDirecciones
		VistaPersonaController controller = loader.getController();
		controller.setLibretaDirecciones(this);

	}

	/**
	 *  Devuelve el escenario principal
	 * @return Stage escenarioPrincipal
	 */
	public static Stage getPrimaryStage() {
		return escenarioPrincipal;
	}

	/**
	 * Método inicial de la aplicación
	 * @param args Argumentos iniciales de la aplicación
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Muestra la vista de edición de la persona seleccionada
	 * @param persona
	 * @return 
	 */
	public boolean muestraEditarPersona(Persona persona) {

		// Cargo la vista persona a partir de VistaPersona.fxml
		FXMLLoader loader = new FXMLLoader();
		URL location = LibretaDirecciones.class.getResource("../view/EditarPersona.fxml");
		loader.setResources(resourceBundle);
		loader.setLocation(location);
		try {
			editarPersona = loader.load();
		} catch (IOException ex) {
			Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}

		// Creo el escenario de edición (con modal) y establezco la escena
		Stage escenarioEdicion = new Stage();
		escenarioEdicion.setTitle(resourceBundle.getString("personedit.title"));
		escenarioEdicion.initModality(Modality.WINDOW_MODAL);
		escenarioEdicion.initOwner(escenarioPrincipal);
		escenarioEdicion.getIcons().add(new Image(this.getClass().getResourceAsStream("../img/libretaDirecciones.png")));
		Scene escena = new Scene(editarPersona);
		escenarioEdicion.setScene(escena);

		// Asigno el escenario de edición y la persona seleccionada al controlador
		EditarPersonaController controller = loader.getController();
		controller.setEscenarioEdicion(escenarioEdicion); //(this)
		controller.setPersona(persona);

		// Muestro el diálogo hasta que el usuario lo cierre
		escenarioEdicion.showAndWait();

		// devuelvo el botón pulsado
		return controller.isGuardarClicked();

	}

	/**
	 * Carga personas desde la base de datos.
	 */
	public static void cargaPersonas() {

		try {
			datosPersonas.addAll(conector.getPersonas());
			
			Alert alerta = new Alert(Alert.AlertType.INFORMATION);
			alerta.setTitle(resourceBundle.getString("carga.completa.db"));
			alerta.setHeaderText(resourceBundle.getString("carga.completa.db.texto"));
			alerta.showAndWait();
			
		} catch (SQLException e) {
			
			e.printStackTrace();
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle(resourceBundle.getString("error"));
			alerta.setHeaderText(resourceBundle.getString("error.cargar.db"));
			alerta.showAndWait();
			
		}
		


	}
	
	/**
	 * Carga personas desde un fichero XML
	 * @param file Archivo XML utilizado
	 * @throws JAXBException Excepción posible durante el proceso de carga de personas desde el archivo XML a la aplicación
	 */
	public void cargaPersonasXML(File file) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Empaquetador.class);
		Unmarshaller unmarshall = context.createUnmarshaller();
		
		Empaquetador empaquetador = (Empaquetador) unmarshall.unmarshal(file);
		datosPersonas.clear();
		datosPersonas.addAll(empaquetador.getPersonas());
	}

	/**
	 * Guarda personas en un fichero XML
	 * @param file Archivo XML utilizado
	 */
	public void guardaPersonas(File file) {

		try {
			// Contexto
			JAXBContext context = JAXBContext.newInstance(Empaquetador.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			// Empaqueto los datos de las personas
			Empaquetador empaquetador = new Empaquetador();
			empaquetador.setPersonas(getDatosPersona());

			// Marshall y guardo XML a archivo
			m.marshal(empaquetador, file);

		} catch (Exception e) {
			// Muestro alerta
			Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle(resourceBundle.getString("error"));
			alerta.setHeaderText(resourceBundle.getString("error.guardar"));
			alerta.setContentText(e.toString());
			alerta.showAndWait();
		}
		

	}
	
	/**
	 * Guarda la lista actual de personas en la base de datos
	 */
	public void guardaPersonas() {
		try {
			conector.putPersonas(datosPersonas);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Muestra las estadísticas en la vista de estadísticas.
	 */
	public void vistaEstadisticas() {

		//Cargo la vista estadísticas
        FXMLLoader loader = new FXMLLoader();
        URL location = LibretaDirecciones.class.getResource("/view/VistaEstadisticas.fxml");

        loader.setResources(resourceBundle);
        AnchorPane vistaEstadisticas = new AnchorPane();
        loader.setLocation(location);
        try {
            vistaEstadisticas = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Inicializo un nuevo escenario y asigno el principal
        Stage escenarioEstadisticas = new Stage();        
        escenarioEstadisticas.setTitle(resourceBundle.getString("estadisticas"));
        escenarioEstadisticas.initModality(Modality.WINDOW_MODAL);
        escenarioEstadisticas.initOwner(escenarioPrincipal);
        escenarioEstadisticas.getIcons().add(new Image(this.getClass().getResourceAsStream("../img/libretaDirecciones.png")));
        
        //Cargo la escena que contiene ese layout de estadisticas
        Scene escena = new Scene(vistaEstadisticas);
        escenarioEstadisticas.setScene(escena);
                    
        //Asigno el controlador
        VistaEstadisticasController controller = loader.getController();
        controller.setDatosPersonas(datosPersonas);

        //Muestro el escenario
        escenarioEstadisticas.show();
        

	}
	
	/**
	 * Muestra la información sobre el autor en la vista de VistaAbout.
	 */
	public void vistaAbout() {

		//Cargo la vista estadísticas
        FXMLLoader loader = new FXMLLoader();
        URL location = LibretaDirecciones.class.getResource("/view/VistaAbout.fxml");

        loader.setResources(resourceBundle);
        AnchorPane vistaAbout = new AnchorPane();
        loader.setLocation(location);
        try {
        	vistaAbout = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(LibretaDirecciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Inicializo un nuevo escenario y asigno el principal
        Stage escenarioAbout = new Stage();        
        escenarioAbout.setTitle(resourceBundle.getString("menu.item.about"));
        escenarioAbout.initModality(Modality.WINDOW_MODAL);
        escenarioAbout.initOwner(escenarioPrincipal);
        escenarioAbout.getIcons().add(new Image(this.getClass().getResourceAsStream("../img/libretaDirecciones.png")));

        
        //Cargo la escena que contiene ese layout de estadisticas
        Scene escena = new Scene(vistaAbout);
        escenarioAbout.setScene(escena);
                    
        //Asigno el controlador
        VistaEstadisticasController controller = loader.getController();

        //Muestro el escenario
        escenarioAbout.show();

	}
	
	

	/**
	 * Crear PDF a partir de los datos de la aplicación.
	 * @throws IOException
	 */
	public void crearPDF() throws IOException {

		// Creo un nuevo documento, una página y la añado
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

		// Establezco la posición Y de la primera líena y el tipo de fuente
		int linea = 700;
		contentStream.setFont(PDType1Font.TIMES_ROMAN, 10);

		// Recorro la lista de personas
		List<Persona> personas = datosPersonas;
		for (Persona p : personas) {
			// Inicio un nuevo texto y escribo los datos
			contentStream.beginText();
			contentStream.newLineAtOffset(25, linea);
			contentStream.showText(resourceBundle.getString("personview.detalles.nombre") + ": " + p.getNombre() + " " + p.getApellidos() + " | ");
			contentStream.showText(resourceBundle.getString("personview.detalles.fecNac") + ": " + p.getFechaDeNacimiento());
			contentStream.showText(resourceBundle.getString("personview.detalles.direccion") +  ": " + p.getDireccion() + ", " + p.getCiudad());
			contentStream.endText();
			// Cambio de línea
			linea -= 25;
		}

		// Cierro el content stream
		contentStream.close();
		
		try {
			// Inicio el file chooser
			FileChooser fileChooser = new FileChooser();

			// Filtro para la extensión
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
			fileChooser.getExtensionFilters().add(extFilter);

			// Muestro el diálogo de guardar
			File archivo = fileChooser.showSaveDialog(getPrimaryStage());

			if (archivo != null) {

				// Me aseguro de que tiene la extensión correcta y si no la cambio
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
			alerta.setTitle(resourceBundle.getString("error"));
			alerta.setHeaderText(resourceBundle.getString("error.guardar"));
			alerta.showAndWait();
			cont.close();
			contentStream.close();
			documento.close();
		}


	}
	

    /**
     * Abre un documento JasperReports para consultar su contenido
     * @throws JRException Excepción al acceder al documento jasper Reports
     */
    public static void abrirJasperDocuments() throws JRException, NullPointerException {
 
		// Previsualización
		JasperPrint jasperPrintWindow = JasperFillManager.fillReport("D:\\Eclipse-workspace\\libretaDirecciones\\src\\jasper\\Contactos.jasper", null, ConectorSql.getConexion());
		JasperViewer jasperViewer = new JasperViewer(jasperPrintWindow);
		jasperViewer.setVisible(true);

    }
}
