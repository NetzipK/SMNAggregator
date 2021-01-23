package gr.uom.project2020_smnaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;
    public static final String TAG = "MyApp";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView tagsListView = findViewById(R.id.hashtagsListView);

        TrendsArrayAdapter trendsArrayAdapter = new TrendsArrayAdapter(this, R.layout.list_tags, new ArrayList<Tag>(), tagsListView);

        GetTrendingTask getTrendingTaskObject = new GetTrendingTask(trendsArrayAdapter, this);

        getTrendingTaskObject.execute(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}