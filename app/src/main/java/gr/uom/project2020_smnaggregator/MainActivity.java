package gr.uom.project2020_smnaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MyApp";
    private TrendsArrayAdapter trendsArrayAdapter;
    private ListView tagsListView;
    private EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagsListView = findViewById(R.id.hashtagsListView);
        trendsArrayAdapter = new TrendsArrayAdapter(this, R.layout.list_tags, new ArrayList<Tag>(), tagsListView);
        GetTrendingTask getTrendingTaskObject = new GetTrendingTask(trendsArrayAdapter, this);
        getTrendingTaskObject.execute(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret));
        //todo: OnClick on popular hashtags switch to HashtagActivity with EXTRA selected hashtag
        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tag item = (Tag) parent.getItemAtPosition(position);
                startHashtagActivity(item.getHashtag());
            }
        });
        //todo: EditText on click done switch to HastagActivity with EXTRA typed hashtag
        search = findViewById(R.id.searchText);
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startHashtagActivity(v.getText().toString());
                    handled = true;

                }
                return handled;
            }
        });
    }

    public void startHashtagActivity(String hashtag) {
        Intent intent = new Intent(MainActivity.this, HashtagActivity.class);
        intent.putExtra("hashtag", hashtag);
        startActivity(intent);
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