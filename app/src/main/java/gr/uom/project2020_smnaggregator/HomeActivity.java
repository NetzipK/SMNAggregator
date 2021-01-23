package gr.uom.project2020_smnaggregator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView name;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = getIntent().getStringExtra("username");
        name = findViewById(R.id.nameTextView);
        name.setText(user);
    }
}