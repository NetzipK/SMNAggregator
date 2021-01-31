package gr.uom.project2020_smnaggregator.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gr.uom.project2020_smnaggregator.objects.Comment;

public class CommentsJsonParser {
    public static final String TAG = "MyAppCommentsJsonParser";

    private String postId;

    public CommentsJsonParser(String postId) {
        this.postId = postId.substring(6);
    }

    public List<Comment> parseCommentData(String commentJson) {
        List<Comment> commentList = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(commentJson);
            JSONArray jsonArray = json.getJSONArray("statuses");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject commJsonObj = jsonArray.getJSONObject(i);
                String commCreatedAt = commJsonObj.getString("created_at");
                String commText = commJsonObj.getString("text");
                Long commId = commJsonObj.getLong("id");
                String commUserName = commJsonObj.getJSONObject("user").getString("name");
                String commUserScreenName = commJsonObj.getJSONObject("user").getString("screen_name");
                Boolean liked = commJsonObj.getBoolean("favorited");
                Boolean retweeted = commJsonObj.getBoolean("retweeted");
                if (postId.equals(commId.toString()))
                    break;
                Comment comment = new Comment();
                comment.setCommentDate(commCreatedAt);
                comment.setCommentBody(commText);
                comment.setCommentUserName(commUserName);
                comment.setCommentUserScreenName(commUserScreenName);
                comment.setPostId(commId);
                comment.setLiked(liked);
                comment.setRetweeted(retweeted);

                commentList.add(comment);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in json parsing", e);
        }
        return commentList;
    }
}
