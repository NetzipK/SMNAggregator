package gr.uom.project2020_smnaggregator;

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

public class RetweetTask extends AsyncTask<Void, Void, Boolean> {

    public static final String TAG = "MyAppRetweetTask";
    public static final String REMOTE_API = "https://api.twitter.com/1.1/statuses/";

    private Context context;
    private Long postId;
    private Boolean toRetweet;

    public RetweetTask(Context context, long postId, boolean b) {
        this.context = context;
        this.postId = postId;
        toRetweet = b;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");
        OAuth10aService service = new ServiceBuilder(context.getString(R.string.twitter_consumer_key))
                .apiSecret(context.getString(R.string.twitter_consumer_secret))
                .build(TwitterApi.instance());

        OAuthRequest request = new OAuthRequest(Verb.POST, REMOTE_API + (toRetweet ? "retweet" : "unretweet") + "/"+postId+".json");
        OAuth1AccessToken accessToken = new OAuth1AccessToken(acc_token, token_sec);
        service.signRequest(accessToken, request);
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
