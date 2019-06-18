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
@Named(value = "deleteGroupBean")
@RequestScoped
public class DeleteGroupBean {

    @EJB
    private UserFacade userFacade;

    @EJB
    private AssociationFacade associationFacade;

    @Inject
    UserBean userBean;
    
    private User user;
    private int groupId;
    
    /**
     * Creates a new instance of CreateGroupBean
     */
    public DeleteGroupBean() {
    }
       
    public String deleteGroup2(Association group){
     
        if(group.getUserList().size() > 1 && !group.getAdmin().getIdUser().equals(this.user.getIdUser())){
            List<User> members = group.getUserList();
            members.remove(user);
            group.setUserList(members);
            List<Association> associations = user.getAssociationList();
            associations.remove(group);
            user.setAssociationList(associations);
            userFacade.edit(user);
            associationFacade.edit(group);
        } else {
            List<Association> associations = user.getAssociationList();
            associations.remove(group);
            user.setAssociationList(associations);
            associations = user.getAssociationList1();
            associations.remove(group);
            user.setAssociationList1(associations);
            userFacade.edit(user);
            associationFacade.remove(group);
        }
        return "groups";
    }
    
    @PostConstruct
    public void init(){
       this.user = userBean.getUser();
    }
    
}
