package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.util.StringHelper;
import java.io.Serializable;
import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author Danilo Souza Almeida
 */
public class GenericBean implements Serializable {
    
    private State currentState;
    
    enum State {
        SEARCH,
        INSERT,
        EDIT
    }

    public GenericBean() {
        currentState = State.SEARCH;
    }
    
    public void changeStateToSearch(){
        currentState = State.SEARCH;
    }
    public void changeStateToEdit(){
        currentState = State.EDIT;
    }
    public void changeStateToInsert(){
        currentState = State.INSERT;
    }
    
    public boolean isStateEdit(){
        return State.EDIT.equals(currentState);
    }
    public boolean isStateInsert(){
        return State.INSERT.equals(currentState);
    }
    public boolean isStateSearch(){
        return State.SEARCH.equals(currentState);
    }
    public boolean isStateNotEdit(){
        return !State.EDIT.equals(currentState);
    }
    public boolean isStateNotInsert(){
        return !State.INSERT.equals(currentState);
    }
    public boolean isStateNotSearch(){
        return !State.SEARCH.equals(currentState);
    }
    
    public String getCurrentStateName() {
        return currentState.name();
    }
    public State getCurrentState() {
        return currentState;
    }
    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
    
    protected void addMessage(String message) {
        addMessage(null, message, getTitleFromSeverity(FacesMessage.SEVERITY_INFO), FacesMessage.SEVERITY_INFO);
    }

    protected void addMessage(String componentId, String message) {
        addMessage(componentId, message, getTitleFromSeverity(FacesMessage.SEVERITY_INFO), FacesMessage.SEVERITY_INFO);
    }

    protected void addMessage(FacesMessage.Severity severity, String message) {
        addMessage(null, message, getTitleFromSeverity(severity), severity);
    }

    protected void addMessage(String componentId, String message, String sumary, FacesMessage.Severity severity) {
        if(StringHelper.isEmpty(sumary)){
            sumary = getTitleFromSeverity(FacesMessage.SEVERITY_INFO);
        }
        FacesContext.getCurrentInstance().addMessage(componentId, new FacesMessage(severity, sumary, message));
    }

    protected String getMessageForKey(String key) {
        FacesContext ctx = FacesContext.getCurrentInstance();
        ResourceBundle rb = ctx.getApplication().getResourceBundle(ctx, "i18n");
        return rb.getString(key);
    }

    protected String getTitleFromSeverity(FacesMessage.Severity severity){
        if(FacesMessage.SEVERITY_WARN.equals(severity)){
            return "Aviso";
        }
        if(FacesMessage.SEVERITY_INFO.equals(severity)){
            return "Informação";
        }
        if(FacesMessage.SEVERITY_ERROR.equals(severity)){
            return "Erro";
        }
        if(FacesMessage.SEVERITY_FATAL.equals(severity)){
            return "Erro Fatal";
        }
        return "Info";
    }
    
    protected FacesMessage getFacesMessageForKey(String key) {
        return new FacesMessage(getMessageForKey(key));
    }
    
    public FacesMessage.Severity getSeverityInfo(){
        return FacesMessage.SEVERITY_INFO;
    }
    
    public FacesMessage.Severity getSeverityWarn(){
        return FacesMessage.SEVERITY_WARN;
    }
    
    public FacesMessage.Severity getSeverityError(){
        return FacesMessage.SEVERITY_ERROR;
    }
    
    public FacesMessage.Severity getSeverityFatal(){
        return FacesMessage.SEVERITY_FATAL;
    }
}
