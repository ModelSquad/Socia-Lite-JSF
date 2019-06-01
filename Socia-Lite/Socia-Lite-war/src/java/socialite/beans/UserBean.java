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
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import socialite.entity.Media;
import socialite.entity.Post;

/**
 *
 * @author Sevi
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    @Inject
    private LoginSessionBean loginSessionBean;
    private User user;    
    @EJB
    private UserFacade userFacade;    

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
