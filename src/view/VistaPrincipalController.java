package view;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import controller.LibretaDirecciones;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import util.ConectorSql;

public class VistaPrincipalController {

	/**
	 *  Referencia a la clase principal
	 */
	private LibretaDirecciones libretaDirecciones;

	/**
	 *  Es llamada por la clase Principal para tener una referencia de vuelta de si misma
	 * @param libretaDirecciones
	 */
	public void setLibretaDirecciones(LibretaDirecciones libretaDirecciones) {
		this.libretaDirecciones = libretaDirecciones;
	}

	/**
	 *  Libreta de direcciones en XML vac�a
	 */
	@FXML
	private void nuevo() {
		libretaDirecciones.getDatosPersona().clear();
	}

	/**
	 *  Abro un File Chooser para que el usario seleccione una libreta guardada en xml
	 * @throws JAXBException Excepci�n relacionada con la carga del archivo xml de la libreta especificada
	 */
	@FXML
	private void abrir() throws JAXBException, Exception {

		FileChooser fileChooser = new FileChooser();
			
		FileChooser.ExtensionFilter extensions = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensions);

        File file = fileChooser.showOpenDialog(LibretaDirecciones.getPrimaryStage());

        if (file != null) {
        	try {
                libretaDirecciones.cargaPersonasXML(file);
        	} catch(javax.xml.bind.UnmarshalException e) {
            	Alert alerta = new Alert(Alert.AlertType.ERROR);
    			alerta.setTitle(LibretaDirecciones.resourceBundle.getString("error"));
    			alerta.setHeaderText(LibretaDirecciones.resourceBundle.getString("error.abrir"));
    			alerta.setContentText(LibretaDirecciones.resourceBundle.getString("error.abrir.texto"));
    			alerta.showAndWait();
        	}
        }
        else {
        	Alert alerta = new Alert(Alert.AlertType.ERROR);
			alerta.setTitle(LibretaDirecciones.resourceBundle.getString("error"));
			alerta.setHeaderText(LibretaDirecciones.resourceBundle.getString("error.abrir"));
			alerta.showAndWait();
        }
	}
	
	@FXML
	private void abrirBD() {
		libretaDirecciones.getDatosPersona().clear();
		LibretaDirecciones.cargaPersonas();
	}

	/**
	 *  Guardar la lista de personas en la base de datos
	 */
	@FXML
	private void guardar() {
		libretaDirecciones.guardaPersonas();
	}

	/**
	 *  Abro un File Chooser para guardar la lista de personas en un archivo XML
	 */
	@FXML
	private void guardarComo() {

		FileChooser fileChooser = new FileChooser();

		// Filtro para la extensi�n
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		// Muestro el di�logo de guardar
		File archivo = fileChooser.showSaveDialog(LibretaDirecciones.getPrimaryStage());

		if (archivo != null) {
			// Me aseguro de que tiene la extensi�n correcta
			if (!archivo.getPath().endsWith(".xml")) {
				archivo = new File(archivo.getPath() + ".xml");
			}
			libretaDirecciones.guardaPersonas(archivo);
		}
	}

	/**
	 * Muestra informaci�n Acerca de
	 */
	@FXML
	private void acercaDe() {
		Alert alerta = new Alert(Alert.AlertType.INFORMATION);
		alerta.setTitle(LibretaDirecciones.resourceBundle.getString("menu.item.about"));
		alerta.setContentText(
				"Autor: �ngel S�nchez\n Powered by SxZ");
		alerta.showAndWait();
	}

	/**
	 *  Salir
	 */
	@FXML
	private void salir() {
		System.exit(0);
	}

	/**
	 * Muestra la informaci�n en un gr�fico
	 */
	@FXML
	private void grafico() {
		libretaDirecciones.vistaEstadisticas();
	}
	
	/**
	 * Muestra la informaci�n personal en un modal
	 */
	@FXML
	private void about() {
		libretaDirecciones.vistaAbout();
	}
	/**
	 * Guarda la informaci�n en un archivo .pdf
	 * @throws IOException
	 */
	@FXML
	private void pdf() throws IOException {
		libretaDirecciones.crearPDF();
	}
	
	@FXML
	private void elegirES() {
		libretaDirecciones.elegirIdioma("es");
	}
	
	@FXML
	private void elegirEN() {
		libretaDirecciones.elegirIdioma("en");
	}
	
	@FXML
	private void elegirFR() {
		libretaDirecciones.elegirIdioma("fr");
	}
	
	/**
	 * Crea un PDF a partir del reporte jasper almacenado
	 */
	@FXML
	public void generarJasperAll() throws NullPointerException {		
		try {
			LibretaDirecciones.abrirJasperDocuments();
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	

}
