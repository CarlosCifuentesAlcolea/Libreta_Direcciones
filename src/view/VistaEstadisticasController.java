package view;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import model.Persona;

public class VistaEstadisticasController {
    
    @FXML
    private BarChart<String, Integer> graficoBarras;

    @FXML
    private CategoryAxis ejeX;
    
    @FXML
    private NumberAxis ejeY;

    private ObservableList<String> nombreMeses = FXCollections.observableArrayList();

    //Se invoca justo después de que se ha cargado el archivo FXML
    @FXML
    private void initialize() {
    	
    	ejeX = new CategoryAxis();
    	ejeY = new NumberAxis();
        
        //Array de nombre de meses
        String[] meses_es = {"Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Sep","Oct","Nov","Dic"};
        String[] meses_fr = {"Jan","Fev","Mar","Avr","Mai","Jui","Jul","Aôu","Sep","Oct","Nov","Dec"};
        String[] meses_en = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        
        // Establecemos el idioma actual de la aplicación
        String lengua = Locale.getDefault().getLanguage();
        
        // Cambiamos las etiquetas de meses dependiendo del idioma
        switch(lengua) {
        case "es":  //Los convierto a lista obervable
            		nombreMeses.addAll(Arrays.asList(meses_es));
            		//Asigno los nombres de meses en español
            		ejeX.setCategories(nombreMeses);
            		break;
            		
        case "en": 	//Los convierto a lista obervable
    				nombreMeses.addAll(Arrays.asList(meses_en));
					//Asigno los nombres de meses en inglés
					ejeX.setCategories(nombreMeses);
					break;
					
        case "fr":	//Los convierto a lista obervable
    				nombreMeses.addAll(Arrays.asList(meses_fr));
    				//Asigno los nombres de meses en francés
    				ejeX.setCategories(nombreMeses);
    				break;
        	
        }

        
    }

    /**
     * Set mes de cada persona para el eje Y
     * @param personas
     */
    public void setDatosPersonas(List<Persona> personas) {
        
        //Array con cantidad de personas por mes de nacimiento
        int[] numMes = new int[12];
        for (Persona p : personas) {
            int mes = p.getFechaDeNacimiento().getMonthValue() - 1;
            numMes[mes]++;
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        
        // Generamos traducción dinámica de la etiqueta Mes de Nacimiento
        String lengua = Locale.getDefault().getLanguage();
        switch(lengua) {
	        case "es": 	series.setName("Mes de Nacimiento");
	        			break;

	        case "en": series.setName("Birth Month");
	        			break;
	        			
	        case "fr": series.setName("Mois de naissance");
						break;
        }

        for (int i = 0; i < numMes.length; i++)
            series.getData().add(new XYChart.Data<>(nombreMeses.get(i), numMes[i], i));
        

        //Añado la serie al gráfico
        graficoBarras.getData().add(series);
        
    }
    
}