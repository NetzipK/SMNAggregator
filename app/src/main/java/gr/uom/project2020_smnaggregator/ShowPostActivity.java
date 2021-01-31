package gr.uom.project2020_smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import gr.uom.project2020_smnaggregator.adapters.CommentsArrayAdapter;
import gr.uom.project2020_smnaggregator.objects.Comment;
import gr.uom.project2020_smnaggregator.tasks.GetCommentsTask;
import gr.uom.project2020_smnaggregator.tasks.GetTweetTask;
import gr.uom.project2020_smnaggregator.tasks.LikeTask;
import gr.uom.project2020_smnaggregator.tasks.RetweetTask;

public class ShowPostActivity extends AppCompatActivity {

    private String hashtag;
    private Long postId;

    private GetTweetTask getTweetTask;

    private TextView userName;
    private TextView userScreenName;
    private TextView tweetText;
    private TextView tweetDate;

    private ImageButton likeButton;
    private ImageButton retweetButton;
    private ImageButton commentButton;

    private ListView commentsListView;

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
        commentButton = findViewById(R.id.commentButton);

        commentsListView = findViewById(R.id.commentsListView);

        CommentsArrayAdapter commentsArrayAdapter = new CommentsArrayAdapter(this, R.layout.list_comments, new ArrayList<Comment>(), commentsListView);

        GetCommentsTask getCommentsTask = new GetCommentsTask(this, postId, commentsArrayAdapter);
        getCommentsTask.execute();

        likeButton.setOnClickListener(v -> {
            if (v.getTag() == (Object)false) {
                LikeTask likeTask = new LikeTask(this, postId, true);
                likeTask.execute();
            } else if(v.getTag() == (Object)true) {
                LikeTask likeTask = new LikeTask(this, postId, false);
                likeTask.execute();
            }
        });

        retweetButton.setOnClickListener(v -> {
            if (v.getTag() == (Object)false) {
                RetweetTask retweetTask = new RetweetTask(this, postId, true);
                retweetTask.execute();
            } else if(v.getTag() == (Object)true) {
                RetweetTask retweetTask = new RetweetTask(this, postId, false);
                retweetTask.execute();
            }
        });
    }
}