package gr.uom.project2020_smnaggregator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import gr.uom.project2020_smnaggregator.objects.Post;
import gr.uom.project2020_smnaggregator.R;

public class PostsArrayAdapter extends ArrayAdapter<Post> {

    private List<Post> postList;
    private final LayoutInflater inflater;
    private final int layoutResource;
    private Context context;

    private ListView postListView;

    public PostsArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Post> obj, ListView listView) {
        super(context, resource, obj);
        postList = obj;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        postListView = listView;
        this.context = context;
    }
    @Override
    public int getCount() {
        return postList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else
            viewHolder = (ViewHolder) convertView.getTag();

        Post currentPost = postList.get(position);
        viewHolder.linearLayout.removeAllViews();
        int imgSize = currentPost.getPostImages().size();
        ArrayList<String> postImages = currentPost.getPostImages();
        if (imgSize > 0) {
            for (String imgUrl : postImages) {
                ImageView imgView = new ImageView(context);
                viewHolder.linearLayout.addView(imgView);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(16, 0, 0, 0);
                imgView.setLayoutParams(lp);
                Picasso.with(context).load(imgUrl).resize(0, 350).into(imgView);
            }
        }

        viewHolder.postMedia.setText(currentPost.getPostMedia());
        viewHolder.postUserName.setText(currentPost.getPostUserName());
        viewHolder.postDate.setText(currentPost.getPostDate());
        viewHolder.postBody.setText(currentPost.getPostBody());


        return convertView;
    }

    private class ViewHolder {
        final TextView postMedia;
        final TextView postUserName;
        final TextView postDate;
        final TextView postBody;
        final LinearLayout linearLayout;

        ViewHolder(View view) {
            postMedia = view.findViewById(R.id.postMediaTxt);
            postUserName = view.findViewById(R.id.postUserNameTxt);
            postDate = view.findViewById(R.id.postDateTxt);
            postBody = view.findViewById(R.id.postBodyTxt);
            linearLayout = view.findViewById(R.id.imageLayout);
        }
    }

    @Nullable
    @Override
    public Post getItem(int position) {
        return postList.get(position);
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        postListView.setAdapter(this);
    }
}
