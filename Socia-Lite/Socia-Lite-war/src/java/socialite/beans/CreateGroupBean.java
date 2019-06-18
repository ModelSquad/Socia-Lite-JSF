/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import socialite.dao.AssociationFacade;
import socialite.dao.UserFacade;
import socialite.entity.Association;
import socialite.entity.User;

/**
 *
 * @author cherra
 */
@Named(value = "createGroupBean")
@RequestScoped
public class CreateGroupBean {

    @EJB
    private UserFacade userFacade;

    @EJB
    private AssociationFacade associationFacade;

    @Inject
    UserBean userBean;
    
    private User user;
    private String name;
    private String description;
    
    /**
     * Creates a new instance of CreateGroupBean
     */
    public CreateGroupBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
       
    public String createGroup(){
        Association group = new Association();
        group.setAdmin(user);
        List<User> members = new ArrayList<User>();
        members.add(user);
        group.setUserList(members);
        group.setName(name);
        group.setDescription(description);
        List<Association> associations = user.getAssociationList();
        associations.add(group);
        user.setAssociationList(associations);
        associations = user.getAssociationList1();
        associations.add(group);
        user.setAssociationList1(associations);
        userFacade.edit(user);
        associationFacade.create(group);
        return "groups";
    }
    
    @PostConstruct
    public void init(){
       this.user = userBean.getUser();
    }
    
}
