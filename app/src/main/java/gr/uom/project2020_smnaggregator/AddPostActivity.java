package gr.uom.project2020_smnaggregator;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AddPostActivity extends AppCompatActivity {

    public static final int PICK_IMAGE = 0;
    public static final String TAG = "MyAppNewPost";

    private Button selectImageButton;
    private Button submitButton;
    private TextView noImageTextView;
    private LinearLayout linearLayout;
    private EditText postBody;
    private Boolean isComment;

    ArrayList<Uri> selectedImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        Intent intent = getIntent();
        isComment = intent.getStringExtra("replyId") != null && intent.getStringExtra("replyUser") != null;

        selectImageButton = findViewById(R.id.selectImageButton);
        submitButton = findViewById(R.id.submitButton);
        noImageTextView = findViewById(R.id.noImageTextView);
        linearLayout = findViewById(R.id.imagesLinearLayout);
        postBody = findViewById(R.id.postBodyEditText);

        if (isComment) {
            findViewById(R.id.textView4).setVisibility(View.INVISIBLE);
            findViewById(R.id.smLinearLayout).setVisibility(View.INVISIBLE);
        }

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                getIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {camIntent});
                startActivityForResult(chooserIntent, PICK_IMAGE);
            }
        });

        submitButton.setOnClickListener(v -> {
            if (postBody.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please enter a body first!", Toast.LENGTH_SHORT).show();
            } else {
                beginPublishing();
            }
        });


    }

    private void beginPublishing() {
        Switch twitterSwitch = findViewById(R.id.twitterSwitch);
        Switch instagramSwitch = findViewById(R.id.instagramSwitch);
        Switch facebookSwitch = findViewById(R.id.facebookSwitch);

        if (isComment) {
            if (!selectedImages.isEmpty()) {
                TweetWithImagesTask tweetWithImagesTask = new TweetWithImagesTask(this, postBody.getText().toString(), getIntent().getStringExtra("replyId"), getIntent().getStringExtra("replyUser"));
                Uri[] uris = selectedImages.toArray(new Uri[selectedImages.size()]);
                tweetWithImagesTask.execute(uris);
            } else {
                TweetTask tweetTask = new TweetTask(this, postBody.getText().toString(),  getIntent().getStringExtra("replyId"), getIntent().getStringExtra("replyUser"));
                tweetTask.execute();
            }
        } else {
            if (facebookSwitch.isChecked()) {
                SharePhoto.Builder builder = new SharePhoto.Builder();
                builder.setCaption("Test!");

                ShareDialog shareDialog = new ShareDialog(this);
            }

            if (twitterSwitch.isChecked()) {
                if (!selectedImages.isEmpty()) {
                    TweetWithImagesTask tweetWithImagesTask = new TweetWithImagesTask(this, postBody.getText().toString());
                    Uri[] uris = selectedImages.toArray(new Uri[selectedImages.size()]);
                    tweetWithImagesTask.execute(uris);
                } else {
                    TweetTask tweetTask = new TweetTask(this, postBody.getText().toString());
                    tweetTask.execute();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                if (data.getData() != null) {
                    Log.d(TAG, data.toString() + "");
                    selectedImages.add(data.getData());
                    addNewImageToLayout(data.getData());
                } else if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        selectedImages.add(item.getUri());
                        addNewImageToLayout(item.getUri());
                    }
                }
            } else {
                Log.d(TAG, "You haven't picked any image!");
            }
        } catch (Exception e) {
            Log.e(TAG, "Something went wrong", e);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addNewImageToLayout(Uri uri) {
        ImageView imgView = new ImageView(this);
        linearLayout.addView(imgView);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 20, 0);
        imgView.setLayoutParams(lp);
        Picasso.with(this).load(uri).resize(0, 360).into(imgView);

    }
}