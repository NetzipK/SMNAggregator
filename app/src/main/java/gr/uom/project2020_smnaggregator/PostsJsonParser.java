package gr.uom.project2020_smnaggregator;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PostsJsonParser {

    public static final String TAG = "MyAppPostsJsonParser";
    public static final String POST_DATE_LITERAL = "created_at";
    public static final String POST_TEXT_LITERAL = "text";
    public static final String POST_USER_LITERAL = "screen_name";

    public List<Post> parsePostData(String postJsonData) {
        List<Post> postList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(postJsonData);
            JSONArray jsonArray = json.getJSONArray("statuses");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject postJsonObj = jsonArray.getJSONObject(i);
                String postCreatedAt = postJsonObj.getString(POST_DATE_LITERAL);
                String postText = postJsonObj.getString(POST_TEXT_LITERAL);
                String postUserName = postJsonObj.getJSONObject("user").getString(POST_USER_LITERAL);

                Post post = new Post();
                post.setPostBody(postText);
                post.setPostDate(postCreatedAt);
                post.setPostUserName(postUserName);
                post.setPostMedia("Twitter");

                postList.add(post);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in json parsing", e);
        }
        return postList;
    }
}
