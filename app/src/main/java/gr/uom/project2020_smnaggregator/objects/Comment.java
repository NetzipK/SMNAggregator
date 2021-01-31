package gr.uom.project2020_smnaggregator.objects;

public class Comment {
    private String commentUserName;
    private String commentUserScreenName;
    private String commentBody;
    private String commentDate;
    private Long postId;
    private Boolean liked;
    private Boolean retweeted;

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public Boolean getRetweeted() {
        return retweeted;
    }

    public void setRetweeted(Boolean retweeted) {
        this.retweeted = retweeted;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserScreenName() {
        return commentUserScreenName;
    }

    public void setCommentUserScreenName(String commentUserScreenName) {
        this.commentUserScreenName = commentUserScreenName;
    }

    public String getCommentBody() {
        return commentBody;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentUserName='" + commentUserName + '\'' +
                ", commentUserScreenName='" + commentUserScreenName + '\'' +
                ", commentBody='" + commentBody + '\'' +
                ", commentDate='" + commentDate + '\'' +
                ", postId=" + postId +
                '}';
    }
}
