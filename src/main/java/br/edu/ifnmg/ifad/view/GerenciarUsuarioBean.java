package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.entity.Usuario;
import br.edu.ifnmg.ifad.util.StringHelper;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

@ManagedBean
@SessionScoped
public class GerenciarUsuarioBean extends GenericCrudBean<Usuario> {
 
    private String senha;

    @Override
    public void save(ActionEvent actionEvent) {
        if(!StringHelper.isEmpty(senha)){
            getEntity().setSenha(senha);
        }
        super.save(actionEvent);
    }
    
    public void aplicarEmailToLogin(){
        if(StringHelper.isEmpty(getEntity().getLogin())){
           getEntity().setLogin(getEntity().getEmail());
        }
    }
    
    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
