package br.edu.ifnmg.ifad.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author DANILO
 */
public class PropertiesUtil {
    

    public void creatProperties(){
        
    }
    
    
    public Properties getProperties(File file) throws IOException{
        Properties properties = new Properties();
        if(file != null && file.exists()){
            properties.load(new FileInputStream(file));
        }
        return properties;
    }
    
}
