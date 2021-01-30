package gr.uom.project2020_smnaggregator.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.httpclient.multipart.FileByteArrayBodyPartPayload;
import com.github.scribejava.core.httpclient.multipart.MultipartPayload;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import gr.uom.project2020_smnaggregator.R;
import gr.uom.project2020_smnaggregator.tasks.TweetTask;

public class TweetWithImagesTask extends AsyncTask<Uri, Void, ArrayList<String>> {
    public static final String TAG = "MyAppTweetWithImagesTask";
    public static final String REMOTE_API = "https://upload.twitter.com/1.1/media/upload.json";

    private Context context;
    private String status;
    private String replyToId;
    private String replyToUser;

    ArrayList<String> imageId = new ArrayList<>();

    public TweetWithImagesTask(Context context, String status) {
        this.context = context;
        this.status = status;
    }

    public TweetWithImagesTask(Context context, String status, String replyToId, String replyToUser) {
        this(context, status);
        this.replyToId = replyToId;
        this.replyToUser = replyToUser;
    }

    @Override
    protected ArrayList<String> doInBackground(Uri... uris) {
        SharedPreferences sharedPref = context.getSharedPreferences("access_token", Context.MODE_PRIVATE);
        String acc_token = sharedPref.getString("access_token", "");
        String token_sec = sharedPref.getString("access_token_secret", "");

        OAuth10aService service = new ServiceBuilder(context.getString(R.string.twitter_consumer_key))
                .apiSecret(context.getString(R.string.twitter_consumer_secret))
                .build(TwitterApi.instance());
        OAuthRequest request = new OAuthRequest(Verb.POST, REMOTE_API);
        OAuth1AccessToken accessToken = new OAuth1AccessToken(acc_token, token_sec);
        service.signRequest(accessToken, request);
        for (int i = 0; i < uris.length; i++) {
            try {
                Log.d(TAG, uris[i].getPath());
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uris[i]);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                try {
                    byteArray = loadFileAsBytesArray(new File(uris[i].getPath()));
                }
                catch (Exception e) {

                }

                final String boundary = "---just a boundary"; //any string, no restriction at all
                final MultipartPayload multipartPayload = new MultipartPayload(boundary);
                multipartPayload.addBodyPart(new FileByteArrayBodyPartPayload(byteArray, "media"));
                request.setMultipartPayload(multipartPayload);

                try {
                    Response response = service.execute(request);
                    Log.d(TAG, response.getCode()+"");
                    if (response.isSuccessful()) {
                        JSONObject json = new JSONObject(response.getBody());
                        imageId.add(json.getString("media_id_string"));
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error happened!", e);
                    e.printStackTrace();
                }
            } catch (IOException e) {
                Log.e(TAG, "Error happened!", e);
                e.printStackTrace();
            }
        }
        return imageId;
    }

    public static byte[] loadFileAsBytesArray(File file) throws Exception
    {
        int length = (int) file.length();
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(file));
        byte[] bytes = new byte[length];
        reader.read(bytes, 0, length);
        reader.close();
        return bytes;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        Log.d(TAG, strings.toString());
        if (imageId.size() > 0) {
            TweetTask tweetTask;
            if (!replyToId.isEmpty()) {
                tweetTask = new TweetTask(context, status, replyToId, replyToUser);
            } else {
                tweetTask = new TweetTask(context, status);
            }
            ArrayList<BigInteger> bigIntegerArrayList = new ArrayList<BigInteger>();
            for (String s :
                    imageId) {
                bigIntegerArrayList.add(new BigInteger(s));
            }
            BigInteger[] bigIntegers = bigIntegerArrayList.toArray(new BigInteger[imageId.size()]);
            tweetTask.execute(bigIntegers);
        }
        super.onPostExecute(strings);
    }
}
