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

import java.net.URLEncoder;
import java.util.List;

public class GetPostsTask extends AsyncTask<String, Void, List<Post>> {

    public static final String TAG = "MyAppGetPostsTask";
    public static final String REMOTE_API = "https://api.twitter.com/1.1/search/tweets.json?q=";

    public List<Post> postList;

    private PostsArrayAdapter adapter;
    private Context context;
    private String hashtag;

    public GetPostsTask(PostsArrayAdapter adapter, Context context, String hashtag) {
        this.adapter = adapter;
        this.context = context;
        try {
            this.hashtag = URLEncoder.encode(hashtag, "utf-8");
        } catch (Exception e) {
            this.hashtag = hashtag;
        }
    }

    @Override
    protected List<Post> doInBackground(String... strings) {
        OAuth10aService service = new ServiceBuilder(strings[0])
                .apiSecret(strings[1])
                .build(TwitterApi.instance());
        String postJson = downloadRestData(REMOTE_API, service);
        PostsJsonParser jsonParser = new PostsJsonParser();
        return jsonParser.parsePostData(postJson);
    }

    private String downloadRestData(String remoteUrl, OAuth10aService service) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"twitter\":");
        Log.d(TAG, remoteUrl + hashtag);
        OAuthRequest request = new OAuthRequest(Verb.GET, remoteUrl + hashtag);
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");
        OAuth1AccessToken accessToken = new OAuth1AccessToken(acc_token, token_sec);
        service.signRequest(accessToken, request);
        Log.d(TAG, acc_token + " " + token_sec);
        try {
            Response response = service.execute(request);
            sb.append(response.getBody());
        } catch (Exception e) {
            Log.e(TAG, "Error happened!", e);
        }
        sb.append("},\"instagram\":");
        //todo: Add instagram download data
        sb.append("{}");

        sb.append("}");
        Log.d(TAG, sb.toString());
        return sb.toString();
    }

    @Override
    protected void onPostExecute(List<Post> posts) {
        postList = posts;
        for (Post post :
                postList) {
            Log.i(TAG, post.toString());
        }
        adapter.setPostList(postList);
    }
}
