package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.entity.EntityManageable;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.hibernate.Session;

/**
 *
 * @author Danilo Souza Almeida
 */
public abstract class GenericCrudBean<E extends EntityManageable> extends AbstractManager {
    
    private static final long serialVersionUID = 1L;

    private Class<E> classEntity;
    private E entity;
    private List<E> entitys;
    
    public GenericCrudBean(){
        try {
            entity = getClassEntity().newInstance();
            entitys = new ArrayList<E>();
            changeStateToSearch();
        } catch (InstantiationException ex) {
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void newRegistre(ActionEvent actionEvent){
        try {
            entity = getClassEntity().newInstance();
            changeStateToInsert();
        } catch (InstantiationException ex) {
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void saveWithoutReturnToSearch(ActionEvent actionEvent){
        try {
            save();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Salvo com sucesso!"));
        } catch (BusinessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Desculpe, mas parece que ocorreu ao tentar salvar o objeto!"));
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void save() throws BusinessException, Exception{
            doInTransaction(new PersistenceActionWithoutResult() {
                @Override
                public void execute(Session s) throws BusinessException {
                    entity.setSession(s);
                    entity.save();
                }
            });
            if(entitys == null){
                entitys = new ArrayList<E>();
            }
            if(getEntitys().contains(entity)){
                getEntitys().remove(entity);
            }
            getEntitys().add(0, entity);
            entity = getClassEntity().newInstance();
    }
    
    public void save(ActionEvent actionEvent){
        try {
            save();
            changeStateToSearch();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Informação", "Salvo com sucesso!"));
        } catch (BusinessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Desculpe, mas parece que ocorreu ao tentar salvar o objeto!"));
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void delete(ActionEvent actionEvent){
        try {
            doInTransaction(new PersistenceActionWithoutResult() {
                @Override
                public void execute(Session s) throws BusinessException {
                    entity.setSession(s);
                    entity.remove();
                }
            }); 
            getEntitys().remove(getEntity());
            newRegistre(actionEvent);
            search(actionEvent);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Removido com sucesso!!"));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Desculpe, mas parece que ocorreu um erro na aplicação!"));
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void edit(E entity){
        try {
            this.entity = entity;
            doInTransaction(new PersistenceActionWithoutResult() {
                @Override
                public void execute(Session s) throws BusinessException {
                    s.refresh(GenericCrudBean.this.entity);
                }
            });
            changeStateToEdit();
        } catch (BusinessException ex) {
            Logger.getLogger(GenericCrudBean.class.getName()).log(Level.SEVERE, null, ex);
            addMessage(getSeverityWarn(), ex.getMessage());
        }
    }
    
    public void search(ActionEvent event){
        try {
            if(isStateSearch()){
                entitys = doInTransaction(new PersistenceAction<List<E>>() {
                    @Override
                    public List<E> execute(Session s) throws BusinessException {
                        return s.createCriteria(getClassEntity()).list();
                    }
                });
                if(entitys == null || entitys.isEmpty()){
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Nenhum dado encontrado!"));
                }
            } else {
                    entity = getClassEntity().newInstance();
                    changeStateToSearch();
            }
        } catch (BusinessException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", ex.getMessage()));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Desculpe, mas parece que ocorreu um erro na aplicação!"));
            Logger.getLogger(GenericBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Getters and setters
    public E getEntity() {
        return entity;
    }
    public void setEntity(E entity) {
        this.entity = entity;
    }

    public List<E> getEntitys() {
        return entitys;
    }
    public void setEntitys(List<E> entitys) {
        this.entitys = entitys;
    }

    public Class<E> getClassEntity() {
        classEntity = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return classEntity;
    }
    
}
