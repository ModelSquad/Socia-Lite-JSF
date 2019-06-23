/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Sevi
 */
@Named(value = "signOutBean")
@RequestScoped
public class SignOutBean {

    /**
     * Creates a new instance of SignOutBean
     */
    public SignOutBean() {        
    }
    
    public String signOut(){       
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("LoginSessionBean", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("UserBean", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PostsBean", null);
        return "index";
    }
    
}
