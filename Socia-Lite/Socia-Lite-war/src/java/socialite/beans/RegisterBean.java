/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import org.mindrot.jbcrypt.BCrypt;
import socialite.dao.UserFacade;
import socialite.entity.User;

/**
 *
 * @author Sevi
 */
@Named(value = "registerBean")
@SessionScoped
public class RegisterBean implements Serializable{
    @EJB
    private UserFacade userFacade;    
    private User user;
    private String repeatPassword;
    private boolean emailTaken;   
    private boolean passwordsDontMatch;
    

    /**
     * Creates a new instance of RegisterBean
     */
    public RegisterBean() {
    }
    
    @PostConstruct
    public void init(){
        user=new User();
        emailTaken=false;
        passwordsDontMatch=false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public boolean isPasswordsDontMatch() {
        return passwordsDontMatch;
    }

    public void setPasswordsDontMatch(boolean passwordsDontMatch) {
        this.passwordsDontMatch = passwordsDontMatch;
    }
    
    
    public void emailIsAvailable(AjaxBehaviorEvent event){
        if(userFacade.findByEmail(user.getEmail()) == null){
            emailTaken=false;
        }else{
            emailTaken=true;
        }    
    }
    
    public void passwordsMatch(AjaxBehaviorEvent event){
        if(user.getPassword().equals(repeatPassword)){
            passwordsDontMatch = false;
        }else{
            passwordsDontMatch = true;
        }
    }

    public boolean isEmailTaken() {
        return emailTaken;
    }

    public void setEmailTaken(boolean emailTaken) {
        this.emailTaken = emailTaken;
    }
    
    public String saveName(){
        userFacade.edit(user);
        //editar el usuario de otros beans de sesion
        return null;
    }
    
    private boolean emailIsAvailable(String email) {
        return userFacade.findByEmail(email) == null;
    }
    private boolean passwordsMatch(){
        return user.getPassword().equals(repeatPassword);
    }
    
    public String hashPassword(String password_plaintext) {
        int workload = 12;
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        return (hashed_password);
    }
    
    public String doRegister(){
        if(!emailIsAvailable(user.getEmail()) || !passwordsMatch() || user.getName()==null || user.getSurname()==null || user.getNickname()==null || user.getBirthDate() == null){
            return null;
        }else{
            user.setPassword(hashPassword(user.getPassword()));
            userFacade.create(user);
            return "prueba";
        }              
    }
    
    
}
