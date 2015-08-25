package br.edu.ifnmg.ifad.entity;

import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.io.Serializable;
import org.hibernate.Session;

/**
 * Interface utilizada para as classes de entidade que podem ser gerenciadas
 * por outra clase, como salvar e deletar
 * @author Danilo Souza Almeida
 */
public abstract class EntityManageable implements Serializable {
    
    private Session session;
    
    public abstract void save() throws BusinessException;
    public abstract void remove() throws BusinessException;
    public abstract void valid() throws BusinessException;

    public Session getSession() {
        return session;
    }
    public void setSession(Session session){
        this.session = session;
    }
}
