package gr.uom.project2020_smnaggregator.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.net.URLEncoder;
import java.util.List;

import gr.uom.project2020_smnaggregator.R;
import gr.uom.project2020_smnaggregator.ShowPostActivity;
import gr.uom.project2020_smnaggregator.adapters.CommentsArrayAdapter;
import gr.uom.project2020_smnaggregator.objects.Comment;
import gr.uom.project2020_smnaggregator.parsers.CommentsJsonParser;

public class GetCommentsTask extends AsyncTask<String, Integer, List<Comment>> {

    public static final String TAG = "MyAppGetCommentsTask";
    public static final String REMOTE_API = "https://api.twitter.com/1.1/search/tweets.json?q=";

    public List<Comment> commentList;

    private Context context;
    private CommentsArrayAdapter adapter;
    private String postId;

    public GetCommentsTask(Context context, Long postId, CommentsArrayAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        try {
            this.postId = URLEncoder.encode("url:"+postId, "utf-8");
        } catch (Exception e) {
            this.postId = postId.toString();
        }
    }

    @Override
    protected List<Comment> doInBackground(String... strings) {
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");

        OAuth10aService service = new ServiceBuilder(context.getString(R.string.twitter_consumer_key))
                .apiSecret(context.getString(R.string.twitter_consumer_secret))
                .build(TwitterApi.instance());
        String commentJson = downloadRestData(REMOTE_API, service);
        CommentsJsonParser jsonParser = new CommentsJsonParser(postId);
        return jsonParser.parseCommentData(commentJson);
    }

    private String downloadRestData(String remoteUrl, OAuth10aService service) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        OAuthRequest request = new OAuthRequest(Verb.GET, remoteUrl + postId);
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");
        OAuth1AccessToken accessToken = new OAuth1AccessToken(acc_token, token_sec);
        service.signRequest(accessToken, request);
        try {
            Response response = service.execute(request);
            sb.append(response.getBody());
        } catch (Exception e) {
            Log.e(TAG, "Error happened!", e);
        }
        Log.d(TAG, sb.toString());
        return sb.toString();
    }

    @Override
    protected void onPostExecute(List<Comment> comments) {
        commentList = comments;
        for (Comment comment :
                commentList) {
            Log.i(TAG, comment.toString());
        }
        adapter.setCommentList(commentList);
        super.onPostExecute(comments);
    }
}
