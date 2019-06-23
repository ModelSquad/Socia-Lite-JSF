/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package socialite.beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Inject;
import socialite.dao.AssociationFacade;
import socialite.dao.PostFacade;
import socialite.entity.Association;
import socialite.entity.Post;

/**
 *
 * @author xfja
 */
@Named(value = "postsBean")
@SessionScoped
public class PostsBean implements Serializable {

    @EJB
    private AssociationFacade associationFacade;

    private List<Post> postList;

    private Post postSelected;
    private Association associationSelected;

    public Post getPostSelected() {
        return postSelected;
    }

    public void setPostSelected(Post postSelected) {
        this.postSelected = postSelected;
    }

    @EJB
    private PostFacade postFacade;

    @Inject
    private LoginSessionBean loginSessionBean;

    @PostConstruct
    public void init() {
        if (this.associationSelected == null) {
            List<Integer> ids = new ArrayList<>();
            Integer idUser = this.loginSessionBean.getLoggedUser().getIdUser();
            ids.add(idUser);
            this.loginSessionBean.getLoggedUser().getUserList().forEach((u) -> {
                ids.add(u.getIdUser());
            });
            this.postList = this.postFacade.findPostsByMultipleIds(ids, idUser);
        } else {
            this.postList = this.postFacade.findPostsByGroup(this.associationSelected.getIdAssociation());
        }
    }

    public Association getAssociationSelected() {
        return associationSelected;
    }

    public void setAssociationSelected(Association associationSelected) {
        this.associationSelected = associationSelected;
    }

    public List<Post> getPostList() {
        return postList;
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
    }

    public void deletePost(Post post) {
        this.postFacade.remove(post);
        init();
    }

    public String editPost(Post post) {
        this.postSelected = post;
        return "editPost";
    }

    public String chargeGroupFeed(Integer idGroup) {
        this.associationSelected = this.associationFacade.find(idGroup);
        this.init();
        return "welcome";
    }

    public String reset() {
        this.associationSelected = null;
        this.init();
        return "welcome";
    }
}
