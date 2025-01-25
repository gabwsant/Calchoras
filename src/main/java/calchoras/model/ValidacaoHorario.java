package calchoras.model;

import java.util.regex.Pattern;


public class ValidacaoHorario {
    
    // Regex para HHMM (hora: 00-23, minuto: 00-59)
    static String regex = "^([01]\\d|2[0-3])[0-5]\\d$";
    
    public static boolean isHorarioValido(String horario) {
        return Pattern.matches(regex, horario);
    }
    
   public static String getRegex(){
       return regex;
   }
    
}
