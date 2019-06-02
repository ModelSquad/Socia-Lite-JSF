/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.faces.context.FacesContext;
import socialite.entity.User;

/**
 *
 * @author jaysus
 */
@Named(value = "loginSessionBean")
@SessionScoped
public class LoginSessionBean implements Serializable {

    private User loggedUser;

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
    
    /**
     * Creates a new instance of LoginSessionBean
     */
    public LoginSessionBean() {
    }
    
    
}
