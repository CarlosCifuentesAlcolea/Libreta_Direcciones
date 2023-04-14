package model;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import util.AdaptadorDeFechas;

public class Persona {
    
	private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty apellidos;
    private final StringProperty direccion;
    private final StringProperty ciudad;
    private final IntegerProperty codigoPostal;
    private final ObjectProperty<LocalDate> fechaDeNacimiento;
    
    public Persona() {
    	 this(0, null, null, null, null, 0, null);
    }
    
    public Persona(String nombre, String apellidos) {
        this.id = new SimpleIntegerProperty(0);
        this.nombre = new SimpleStringProperty(nombre);
        this.apellidos = new SimpleStringProperty(apellidos);

        this.direccion = new SimpleStringProperty("Mi dirección");
        this.ciudad = new SimpleStringProperty("some city");
        this.codigoPostal = new SimpleIntegerProperty(28440);
        this.fechaDeNacimiento = new SimpleObjectProperty<LocalDate>(LocalDate.of(1974, 6, 15));
        
    }

    public Persona(int id, String nombre2, String apellidos2, String direccion2, String ciudad2, int codigoPostal2,
			LocalDate localDate) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre2);
        this.apellidos = new SimpleStringProperty(apellidos2);

        this.direccion = new SimpleStringProperty(direccion2);
        this.ciudad = new SimpleStringProperty(ciudad2);
        this.codigoPostal = new SimpleIntegerProperty(codigoPostal2);
        this.fechaDeNacimiento = new SimpleObjectProperty<LocalDate>(localDate);
	}

	public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos.get();
    }

    public void setApellidos(String apellidos) {
        this.apellidos.set(apellidos);
    }

    public StringProperty apellidosProperty() {
        return apellidos;
    }

    public String getDireccion() {
        return direccion.get();
    }

    public void setDireccion(String direccion) {
        this.direccion.set(direccion);
    }

    public StringProperty direccionProperty() {
        return direccion;
    }
    
    public String getCiudad() {
        return ciudad.get();
    }

    public void setCiudad(String ciudad) {
        this.ciudad.set(ciudad);
    }

    public StringProperty ciudadProperty() {
        return ciudad;
    }

    public int getCodigoPostal() {
        return codigoPostal.get();
    }

    public void setCodigoPostal(int codigoPostal) {
        this.codigoPostal.set(codigoPostal);
    }

    public IntegerProperty codigoPostalProperty() {
        return codigoPostal;
    }

    @XmlJavaTypeAdapter(AdaptadorDeFechas.class)
    public LocalDate getFechaDeNacimiento() {
        return (LocalDate) fechaDeNacimiento.get();
    }

    public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {
        this.fechaDeNacimiento.set(fechaDeNacimiento);
    }

    public ObjectProperty<LocalDate> fechaDeNacimientoProperty() {
        return fechaDeNacimiento;
    }

	public IntegerProperty getIdProperty() {
		return id;
	}
	
	public int getId() {
		return id.get();
	}
	
	public String toString() {
		return "Nombre: " + getNombre() + " | Apellidos: " + getApellidos() + "\nDirección: " + getDireccion() 
				+ " | Ciudad: " + getCiudad() + " | CP: " + getCodigoPostal() + "\nFecha de Nacimiento: " + getFechaDeNacimiento();
	}

}
