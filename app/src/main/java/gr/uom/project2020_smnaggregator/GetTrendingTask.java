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
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.github.scribejava.core.oauth.OAuthService;

public class GetTrendingTask extends AsyncTask<String, Void, String> {

    public static final String TAG = "MyAppGetTrendingTask";
    public static final String REMOTE_API = "https://api.twitter.com/1.1/trends/place.json?id=23424833";
    public static final String POST_API = "https://api.twitter.com/1.1/statuses/destroy/1351255323093458949.json";

    private Context context;
    private TrendsArrayAdapter adapter;
    public GetTrendingTask(TrendsArrayAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String tagJson = downloadRestData(REMOTE_API, strings[0], strings[1]);

        return tagJson;
    }

    private String downloadRestData(String remoteUrl, String key, String secret)  {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        OAuth10aService service = new ServiceBuilder(key)
                .apiSecret(secret)
                .build(TwitterApi.instance());
        OAuthRequest request = new OAuthRequest(Verb.GET, remoteUrl);
        OAuth1AccessToken accessToken = new OAuth1AccessToken("", "");
        service.signRequest(accessToken, request);
        try {
            Response response = service.execute(request);
            sb.append(response.getBody());
        } catch (Exception e) {
            Log.e(TAG, "Error happened!", e);
        }
        sb.append("\n");

//        request = new OAuthRequest(Verb.POST, POST_API);
//        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
//        String acc_token = sharedPref.getString("access_token", "");
//        String token_sec = sharedPref.getString("access_token_secret", "");
//        Log.d(TAG, "LUL " + acc_token + token_sec);
//        accessToken = new OAuth1AccessToken(acc_token, token_sec);
//        service.signRequest(accessToken, request);
//        try {
//            Response response = service.execute(request);
//            sb.append(response.getBody());
//        } catch (Exception e) {
//            Log.e(TAG, "Error happened!", e);
//        }

        return sb.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        Log.d(TAG, s);
    }
}