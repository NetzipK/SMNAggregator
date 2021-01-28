package gr.uom.project2020_smnaggregator;

import java.util.ArrayList;
import java.util.Arrays;

public class Post {
    private String postUserName;
    private String postMedia;
    private String postDate;
    private String postBody;
    private ArrayList<String> postImages = new ArrayList<>();

    public ArrayList<String> getPostImages() {
        return postImages;
    }

    public void setPostImages(ArrayList<String> postImages) {
        this.postImages = postImages;
    }

    public String getPostUserName() {
        return postUserName;
    }

    public String getPostMedia() {
        return postMedia;
    }

    public void setPostMedia(String postMedia) {
        this.postMedia = postMedia;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostBody() {
        return postBody;
    }

    public void setPostBody(String postBody) {
        this.postBody = postBody;
    }

    public void setPostUserName(String postUserName) {
        this.postUserName = postUserName;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postUserName='" + postUserName + '\'' +
                ", postMedia='" + postMedia + '\'' +
                ", postDate='" + postDate + '\'' +
                ", postBody='" + postBody + '\'' +
                ", postImages=" + postImages +
                '}';
    }
}
