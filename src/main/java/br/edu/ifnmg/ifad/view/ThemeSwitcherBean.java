package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.util.Context;
import br.edu.ifnmg.ifad.util.StringHelper;
import br.edu.ifnmg.ifad.util.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.hibernate.Session;

@ManagedBean
@SessionScoped
public class ThemeSwitcherBean extends AbstractManager {
        
    private Map<String, String> themes;
    
    private String tema;
    private String temaSite;
    
    public List<String> getThemes() {
        return new ArrayList<String>(themes.values());
    }

    public String getTema() {
        temaSite = (String) FacesContext.getCurrentInstance().getApplication().evaluateExpressionGet(FacesContext.getCurrentInstance(), "#{temaSite}", String.class);
        if(!StringHelper.isEmpty(temaSite)){
            return temaSite;
        }
        if(Context.getLogin() != null && !StringHelper.isEmpty(Context.getLogin().getTema())){
            tema = Context.getLogin().getTema();
        } else {
            tema = "bootstrap";
        }
        return tema;
    }
    public void setTema(String tema) {
        this.tema = tema;
    }

    @PostConstruct
    public void init() {
        themes = new TreeMap<String, String>();
        themes.put("Aristo", "aristo");
        themes.put("Black-Tie", "black-tie");
        themes.put("Blitzer", "blitzer");
        themes.put("Bluesky", "bluesky");
        themes.put("Casablanca", "casablanca");
        themes.put("Cupertino", "cupertino");
        themes.put("Dark-Hive", "dark-hive");
        themes.put("Dot-Luv", "dot-luv");
        themes.put("Eggplant", "eggplant");
        themes.put("Excite-Bike", "excite-bike");
        themes.put("Flick", "flick");
        themes.put("Glass-X", "glass-x");
        themes.put("Hot-Sneaks", "hot-sneaks");
        themes.put("Humanity", "humanity");
        themes.put("Le-Frog", "le-frog");
        themes.put("Midnight", "midnight");
        themes.put("Mint-Choc", "mint-choc");
        themes.put("Overcast", "overcast");
        themes.put("Pepper-Grinder", "pepper-grinder");
        themes.put("Redmond", "redmond");
        themes.put("Rocket", "rocket");
        themes.put("Sam", "sam");
        themes.put("Smoothness", "smoothness");
        themes.put("South-Street", "south-street");
        themes.put("Start", "start");
        themes.put("Sunny", "sunny");
        themes.put("Swanky-Purse", "swanky-purse");
        themes.put("Trontastic", "trontastic");
        themes.put("UI-Darkness", "ui-darkness");
        themes.put("UI-Lightness", "ui-lightness");
        themes.put("Vader", "vader");
        
        //Novos
        
        themes.put("Afterdark", "afterdark");
        themes.put("Afternoon", "afternoon");
        themes.put("Afterwork", "afterwork");
        themes.put("Bootstrap", "bootstrap");
        themes.put("Delta", "delta");
        
        themes.put("Cruze", "cruze");
        themes.put("Home", "home");
        
    }
    
    public void saveTheme(String tema){
        this.tema = tema;
        saveTheme();
    }
    
    public void saveTheme() {
        if(Context.getLogin() != null){
            Context.getLogin().setTema(tema);
            try {
                doInTransaction(new PersistenceActionWithoutResult() {
                    @Override
                    public void execute(Session s) throws BusinessException {
                        Context.getLogin().setSession(s);
                        Context.getLogin().save();
                    }
                });
            } catch (BusinessException ex) {
                addMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage());
                Logger.getLogger(ThemeSwitcherBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String getTemaSite() {
        return temaSite;
    }

    public void setTemaSite(String temaSite) {
        this.temaSite = temaSite;
    }
}
                    