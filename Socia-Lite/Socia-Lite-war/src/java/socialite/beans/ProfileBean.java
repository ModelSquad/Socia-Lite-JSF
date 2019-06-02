/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.event.AjaxBehaviorEvent;
import socialite.dao.UserFacade;
import socialite.entity.User;

/**
 *
 * @author Sevi
 */
@Named(value = "profileBean")
@SessionScoped
public class ProfileBean implements Serializable{
    private User user;
    private String searchedUserSize;
    private String searchUser;
    private List<User> searchedUser;
    @EJB
    private UserFacade userFacade;
    /**
     * Creates a new instance of ProfileBean
     */
    public ProfileBean() {
    }
    
    @PostConstruct
    public void init(){
        searchedUser= new ArrayList<>();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
       public String getSearchUser() {
        return searchUser;
    }

    public List<User> getSearchedUser() {
        return searchedUser;
    }

    public void setSearchedUser(List<User> searchedUser) {
        this.searchedUser = searchedUser;
    }
    

    public void setSearchUser(String searchUser) {
        this.searchUser = searchUser;
    }
    
    public String getSearchedUserSize() {
        return String.valueOf(searchedUser.size()>0 ? searchedUser.size()-1 : 0);
    }

    public void setSearchedUserSize(String searchedUserSize) {
        this.searchedUserSize = searchedUserSize;
    }
    public void searchByNickname(AjaxBehaviorEvent event){
        if (searchUser != null && !searchUser.isEmpty()) {
            searchedUser=userFacade.findByNickname(searchUser);
        }
        
    }
    
    public String enterSearch(){
        if(searchUser!=null && !searchUser.isEmpty()){
            List<User> lista = userFacade.findByNickname(searchUser);
            if(!lista.isEmpty()){
                user=lista.get(0);
                searchUser=null;
                return "profile";
            }else{
                return null;
            }
        }else{
            return null;
        }
        
    }
    
    
}
