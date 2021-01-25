package gr.uom.project2020_smnaggregator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PostsArrayAdapter extends ArrayAdapter<Post> {

    private List<Post> postList;
    private final LayoutInflater inflater;
    private final int layoutResource;

    private ListView postListView;

    public PostsArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Post> obj, ListView listView) {
        super(context, resource, obj);
        postList = obj;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        postListView = listView;
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

        ViewHolder(View view) {
            postMedia = view.findViewById(R.id.postMediaTxt);
            postUserName = view.findViewById(R.id.postUserNameTxt);
            postDate = view.findViewById(R.id.postDateTxt);
            postBody = view.findViewById(R.id.postBodyTxt);
        }
    }

    public void setPostList(List<Post> postList) {
        this.postList = postList;
        postListView.setAdapter(this);
    }
}
