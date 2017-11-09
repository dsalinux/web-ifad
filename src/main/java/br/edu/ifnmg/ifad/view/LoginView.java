/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.entity.Usuario;
import br.edu.ifnmg.ifad.util.Context;
import br.edu.ifnmg.ifad.util.StringHelper;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author danilo
 */
@ManagedBean
@SessionScoped
public class LoginView extends AbstractManager {

    private String login;
    private String senha;
    private String repetirSenha;
    private String url;
    private final String DEFAULT_PAGE = "index.jsf";

    public void logar() {
        try {
            if(StringHelper.isEmpty(login) || StringHelper.isEmpty(senha)){
                throw new BusinessException("Por favor informe o Login e a Senha!");
            }
            Usuario usuarioLogado = doInTransaction(new PersistenceAction<Usuario>() {
                @Override
                public Usuario execute(Session s) throws BusinessException {
                    Criteria criteria = s.createCriteria(Usuario.class);
                    criteria.add(Restrictions.eq("login", login));
                    criteria.add(Restrictions.eq("senha", senha));
                    Usuario login = (Usuario) criteria.uniqueResult();
                    if(login == null){
                        throw new BusinessException("Email ou senha inválidos!");
                    }
                    return login;
                }
            });
            senha = null;
            Context.setLogin(usuarioLogado);
        } catch (BusinessException ex) {
            addMessage(FacesMessage.SEVERITY_WARN, ex.getMessage());
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirecionar() {
        try {
            if(StringHelper.isEmpty(url)){
                url = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("retorno");
                if (StringHelper.isEmpty(url)) {
                    url = DEFAULT_PAGE;
                }
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao redirecionar a página!");
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void redirecionarPermissao() {
        url = DEFAULT_PAGE+"?erro_permissao=true";
        redirecionar();
    }

    public void redirecionarLogin() {
        try {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            StringBuilder url = new StringBuilder();
            url.append(request.getRequestURI());
            Set key = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().keySet();
            Collection value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().values();
            if (FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().size() > 0) {
                String k = key.toArray()[0].toString();
                String v = value.toArray()[0].toString();
                url.append("?");
                url.append(k);
                url.append('=');
                url.append(v);
            }
            this.url = url.toString();
            FacesContext.getCurrentInstance().getExternalContext().redirect("login-admin.jsf");
        } catch (IOException ex) {
            try {
                addMessage(FacesMessage.SEVERITY_ERROR, "Erro ao redirecionar!");
                FacesContext.getCurrentInstance().getExternalContext().redirect(DEFAULT_PAGE);
                Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
                Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    public void alterarSenha() {
        try {
            if (StringHelper.isEmpty(senha) || StringHelper.isEmpty(repetirSenha)) {
                addMessage(FacesMessage.SEVERITY_WARN, "Informe e repita a nova senha nos campos!");
                return;
            }
            if (!senha.equals(repetirSenha)) {
                addMessage(FacesMessage.SEVERITY_WARN, "As senhas digitadas nos dois campos estão diferentes! Digite a mesma senha em ambas as caixas!");
            }
            doInTransaction(new PersistenceActionWithoutResult() {

                @Override
                public void execute(Session s) throws BusinessException {
                    Usuario login = Context.getLogin();
                    login.setSenha(senha);
                    login.setAlterarSenha(false);
                    login.setSession(s);
                    login.save();
                    Context.setLogin(login);
                }
            });
        } catch (BusinessException ex) {
            addMessage(FacesMessage.SEVERITY_WARN, ex.getMessage());
            Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isPronto() {
        if (Context.getLogin() != null && !Context.getLogin().getAlterarSenha()) {
            return true;
        }
        return false;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getRepetirSenha() {
        return repetirSenha;
    }

    public void setRepetirSenha(String repetirSenha) {
        this.repetirSenha = repetirSenha;
    }
}
