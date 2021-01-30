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

import gr.uom.project2020_smnaggregator.R;
import gr.uom.project2020_smnaggregator.tasks.GetTweetTask;

public class LikeTask extends AsyncTask<Void, Void, Boolean> {

    public static final String TAG = "MyAppLikeTask";
    public static final String REMOTE_API = "https://api.twitter.com/1.1/favorites/";

    private Context context;
    private Long postId;
    private Boolean toLike;

    public LikeTask(Context context, long postId, boolean b) {
        this.context = context;
        this.postId = postId;
        toLike = b;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");
        OAuth10aService service = new ServiceBuilder(context.getString(R.string.twitter_consumer_key))
                .apiSecret(context.getString(R.string.twitter_consumer_secret))
                .build(TwitterApi.instance());

        OAuthRequest request = new OAuthRequest(Verb.POST, REMOTE_API + (toLike ? "create.json" : "destroy.json") + "?id="+postId);
        OAuth1AccessToken accessToken = new OAuth1AccessToken(acc_token, token_sec);
        service.signRequest(accessToken, request);
        Log.d(TAG, postId+"");
        try {
            Response response = service.execute(request);
            Log.d(TAG, response.getBody());
            if (response.isSuccessful()) {
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error happened!", e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        GetTweetTask tweetTask = new GetTweetTask(context, postId);
        tweetTask.execute();
        super.onPostExecute(aBoolean);
    }
}
