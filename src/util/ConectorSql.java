package util;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import controller.LibretaDirecciones;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Persona;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author jairogarciarincon
 */
public class ConectorSql {
    
	/**
	 * Conexión SQL
	 */
    private static Connection conexion ;

    /**
     * Constructor de la clase
     * @param dbURL URL de la base de datos
     * @param usuario Usuario de la base de datos
     * @param password Password del usuario de la base de datos
     * @throws SQLException Posible excepción causada por la conexión con la base de datos
     */
    public ConectorSql(String dbURL, String usuario, String password) throws SQLException {
        
        conexion = DriverManager.getConnection(dbURL, usuario, password);        
    }

    /**
     * Cierra la conexión con la base de datos
     * @throws SQLException
     */
	public void cerrar() throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

	/**
	 * Devuelve una lista con todas las personas almacenadas en la base de datos
	 * @return
	 * @throws SQLException
	 */
    public List<Persona> getPersonas() throws SQLException{
        
        Statement stmnt = conexion.createStatement();
        ResultSet rs = stmnt.executeQuery("SELECT * FROM personas");

        List<Persona> personas = new ArrayList<>();
        while (rs.next()) {
        	int id = rs.getInt("id");
            String nombre = rs.getString("nombre");
            String apellidos = rs.getString("apellidos");
            String direccion = rs.getString("direccion");
            String ciudad = rs.getString("ciudad");
            int codigoPostal = rs.getInt("codigoPostal");
            Date fechaDeNacimiento = rs.getDate("fechaDeNacimiento");
            Persona persona = new Persona(id, nombre, apellidos, direccion, ciudad, codigoPostal, fechaDeNacimiento.toLocalDate());
            personas.add(persona);

        }
        
        rs.close();
        stmnt.close();
        return personas;

    }
    
    /**
     * Introduce una nueva persona en la base de datos
     * @param personas
     * @throws SQLException
     */
    public static void insertPersona(Persona persona) throws SQLException{
        
        //Preparo el statement
        String query = "INSERT INTO personas (nombre,apellidos,direccion,ciudad,codigoPostal,fechaDeNacimiento) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = conexion.prepareStatement(query);
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellidos());
            ps.setString(3, persona.getDireccion());
            ps.setString(4, persona.getCiudad());
            ps.setInt(5, persona.getCodigoPostal());
            ps.setDate(6, java.sql.Date.valueOf(persona.getFechaDeNacimiento()));
            ps.execute();
            
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    		alerta.setTitle(LibretaDirecciones.resourceBundle.getString("db.insercion.exito"));
    		alerta.setHeaderText(LibretaDirecciones.resourceBundle.getString("db.insercion.exito.texto"));
    		alerta.setContentText(persona.toString());
    		alerta.showAndWait();
    }
    
    /**
     * Introduce una lista de personas en la base de datos, borrando los datos previos que puideran encontrarse en la misma
     * @param personas
     * @throws SQLException
     */
    public void putPersonas(List<Persona> personas) throws SQLException{
        
        Statement stmnt = conexion.createStatement();
        stmnt.executeUpdate("DELETE FROM personas");
        stmnt.close();
        //Preparo el statement
        String query = "INSERT INTO personas (nombre,apellidos,direccion,ciudad,codigoPostal,fechaDeNacimiento) VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = conexion.prepareStatement(query);
        //Añado cada persona
        for (Persona persona : personas){
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellidos());
            ps.setString(3, persona.getDireccion());
            ps.setString(4, persona.getCiudad());
            ps.setInt(5, persona.getCodigoPostal());
            ps.setDate(6, java.sql.Date.valueOf(persona.getFechaDeNacimiento()));
            ps.execute();
        }
        ps.close();
    }
    
    /**
     * Introduce una lista de personas en la base de datos, borrando los datos previos que puideran encontrarse en la misma
     * @param personas
     * @throws SQLException
     */
    public static void updatePersona(Persona persona) {
                
    	try {
            //Preparo el statement
            String query = "UPDATE personas SET nombre = ?, apellidos = ?, direccion = ?, ciudad = ?, codigoPostal = ?, fechaDeNacimiento = ? WHERE id = ?";
            PreparedStatement ps = conexion.prepareStatement(query);
            //Añado cada persona
            ps.setString(1, persona.getNombre());
            ps.setString(2, persona.getApellidos());
            ps.setString(3, persona.getDireccion());
            ps.setString(4, persona.getCiudad());
            ps.setInt(5, persona.getCodigoPostal());
            ps.setDate(6, java.sql.Date.valueOf(persona.getFechaDeNacimiento()));
            ps.setInt(7, persona.getId());
            ps.execute();
            ps.close();
            
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
    		alerta.setTitle(LibretaDirecciones.resourceBundle.getString("db.modificar.exito"));
    		alerta.showAndWait();
    		
    	} catch(SQLException e) {
    		
            Alert alerta = new Alert(Alert.AlertType.ERROR);
    		alerta.setTitle(LibretaDirecciones.resourceBundle.getString("db.modificar.error"));
    		alerta.setHeaderText(LibretaDirecciones.resourceBundle.getString("db.modificar.error.texto"));
    		alerta.setContentText(e.toString());
    		alerta.showAndWait();
    	}

    }
    
    public static void borrarPersona(int id) {
         String query = "DELETE FROM personas WHERE id = ?";
		 PreparedStatement ps;
		try {
			ps = conexion.prepareStatement(query);
			ps.setInt(1, id);
			ps.execute();
			ps.close();
			
            //Muestro alerta
            Alert alerta = new Alert(AlertType.INFORMATION);
            alerta.setTitle(LibretaDirecciones.resourceBundle.getString("db.borrar"));
            alerta.setHeaderText(LibretaDirecciones.resourceBundle.getString("db.borrar.exito"));
            alerta.setContentText(LibretaDirecciones.resourceBundle.getString("db.borrar.exito.texto"));
            alerta.showAndWait();
		} catch (SQLException e) {

			e.printStackTrace();
		}

    }

	public static Connection getConexion() {
		return conexion;
	}

	public static void setConexion(Connection conexion) {
		ConectorSql.conexion = conexion;
	}
    
    
    
}