package gr.uom.project2020_smnaggregator;

public class Post {
    private String postUserName;
    private String postMedia;
    private String postDate;
    private String postBody;

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
                '}';
    }
}
