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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import socialite.dao.AssociationFacade;
import socialite.dao.MediaFacade;
import socialite.dao.PostFacade;
import socialite.dao.VisibilityFacade;
import socialite.entity.Association;
import socialite.entity.Media;
import socialite.entity.Post;

/**
 *
 * @author jaysus
 */
@Named(value = "AddPostsBean")
@ViewScoped
public class AddPostsBean implements Serializable {

    @EJB
    private AssociationFacade associationFacade;

    private String newPostTitle;
    private String newPostText;
    private String newPostVisibility;
    private Part newPostPictures;
    private List<Post> postList;
    /*
    private Post postSelected;
    private Association associationSelected;
    */

    /*
    public Post getPostSelected() {
        return postSelected;
    }

    public void setPostSelected(Post postSelected) {
        this.postSelected = postSelected;
    }
    */

    @EJB
    private VisibilityFacade visibilityFacade;
    
    @EJB
    private PostFacade postFacade;
    
      @EJB
    private MediaFacade mediaFacade;
    
    private static final String ACCESS_TOKEN = "3wQ3NmRIRPAAAAAAAAAADR3SEijLf_rodEXbuypIw0ubDuUyjZ-bDPvuA9-qdgEv";
    
    @Inject
    private LoginSessionBean loginSessionBean;
    
    @PostConstruct
    public void init() {
        newPostTitle = newPostText = "";
        newPostVisibility = "public";
        newPostPictures = null;
        /*
        if(this.associationSelected==null){
        List<Integer> ids = new ArrayList<>();
        Integer idUser = this.loginSessionBean.getLoggedUser().getIdUser();
        ids.add(idUser);
                this.loginSessionBean.getLoggedUser().getUserList().forEach((u) -> {
                    ids.add(u.getIdUser());
                });
        this.postList = this.postFacade.findPostsByMultipleIds(ids, idUser);
        }
        else{
            this.postList = this.postFacade.findPostsByGroup(this.associationSelected.getIdAssociation());
        }
        */
    }

    /*
    public Association getAssociationSelected() {
        return associationSelected;
    }

    public void setAssociationSelected(Association associationSelected) {
        this.associationSelected = associationSelected;
    }
    */
    
    public String getNewPostTitle() {
        return newPostTitle;
    }

    public void setNewPostTitle(String newPostTitle) {
        this.newPostTitle = newPostTitle;
    }

    public String getNewPostText() {
        return newPostText;
    }

    public void setNewPostText(String newPostText) {
        this.newPostText = newPostText;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public String getNewPostVisibility() {
        return newPostVisibility;
    }

    public void setNewPostVisibility(String newPostVisibility) {
        this.newPostVisibility = newPostVisibility;
    }

    public Part getNewPostPictures() {
        return null;
    }

    public void setNewPostPictures(Part newPostPictures) {
        this.newPostPictures = newPostPictures;
    }
    
    public void submitPost() {        
        Post post = new Post();
        post.setDate(new Date());
        post.setText(newPostText);
        post.setVisibility((newPostVisibility.equalsIgnoreCase("public")) ? visibilityFacade.find(1) : visibilityFacade.find(2));
        post.setUser(loginSessionBean.getLoggedUser());
        post.setTitle(newPostTitle);
        postFacade.create(post);
        if(newPostPictures != null) {
            try {
                uploadPictures(newPostPictures, post);
            } catch (IOException|DbxException|ServletException ex) {
                Logger.getLogger(AddPostsBean.class.getName()).log(Level.SEVERE, null, ex.getLocalizedMessage());
            }
        }
        
        
        newPostTitle = newPostText = "";
        newPostVisibility = "public";
        newPostPictures = null;
        this.init();
    }
    /*
    public void deletePost(Post post){
        this.postFacade.remove(post);
        init();
    }*/
    /*
    public String editPost(Post post){
        this.postSelected = post;
        return "editPost";
    }*/
    
    private static Collection<Part> getAllParts(Part part) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getParts().stream().filter(p -> part.getName().equals(p.getName())).collect(Collectors.toList());
    }
    
    private void uploadPictures(Part pictures, Post post) throws IOException, DbxException, ServletException {
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
                
                Media currentMedia = new Media();
                currentMedia.setMediaUrl(
                client.sharing().createSharedLinkWithSettings(metadata.getPathLower()).getUrl().replace("?dl=0", "?raw=1")
                );
                currentMedia.setPost(post);
                mediaFacade.create(currentMedia);
                media.add(currentMedia);     
        }
        
        post.setMediaList(media);
        postFacade.edit(post);
    }
    /*
    public String chargeGroupFeed(Integer idGroup){
        this.associationSelected = this.associationFacade.find(idGroup);
        this.init();
        return null;
    }*/
    /*
    
    public String reset(){
        this.associationSelected = null;
        this.init();
        return "welcome";
    }*/
}
