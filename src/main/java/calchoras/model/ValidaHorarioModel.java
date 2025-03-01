package calchoras.model;

public class ValidaHorarioModel {
    
    private static final String regex = "^([01]\\d|2[0-3])[0-5]\\d$";     //regex para HHmm (hora: 00-23, minuto: 00-59)
    private static final String regex1 = "^([01]\\d|2[0-3]):[0-5]\\d$";   //regex para HH:mm (hora: 00-23, :, minuto: 00-59)

    public static boolean isHorarioValido(String horario) {
        return horario.matches(ValidaHorarioModel.getRegex()) || horario.matches(ValidaHorarioModel.getRegex1());
    }

    public static String formatarHorario(String horario){
        if(horario.matches(ValidaHorarioModel.getRegex())){
            return horario.substring(0, 2) + ":" + horario.substring(2, 4);
        }
        return horario; 
    }
    
    public static String getRegex(){
       return regex;
    }
   
   public static String getRegex1(){
       return regex1;
   }
}
