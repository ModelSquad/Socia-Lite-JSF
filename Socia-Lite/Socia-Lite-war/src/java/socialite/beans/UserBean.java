/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import socialite.dao.UserFacade;
import socialite.entity.User;
import javax.faces.event.AjaxBehaviorEvent;

/**
 *
 * @author Sevi
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    @EJB
    private UserFacade userFacade;
    protected User user;    

    public UserBean() {        
    }   
    
    public User getUser(){
        return user;
    }  
    
    @PostConstruct
    public void init () {
        this.user= new User();

    }

    public String doRegister(){
        this.userFacade.create(user);
        return "";
    }    
}
