/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpServletRequest;
import org.mindrot.jbcrypt.BCrypt;
import socialite.dao.PasswordResetFacade;
import socialite.dao.UserFacade;
import socialite.entity.PasswordReset;
import socialite.entity.User;

/**
 *
 * @author jaysus
 */
@Named(value = "resetPasswordBean")
@ViewScoped
public class ResetPasswordBean implements Serializable {

    private boolean error, expired, success;
    private User user;
    private String password, repassword;
    
    @EJB
    private PasswordResetFacade passwordResetFacade; 
    
    @EJB
    private UserFacade userFacade;
    
    
    @PostConstruct
    public void init() {
        HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        
        String resetId = request.getParameter("resetId");
        
        PasswordReset passwordReset = passwordResetFacade.getByUrlId(resetId);
        
        if(passwordReset == null) {
            expired = true;
        } else if(passwordReset.getExpiritionDate().before(new Date())) {
            passwordResetFacade.remove(passwordReset);
            expired = true;
        } else {
            user = passwordReset.getUsuario();
            expired = false;
        }
        
        error = success = false;
        password = repassword = "";
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepassword() {
        return repassword;
    }

    public void setRepassword(String repassword) {
        this.repassword = repassword;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public void doReset() {
        if(password.isEmpty() || !password.equals(repassword)) {
            error = true;
        } else {
            user.setPassword(this.hashPassword(password));
            
            Collection<PasswordReset> passwordResetCollecion = user.getPasswordResetCollection();
            for(PasswordReset pw : passwordResetCollecion) {
                passwordResetFacade.remove(pw);
            }
            passwordResetCollecion.clear();
            user.setPasswordResetCollection(passwordResetCollecion);
            
            userFacade.edit(user);
            
            error = false;
            success = true;
        }
    }
    
    public String hashPassword(String password_plaintext) {
        int workload = 12;
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        return (hashed_password);
    }

}
