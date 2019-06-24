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
import socialite.dao.AssociationRequestFacade;
import socialite.dao.UserFacade;
import socialite.entity.Association;
import socialite.entity.AssociationRequest;
import socialite.entity.User;

/**
 *
 * @author cherra
 */
@Named(value = "groupsBean")
@RequestScoped
public class GroupsBean {

    @EJB
    private UserFacade userFacade;

    @EJB
    private AssociationFacade associationFacade;

    @EJB
    private AssociationRequestFacade associationRequestFacade;

    private User user;
    private String name;
    private String description;
    
    @Inject
    private LoginSessionBean loginSessionBean;
    /**
     * Creates a new instance of GroupsBean
     */
    public GroupsBean() {
    }
    
    
    public List<Association> getFindGroups(){
        return associationFacade.findNotAssociation(user);
    }
    
    
    public String sendGroupRequest(Association association){
        AssociationRequest request = new AssociationRequest();
        request.setUserSender(user);
        request.setAssociationReceiver(association);
        request.setDateTime(new java.sql.Date(System.currentTimeMillis()));
        List<AssociationRequest> requests = association.getAssociationRequestList();
        requests.add(request);
        association.setAssociationRequestList(requests);
        requests = this.user.getAssociationRequestList();
        requests.add(request);
        this.user.setAssociationRequestList(requests);
        associationRequestFacade.create(request);
        userFacade.edit(user);
        associationFacade.edit(association);
        loginSessionBean.setLoggedUser(user);
        return "";
    }
    
    public String acceptGroupRequest(AssociationRequest associationRequest){
        Association association = associationRequest.getAssociationReceiver();
        User sender = associationRequest.getUserSender();
        List<User> members = association.getUserList();
        members.add(sender);
        association.setUserList(members);
        List<Association> associations = sender.getAssociationList();
        associations.add(association);
        sender.setAssociationList(associations);
        List<AssociationRequest> requests = association.getAssociationRequestList();
        requests.remove(associationRequest);
        association.setAssociationRequestList(requests);
        requests = sender.getAssociationRequestList();
        requests.remove(associationRequest);
        sender.setAssociationRequestList(requests);
        userFacade.edit(sender);
        associationFacade.edit(association);
        associationRequestFacade.remove(associationRequest);
        this.user = userFacade.find(user.getIdUser());
        loginSessionBean.setLoggedUser(user);
        return "";
    }
    
    public String denyGroupRequest(AssociationRequest associationRequest){
        Association association = associationRequest.getAssociationReceiver();
        User sender = associationRequest.getUserSender();
        List<AssociationRequest> requests = association.getAssociationRequestList();
        requests.remove(associationRequest);
        association.setAssociationRequestList(requests);
        requests = sender.getAssociationRequestList();
        requests.remove(associationRequest);
        sender.setAssociationRequestList(requests);
        associationFacade.edit(association);
        userFacade.edit(sender);
        associationRequestFacade.remove(associationRequest);
        this.user = userFacade.find(user.getIdUser());
        loginSessionBean.setLoggedUser(user);
        return "";
    }
    
    public String deleteGroup(Association group){
     
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
        loginSessionBean.setLoggedUser(user);
        return "groups";
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
        associationFacade.create(group);
        userFacade.edit(user);
        loginSessionBean.setLoggedUser(user);
        return "groups";
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
    public boolean getValidate(){
        return (name != null && name.length() > 0) && (description != null && description.length()>0); 
    }
    
    @PostConstruct
    public void init(){
        this.user = loginSessionBean.getLoggedUser();
    }
}
