package gr.uom.project2020_smnaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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

import java.util.Arrays;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "MyApp";

    CallbackManager callbackManager;

    LoginButton facebookLoginButton;
    TwitterLoginButton twitterLoginButton;
    Button twitterLogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.twitter_consumer_key), getResources().getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_profile);

        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.tw_login_button);
        facebookLoginButton = (LoginButton) findViewById(R.id.fb_login_button);

        twitterLogoutButton = findViewById(R.id.tw_logout_button);

        facebookLoginButton.setPermissions(Arrays.asList("email"));

        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                boolean loggedIn = accessToken != null && !accessToken.isExpired();
                Log.d(TAG, loggedIn ? "Logged In" : "Not Logged In");
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login canceled by user.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Login fail: " + error, Toast.LENGTH_LONG).show();
            }
        });

        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        if (session != null) {
            Log.d(TAG, "Session found with username: " + session.getUserName());
            twitterLogin(session);
        } else {
            twitterLoginButton.setVisibility(View.VISIBLE);
        }

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                twitterLogin(session);

                Log.d(TAG, session.toString());
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(getApplicationContext(), "Login fail: " + exception, Toast.LENGTH_LONG).show();
            }
        });

        twitterLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterCore.getInstance().getSessionManager().clearActiveSession();

                SharedPreferences sharedPreferences = getSharedPreferences("access_token", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("access_token", null);
                editor.putString("access_token_secret", null);
                editor.commit();

                twitterLogoutButton.setVisibility(View.INVISIBLE);
                TextView twtTextView = findViewById(R.id.twtLoggedIn);
                twtTextView.setText("");
                twitterLoginButton.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), "Successfully logged out from twitter.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void twitterLogin(TwitterSession session) {
        TwitterAuthToken authToken = session.getAuthToken();
        SharedPreferences sharedPreferences = getSharedPreferences("access_token", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("access_token", authToken.token);
        editor.putString("access_token_secret", authToken.secret);
        editor.commit();

        twitterLogoutButton.setVisibility(View.VISIBLE);
        TextView twtTextView = findViewById(R.id.twtLoggedIn);
        twitterLoginButton.setVisibility(View.INVISIBLE);
        twtTextView.setText("Logged in on Twitter as " + session.getUserName());

        Log.d(TAG, sharedPreferences.getString("access_token", "NULL") + "  " + sharedPreferences.getString("access_token_secret", "NULL"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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