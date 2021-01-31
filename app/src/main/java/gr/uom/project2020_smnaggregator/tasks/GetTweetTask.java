package gr.uom.project2020_smnaggregator.tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import gr.uom.project2020_smnaggregator.AddPostActivity;
import gr.uom.project2020_smnaggregator.R;

public class GetTweetTask extends AsyncTask<Long, Void, String> {

    public static final String TAG = "MyAppGetTweetTask";
    public static final String REMOTE_API = "https://api.twitter.com/1.1/statuses/show.json?id=";

    private Context context;
    private Long postId;

    public GetTweetTask(Context context, Long postId) {
        this.context = context;
        this.postId = postId;
    }

    @Override
    protected String doInBackground(Long... longs) {
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");

        OAuth10aService service = new ServiceBuilder(context.getString(R.string.twitter_consumer_key))
                .apiSecret(context.getString(R.string.twitter_consumer_secret))
                .build(TwitterApi.instance());

        OAuthRequest request = new OAuthRequest(Verb.GET, REMOTE_API + postId);
        OAuth1AccessToken accessToken = new OAuth1AccessToken(acc_token, token_sec);

        StringBuilder sb = new StringBuilder();
        sb.append("");
        service.signRequest(accessToken, request);
        try {
            Response response = service.execute(request);
            sb.append(response.getBody());
            Log.d(TAG, response.getBody());
        } catch (Exception e) {
            Log.e(TAG, "Error happened!", e);
        }
        return sb.toString();
    }

    @Override
    protected void onPostExecute(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            String tweetDate = jsonObj.getString("created_at");
            String tweetText = jsonObj.getString("text");
            String userName = jsonObj.getJSONObject("user").getString("name");
            String userScreenName = jsonObj.getJSONObject("user").getString("screen_name");

            ArrayList<String> postImagesArray = new ArrayList<>();
            if (jsonObj.getJSONObject("entities").has("media")) {
                JSONArray postImages = jsonObj.getJSONObject("entities").getJSONArray("media");
                for (int j = 0; j < postImages.length(); j++) {
                    String postImageUrl = postImages.getJSONObject(j).getString("media_url_https");
                    postImagesArray.add(postImageUrl);
                }
            }


            Activity activity = (Activity) context;
            TextView textView;

            textView = activity.findViewById(R.id.tweetDateTextView);
            textView.setText(tweetDate);

            textView = activity.findViewById(R.id.tweetTextView);
            textView.setText(tweetText);

            textView = activity.findViewById(R.id.userNameTextView);
            textView.setText(userName);

            textView = activity.findViewById(R.id.userScreenNameTextView);
            textView.setText("@"+userScreenName);

            Boolean liked = jsonObj.getBoolean("favorited");
            Boolean retweeted = jsonObj.getBoolean("retweeted");

            if (liked) {
                ImageButton likeBtn = activity.findViewById(R.id.likeButton);
                likeBtn.setImageResource(R.drawable.ic_favorited);
                likeBtn.setTag(true);
            } else {
                ImageButton likeBtn = activity.findViewById(R.id.likeButton);
                likeBtn.setImageResource(R.drawable.ic_favorite);
                likeBtn.setTag(false);
            }

            if (retweeted) {
                ImageButton retweetBtn = activity.findViewById(R.id.retweetButton);
                retweetBtn.setImageResource(R.drawable.ic_retweet);
                retweetBtn.setTag(true);
            } else {
                ImageButton retweetBtn = activity.findViewById(R.id.retweetButton);
                retweetBtn.setImageResource(R.drawable.ic_retweet_non);
                retweetBtn.setTag(false);
            }
            // todo: check if verified
            ImageButton commentButton = activity.findViewById(R.id.commentButton);

            commentButton.setOnClickListener(v -> {
                Intent intentNewPost = new Intent(context, AddPostActivity.class);
                intentNewPost.putExtra("replyId", postId.toString());
                intentNewPost.putExtra("replyUser", "@"+userScreenName);
                context.startActivity(intentNewPost);
            });

            if (postImagesArray.size() > 0) {
                LinearLayout imagesLinearLayout = activity.findViewById(R.id.imagesLinearLayout);
                imagesLinearLayout.removeAllViews();
                ScrollView.LayoutParams lp = new ScrollView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0, 16, 0, 0);
                imagesLinearLayout.setLayoutParams(lp);

                for (String imgUrl :
                        postImagesArray) {
                    ImageView imgView = new ImageView(context);
                    imagesLinearLayout.addView(imgView);
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    lp2.setMargins(2, 0, 0, 0);
                    imgView.setLayoutParams(lp2);
                    Picasso.with(context).load(imgUrl).resize(0, 600).into(imgView);
                }
            }

        } catch (Exception e) {
            Log.e(TAG, "Error on json parsing!", e);
        }


        super.onPostExecute(json);
    }
}
