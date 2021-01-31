package gr.uom.project2020_smnaggregator.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import java.math.BigInteger;

import gr.uom.project2020_smnaggregator.R;

public class TweetTask extends AsyncTask<BigInteger, Void, Boolean> {

    public static final String TAG = "MyAppTweetTask";
    public static final String REMOTE_API = "https://api.twitter.com/1.1/statuses/update.json";

    private Context context;
    private String status;
    private String replyToId;
    private String replyToUser;

    public TweetTask(Context context, String status) {
        this.context = context;
        this.status = status;
    }

    public TweetTask(Context context, String status, String replyToId, String replyToUser) {
        this(context, status);
        this.replyToId = replyToId;
        this.replyToUser = replyToUser;
    }

    @Override
    protected Boolean doInBackground(BigInteger... integers) {
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");

        OAuth10aService service = new ServiceBuilder(context.getString(R.string.twitter_consumer_key))
                .apiSecret(context.getString(R.string.twitter_consumer_secret))
                .build(TwitterApi.instance());

        OAuthRequest request = new OAuthRequest(Verb.POST, REMOTE_API);
        OAuth1AccessToken accessToken = new OAuth1AccessToken(acc_token, token_sec);
        if (replyToId != null && replyToUser != null) {
            Log.d(TAG, "It's a comment!");
            request.addParameter("in_reply_to_status_id", replyToId);
            status = replyToUser + " " + status;
        }
        request.addBodyParameter("status", status);
        if (integers.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (BigInteger integer:
                 integers) {
                sb.append(integer + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            Log.d(TAG, sb.toString());
            request.addBodyParameter("media_ids", sb.toString());
        }
        Log.d(TAG, request.getBodyParams().toString());
        service.signRequest(accessToken, request);

        try {
            Response response = service.execute(request);
            if (response.isSuccessful()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean s) {
        if (s) {
            Toast.makeText(context, "Posted to Twitter!", Toast.LENGTH_SHORT).show();
            ((Activity) context).finish();
        }
        else Toast.makeText(context, "Couldn't post to Twitter!", Toast.LENGTH_SHORT).show();

        super.onPostExecute(s);
    }
}
