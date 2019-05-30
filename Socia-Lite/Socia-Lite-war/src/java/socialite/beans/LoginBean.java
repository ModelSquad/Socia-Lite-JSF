package socialite.beans;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.mindrot.jbcrypt.BCrypt;
import socialite.dao.UserFacade;
import socialite.entity.User;

@Named(value = "loginBean")
@RequestScoped
public class LoginBean implements Serializable {

    protected boolean errorLogin;
    protected String email, password;

    @Inject
    private LoginSessionBean loginSessionBean;
    
    @PostConstruct
    public void init() {
        email = password = "";
        errorLogin = false;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean getErrorLogin() {
        return errorLogin;
    }
    
    public void setErrorLogin(boolean errorLogin) {
        this.errorLogin = errorLogin;
    }
    
    @EJB
    private UserFacade userFacade;
    
    public String doLogin() {
        if (email == null) {
            throw new RuntimeException("ERROR. Username is null");
        }

        if (password == null) {
            throw new RuntimeException("ERROR. Password is null");
        }
        
        User user = userFacade.findByEmail(email);
        
        if (user == null || !this.checkPassword(password, user.getPassword())) {
            errorLogin = true;
            return null;
        } 
        
        loginSessionBean.setLoggedUser(user);
        
        return "/welcome.xhtml?faces-redirect=true";
    }
    
    private boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;
        System.out.print(stored_hash);
        if (null == stored_hash || !stored_hash.startsWith("$2a")) {
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
        }

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return (password_verified);
    }
}
