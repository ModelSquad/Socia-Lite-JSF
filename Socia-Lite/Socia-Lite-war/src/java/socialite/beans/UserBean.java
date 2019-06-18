/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.context.FacesContext;
import socialite.dao.UserFacade;
import socialite.entity.User;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import socialite.dao.AssociationFacade;
import socialite.dao.FriendshipRequestFacade;
import socialite.entity.Association;
import socialite.entity.AssociationRequest;
import socialite.entity.FriendshipRequest;
import socialite.entity.Media;

/**
 *
 * @author Sevi
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    @EJB
    private AssociationFacade associationFacade;


    @Inject
    private LoginSessionBean loginSessionBean;
    private User user;    
    @EJB
    private UserFacade userFacade;
    @EJB
    private FriendshipRequestFacade friendshipRequestFacade;
    
    private Part profilePicture;
    private boolean confirmChange;       
    private static final String ACCESS_TOKEN = "3wQ3NmRIRPAAAAAAAAAADR3SEijLf_rodEXbuypIw0ubDuUyjZ-bDPvuA9-qdgEv";

    public UserBean() {        
    }   
    
    @PostConstruct
    public void init () {
        user=loginSessionBean.getLoggedUser();
        confirmChange=false;
    }
    
    public User getUser(){
        return user;
    }
    
    public Part getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Part profilePicture) {
        this.profilePicture = profilePicture;
        confirmChange=true;
      
    }
    
    public boolean isConfirmChange() {
        return confirmChange;
    }

    public void setConfirmChange(boolean confirmChange) {
        this.confirmChange = confirmChange;
    }

    public String changeProfilePicture(){
        try {
            uploadPictures(profilePicture);
            confirmChange=false;
        } catch (IOException|DbxException|ServletException ex) {
                Logger.getLogger(PostsBean.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
        }
        return null;
    }
  
    
    public String saveChanges(){
        userFacade.edit(user);
        loginSessionBean.setLoggedUser(user);
        return null;
    }       
    
    public String deleteBirthplace(){
        user.setBirthPlace(null);
        userFacade.edit(user);
        loginSessionBean.setLoggedUser(user);
        return null;
    }
    
    public String deleteJob(){
        user.setJob(null);
        userFacade.edit(user);
        loginSessionBean.setLoggedUser(user);
        return null;
    }
    
    public String deleteStudyPlace(){
        user.setStudyPlace(null);
        userFacade.edit(user);
        loginSessionBean.setLoggedUser(user);
        return null;
    }
    
    public String deleteWebsite(){
        user.setWebsite(null);
        userFacade.edit(user);
        loginSessionBean.setLoggedUser(user);
        return null;
    }
    
    public List<User> getFindFriends(){
        return userFacade.findNotFriends(user);
    }
    
    public List<Association> getFindGroups(){
        return associationFacade.findNotAssociation(user);
    }
    
    public String deleteFriend(User friend){
        List<User> friends = this.user.getUserList();
        friends.remove(user);
        this.user.setUserList(friends);
        this.user.setUserList1(friends);
        friends = friend.getUserList();
        friends.remove(this.user);
        friend.setUserList(friends);
        friend.setUserList1(friends);
        userFacade.edit(user);
        userFacade.edit(friend);
        this.user = userFacade.find(user);
        return "";
    }
    
    public String deleteGroup(Association association){
        //TODO
        return "";
    }
    
    public String sendFriendshipRequest(User user){
        //TODO
        return "";
    }
    
    public String acceptFriendshipRequest(FriendshipRequest fr){
        User friend = fr.getUserSender();
        List<FriendshipRequest> requestList = this.user.getFriendshipRequestList();
        requestList.remove(fr);
        this.user.setFriendshipRequestList(requestList);
        requestList = friend.getFriendshipRequestList1();
        requestList.remove(fr);
        friend.setFriendshipRequestList1(requestList);
        List<User> friends = this.user.getUserList();
        friends.add(fr.getUserSender());
        this.user.setUserList(friends);
        this.user.setUserList1(friends);
        friends = friend.getUserList();
        friends.add(this.user);
        friend.setUserList(friends);
        friend.setUserList1(friends);
        friendshipRequestFacade.remove(fr);
        userFacade.edit(user);
        userFacade.edit(user);
        this.user = userFacade.find(user);
        return "";
    }
    
    public String denyFriendshipRequest(FriendshipRequest fr){
        User friend = fr.getUserSender();
        friendshipRequestFacade.remove(fr);
        userFacade.edit(user);
        userFacade.edit(user);
        this.user = userFacade.find(user);
        return "";
    }
    
    public String sendGroupRequest(Association association){
        //TODO
        return "";
    }
    
    public String acceptGroupRequest(AssociationRequest associationRequest){
        //TODO
        return "";
    }
    
    public String denyGroupRequest(AssociationRequest assocationRequest){
        //TODO
        return "";
    }
    
    private static Collection<Part> getAllParts(Part part) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getParts().stream().filter(p -> part.getName().equals(p.getName())).collect(Collectors.toList());
    }
    
    private void uploadPictures(Part pictures) throws IOException, DbxException, ServletException {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        
        String filename = loginSessionBean.getLoggedUser().getNickname() + new Date().toString().replaceAll("[ :]", "_");
        List<Media> media = new ArrayList<>();
        List<Part> parts = (List<Part>) getAllParts(pictures);
        
        int maximum = Math.min(parts.size(), 5);
        
        for(int counter = 0; counter < maximum; counter++) {
                Part item = parts.get(counter);
                InputStream inputStream = item.getInputStream();
                String extension = item.getContentType().substring(item.getContentType().lastIndexOf("/") + 1);
                
                FileMetadata metadata = client.files().uploadBuilder("/" + filename + counter + "." + extension)
                .uploadAndFinish(inputStream);
 
                user.setProfilePic(client.sharing().createSharedLinkWithSettings(metadata.getPathLower()).getUrl().replace("?dl=0", "?raw=1"));
                userFacade.edit(user);
                loginSessionBean.setLoggedUser(user);
        }        
    }
    
}
