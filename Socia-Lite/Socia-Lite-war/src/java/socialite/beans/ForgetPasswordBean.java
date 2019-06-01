/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import socialite.dao.PasswordResetFacade;
import socialite.dao.UserFacade;
import socialite.entity.PasswordReset;
import socialite.entity.User;
import socialite.services.Mail;

/**
 *
 * @author jaysus
 */
@Named(value = "forgetPasswordBean")
@ViewScoped
public class ForgetPasswordBean implements Serializable{

    private boolean error, success;
    private String email;

    private static final String RESET_BASE_URL = "http://localhost:8080/Socia-Lite-war/resetPassword.jsf?resetId=";
    
    @EJB
    private UserFacade userFacade;
    
    @EJB
    private PasswordResetFacade passwordResetFacade;
    
    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @PostConstruct
    public void init() {
        error = success = false;
        email = "";
    }
    
    /**
     * Creates a new instance of forgetPasswordBean
     */
    public ForgetPasswordBean() {
    }
    
    public void trySendMail() {
        User user = userFacade.findByEmail(email);
        if(user == null) {
            error = true;
        } else {
            error = false;
            success = true;
            
            PasswordReset psswdReset = new PasswordReset();
            psswdReset.setUsuario(user);
            
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.HOUR, 1);
            psswdReset.setExpiritionDate(c.getTime());
            
            String resetUrlId = UUID.randomUUID().toString();           
            
            psswdReset.setUrl(resetUrlId);
            
            passwordResetFacade.create(psswdReset);
            
            Mail.sendMail(user.getEmail(), RESET_BASE_URL + resetUrlId, user.getNickname());
        }
    }
}
