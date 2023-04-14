package model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "personas") //Define el nombre del elemento raíz XML
public class Empaquetador {
    
	/**
	 * Lista de personas
	 */
    private List<Persona> personas;
    
    /**
     * Elemento persona inserto en la raiz personas
     * @return
     */
    @XmlElement(name = "persona") //Opcional para el elemento especificado
    public List<Persona> getPersonas(){
        return personas;
    }
    
    /**
     * Modifica la lista de personas
     * @param personas
     */
    public void setPersonas(List<Persona> personas){
        this.personas = personas;
    }
    
}
