package util;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class AdaptadorDeFechas extends XmlAdapter<String, LocalDate>{
    
	/**
	 * Parsea la cadena para obtener una fecha LocalDate
	 */
    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }

    /**
     * Parsea una fecha LocalDate para obtener una cadena
     */
    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
    
}