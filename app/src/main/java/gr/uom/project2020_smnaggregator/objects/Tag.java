package gr.uom.project2020_smnaggregator.objects;

public class Tag {
    private String hashtag;

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "hashtag='" + hashtag + '\'' +
                '}';
    }
}
