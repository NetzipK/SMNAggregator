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

import org.json.JSONObject;

import java.util.List;

public class GetTrendingTask extends AsyncTask<String, Integer, List<Tag>> {

    public static final String TAG = "MyAppGetTrendingTask";
    public static final String REMOTE_API = "https://api.twitter.com/1.1/trends/place.json?id=";
    public static final String ACCOUNT_API = "https://api.twitter.com/1.1/account/settings.json";
    public static final String TEST_API = "https://api.twitter.com/2/tweets/search/recent?query=%23paoofi";

    public List<Tag> tagList;

    private Context context;
    private TrendsArrayAdapter adapter;

    public GetTrendingTask(TrendsArrayAdapter adapter, Context context) {
        this.adapter = adapter;
        this.context = context;
    }

    @Override
    protected List<Tag> doInBackground(String... strings) {
        OAuth10aService service = new ServiceBuilder(strings[0])
                .apiSecret(strings[1])
                .build(TwitterApi.instance());
        String tagJson = downloadRestData(REMOTE_API, service);
        TagsJsonParser jsonParser = new TagsJsonParser();
        return jsonParser.parseTagData(tagJson);
    }

    private String downloadRestData(String remoteUrl, OAuth10aService service)  {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        OAuthRequest request = new OAuthRequest(Verb.GET, remoteUrl + getWoeid(service));
        OAuth1AccessToken accessToken = new OAuth1AccessToken("", "");
        service.signRequest(accessToken, request);
        try {
            Response response = service.execute(request);
            sb.append(response.getBody());
        } catch (Exception e) {
            Log.e(TAG, "Error happened!", e);
        }

//        sb.append("\n");
//        request = new OAuthRequest(Verb.GET, TEST_API);
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
//        Log.d(TAG, sb.toString());
        return sb.toString();
    }

    private String getWoeid(OAuth10aService service) {
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");
        Log.d(TAG, acc_token + "  " + token_sec);
        if (acc_token == "" || token_sec == "") {
            return "23424833";
        }
        OAuthRequest request = new OAuthRequest(Verb.GET, ACCOUNT_API);
        OAuth1AccessToken accessToken = new OAuth1AccessToken(acc_token, token_sec);
        service.signRequest(accessToken, request);
        try {
            Response response = service.execute(request);
            String json = response.getBody();
            Log.d(TAG, json);
            JSONObject jsonObject = new JSONObject(json);
            jsonObject = jsonObject.getJSONArray("trend_location").getJSONObject(0);
            return jsonObject.getString("woeid");
        } catch (Exception e) {
            return "23424833";
        }
    }

    @Override
    protected void onPostExecute(List<Tag> tags) {
        tagList = tags;
        for (Tag tag : tagList)
            Log.i(TAG, tag.toString());
        adapter.setTagList(tagList);
        super.onPostExecute(tags);
    }
}
