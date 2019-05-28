/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import socialite.dao.UserFacade;
import socialite.entity.User;

/**
 *
 * @author cherra
 */
@Named(value = "userBean")
@Dependent
public class UserBean {

    @EJB
    private UserFacade userFacade;
    
    protected User user;
    
    protected String email="";
    protected String password="";
    protected String nickname="";
    protected String name="";
    protected String surname="";
    
    /**
     * Creates a new instance of UserBean
     */
    public UserBean() {
    }
    
    
    
    public User getUser(){
        return user;
    }
    
    public void setEmail(String email){
        user.setEmail(email);
    }
    
    public void setNickname(String nickname){
        user.setNickname(nickname);
    }
    
    public void setName(String name){
        user.setName(name);
    }
    
    public void setSurname(String surname){
        user.setSurname(surname);
    }
    
    
    public void getEmail(String email){
        user.setEmail(email);
    }
    
    public void getNickname(String nickname){
        user.setNickname(nickname);
    }
    
    public void getName(String name){
        user.setName(name);
    }
    
    public void getSurname(String surname){
        user.setSurname(surname);
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
