/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.ifad.util;

import br.edu.ifnmg.ifad.entity.Usuario;
import javax.faces.context.FacesContext;

/**
 *
 * @author danilo
 */
public class Context {
    
    public static final String USUARIO_LOGADO = "usuario_logado";
    
    public static Usuario getLogin(){
        Object l = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(USUARIO_LOGADO);
        if(l == null){
            return null;
        }
        Usuario login = (Usuario) l;
        return login;
    }
    public static void setLogin(Usuario login){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(USUARIO_LOGADO, login);
    }
    
}
