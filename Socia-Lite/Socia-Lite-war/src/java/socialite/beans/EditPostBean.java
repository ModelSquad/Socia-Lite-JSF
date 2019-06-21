/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import socialite.dao.PostFacade;
import socialite.dao.VisibilityFacade;
import socialite.entity.Post;

/**
 *
 * @author xfja
 */
@Named(value = "editPostBean")
@RequestScoped
public class EditPostBean {

    @EJB
    private VisibilityFacade visibilityFacade;

    @Inject
    AddPostsBean postBean;

    public AddPostsBean getPostBean() {
        return postBean;
    }

    public void setPostBean(AddPostsBean postBean) {
        this.postBean = postBean;
    }

    public PostFacade getPostFacade() {
        return postFacade;
    }

    public void setPostFacade(PostFacade postFacade) {
        this.postFacade = postFacade;
    }

    public Post getPostSelected() {
        return postSelected;
    }

    public void setPostSelected(Post postSelected) {
        this.postSelected = postSelected;
    }
    
    @EJB
    private PostFacade postFacade;
    
    Post postSelected;

    public String getPostVisibility() {
        return postVisibility;
    }

    public void setPostVisibility(String postVisibility) {
        this.postVisibility = postVisibility;
    }
    String postVisibility;

    public EditPostBean() {
    }
    
    @PostConstruct
    public void init(){
        //this.postSelected = this.getPostBean().getPostSelected();
        this.postVisibility = (this.postSelected.getVisibility().getIdVisibility()==1) ? "public" : "private";
    }
    
    public String submitPost(){
        this.postSelected.setVisibility((this.postVisibility.equalsIgnoreCase("public")) ? visibilityFacade.find(1) : visibilityFacade.find(2));
        this.postFacade.edit(postSelected);
        this.getPostBean().init();
        return "welcome";
    }
    
}
