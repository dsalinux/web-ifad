package br.edu.ifnmg.ifad.view;

import br.edu.ifnmg.ifad.util.MD5Util;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author danilo
 */
@ManagedBean
@ApplicationScoped
public class GravatarView {
    
    public String getGravatarUrlMiniImage(String email){
        if(email != null){
            email = MD5Util.md5Hex(email);
        }
        StringBuilder url = new StringBuilder();
        url.append("http://www.gravatar.com/avatar/").append(email).append("?s=30&d=mm");
        return url.toString();
    }
    
    public String getGravatarUrlImage(String email, int size){
        if(email != null){
            email = MD5Util.md5Hex(email);
        }
        StringBuilder url = new StringBuilder();
        url.append("http://www.gravatar.com/avatar/").append(email).append("?s="+size+"&d=mm");
        return url.toString();
    }
    
}
