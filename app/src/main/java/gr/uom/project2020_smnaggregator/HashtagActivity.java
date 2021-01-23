package gr.uom.project2020_smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.widget.TextView;

public class HashtagActivity extends AppCompatActivity {

    private String hashtag;
    private TextView infoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag);

        hashtag = getIntent().getStringExtra("hashtag");
        infoText = findViewById(R.id.infoTextView);
        Log.d("MyApp", hashtag+"");
        String tempString = "Searching Hashtag: " + hashtag;
        SpannableStringBuilder str = new SpannableStringBuilder(tempString);
        str.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 19, tempString.length(), 0);
        infoText.setText(str);
    }
}