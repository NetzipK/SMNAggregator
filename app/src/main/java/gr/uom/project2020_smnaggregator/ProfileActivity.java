package gr.uom.project2020_smnaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";
    TwitterLoginButton loginButton;
    TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.twitter_consumer_key), getResources().getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        setContentView(R.layout.activity_profile);

        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            Log.d(TAG, "Session found with username: " + session.getUserName());
//            loginButton.setEnabled(false);
            TextView twtTextView = findViewById(R.id.twtLoggedIn);
            twtTextView.setText("Logged in on twitter as " + session.getUserName());
            TwitterAuthToken authToken = session.getAuthToken();
            Log.d(TAG, authToken.toString());
            HttpURLConnection httpConnection = null;
            BufferedReader bufferedReader = null;
            StringBuilder response = new StringBuilder();
            try {
                URL url = new URL("https://api.twitter.com/1.1/trends/place.json?id=23424833");
                httpConnection = (HttpURLConnection) url.openConnection();
                httpConnection.setRequestMethod("GET");

                httpConnection.setRequestProperty("Authorization", "Basic " + authToken.token);
                httpConnection.setRequestProperty("Content-Type",
                        "application/json");
                httpConnection.connect();

                bufferedReader = new BufferedReader(new InputStreamReader(
                        httpConnection.getInputStream()));

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }

                Log.d(TAG,
                        "GET response code: "
                                + String.valueOf(httpConnection
                                .getResponseCode()));
                Log.d(TAG, "JSON response: " + response.toString());
            } catch (Exception e) {
                Log.e(TAG, "GET error: " + Log.getStackTraceString(e));
            } finally {
                if (httpConnection != null) {
                    httpConnection.disconnect();
                }
            }

            Log.d(TAG, response.toString());
        }

        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();

                Log.d(TAG, session.toString());
                loginMethod(session);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "Login fail: " + exception, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loginMethod(TwitterSession session) {
        String usernameS = session.getUserName();
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
        username = findViewById(R.id.usernameTextView);
        username.setText("Hello, " + usernameS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}