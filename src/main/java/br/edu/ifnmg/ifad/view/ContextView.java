package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.entity.Usuario;
import br.edu.ifnmg.ifad.util.Constants;
import br.edu.ifnmg.ifad.util.Context;
import java.util.Calendar;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * @author danilo
 */
@RequestScoped
@ManagedBean
public class ContextView extends GenericBean {

    public String getMensagemLogado() {
        String mensagem = "";
        if (Context.getLogin() != null) {
            int hora = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            if (hora >= 0 && hora < 12) {
                mensagem = "Bom dia ";
            } else if (hora >= 12 && hora < 18) {
                mensagem = "Boa tarde ";
            } else {
                mensagem = "Boa noite ";
            }
            String nome = Context.getLogin().getNome();
            nome = nome.split(" ")[0];
            mensagem += nome;
        }
        return mensagem;
    }

    public Usuario getLogin() {
        return Context.getLogin();
    }

    public void setLogin(Usuario login) {
        Context.setLogin(login);
    }
    
    public void deslogar() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        Context.setLogin(null);
    }
    
    public String getVersion(){
        return Constants.CURRENT_VERSION;
    }
    
}
