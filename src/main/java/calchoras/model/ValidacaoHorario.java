package calchoras.model;

import java.util.regex.Pattern;


public class ValidacaoHorario {
    
    
    private static final String regex = "^([01]\\d|2[0-3])[0-5]\\d$";     //regex para HHmm (hora: 00-23, minuto: 00-59)
    private static final String regex1 = "^([01]\\d|2[0-3]):[0-5]\\d$";   //regex para HH:mm (hora: 00-23, :, minuto: 00-59)

    
    public static boolean isHorarioValido(String horario) {
        return Pattern.matches(regex, horario) || Pattern.matches(regex1, horario);
    }
   
   public static String getRegex(){
       return regex;
   }
   
   public static String getRegex1(){
       return regex1;
   }
}
