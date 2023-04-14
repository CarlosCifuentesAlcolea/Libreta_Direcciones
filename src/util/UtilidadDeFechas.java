package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class UtilidadDeFechas {
    
    /**
     * El patrón utilizado para la conversión de fechas
     */
    private static final String FECHA_PATTERN = "dd/MM/yyyy";
    
    /**
     * El formateador de fecha
     */
    private static final DateTimeFormatter FECHA_FORMATTER = DateTimeFormatter.ofPattern(FECHA_PATTERN);
    
    /**
     * Devuelve la fecha de entrada como un string formateado
     * @param fecha Fecha actual parametrizada
     * @return Cadena de texto con la fecha actual
     */
    public static String formato(LocalDate fecha){
        
        if (fecha == null){
            return null;
        }
        return FECHA_FORMATTER.format(fecha);
        
    }
    
    /**
     * Convierte un string en un objeto de tipo LocalDate (o null si no puede convertirlo)
     * @param fecha Fecha actual parametrizada
     * @return Fecha formateada a LocalDate o null de no ser posible la conversión
     */
    public static LocalDate convertir(String fecha) {
        try {
            return FECHA_FORMATTER.parse(fecha, LocalDate::from);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    
    /**
     * Comprueba si un string de fecha es una fecha válida y devuelve 1 o 0.
     * Usamos el método  LocalDate convertir(String fecha) para la comprobación
     * @param fecha
     * @return
     */
    public static boolean fechaValida(String fecha) {
        
        return UtilidadDeFechas.convertir(fecha) != null;
        
    }
}