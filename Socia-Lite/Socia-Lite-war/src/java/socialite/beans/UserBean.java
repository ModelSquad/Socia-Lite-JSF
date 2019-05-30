/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import socialite.dao.UserFacade;
import socialite.entity.User;

/**
 *
 * @author Sevi
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    @EJB
    private UserFacade userFacade;
    private User user;

    
    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {        
    }  
    
    
    

}
