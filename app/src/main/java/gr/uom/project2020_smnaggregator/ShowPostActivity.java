package gr.uom.project2020_smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

public class ShowPostActivity extends AppCompatActivity {

    private String hashtag;
    private long postId;

    private GetTweetTask getTweetTask;

    private TextView userName;
    private TextView userScreenName;
    private TextView tweetText;
    private TextView tweetDate;

    private ImageButton likeButton;
    private ImageButton retweetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_post);

        Intent intent = getIntent();
        hashtag = intent.getStringExtra("hashtag");
        postId = intent.getLongExtra("id", 0);

        getTweetTask = new GetTweetTask(this, postId);
        getTweetTask.execute();

        likeButton = findViewById(R.id.likeButton);
        retweetButton = findViewById(R.id.retweetButton);

        likeButton.setOnClickListener(v -> {
            if (v.getTag() == (Object)false) {
                LikeTask likeTask = new LikeTask(this, postId, true);
                likeTask.execute();
            } else if(v.getTag() == (Object)true) {
                LikeTask likeTask = new LikeTask(this, postId, false);
                likeTask.execute();
            }
        });
    }
}