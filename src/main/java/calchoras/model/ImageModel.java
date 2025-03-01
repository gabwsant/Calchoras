package calchoras.model;

import calchoras.Calchoras;
import java.net.URL;

public class ImageModel {
    
    URL iconUrl;
    
    public ImageModel(){
        iconUrl = Calchoras.class.getClassLoader().getResource("calc_icon.png");
    }
    
    public URL getIconUrl(){
        return iconUrl;
    }
}
