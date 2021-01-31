package gr.uom.project2020_smnaggregator.adapters;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import gr.uom.project2020_smnaggregator.AddPostActivity;
import gr.uom.project2020_smnaggregator.R;
import gr.uom.project2020_smnaggregator.ShowPostActivity;
import gr.uom.project2020_smnaggregator.objects.Comment;
import gr.uom.project2020_smnaggregator.tasks.LikeTask;
import gr.uom.project2020_smnaggregator.tasks.RetweetTask;

public class CommentsArrayAdapter extends ArrayAdapter<Comment> {

    public static final String TAG = "MyAppCommentsArrayAdapter";

    private List<Comment> commentList;
    private final LayoutInflater inflater;
    private final int layoutResource;
    private Context context;

    private ListView commentsListView;

    public CommentsArrayAdapter(Context context, int resource, @NonNull ArrayList<Comment> comments, ListView listView) {
        super(context, resource, comments);
        commentList = comments;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        commentsListView = listView;
        this.context = context;
    }

    @Override
    public int getCount() {
        return commentList.size();
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

        Comment currComment = commentList.get(position);

        viewHolder.commentUserName.setText(currComment.getCommentUserName());
        viewHolder.commentUserScreenName.setText(currComment.getCommentUserScreenName());
        viewHolder.commentBody.setText(currComment.getCommentBody());
        viewHolder.commentDate.setText(currComment.getCommentDate());

        return convertView;
    }

    private class ViewHolder {
        final TextView commentUserName;
        final TextView commentUserScreenName;
        final TextView commentBody;
        final TextView commentDate;

        ViewHolder(View view) {
            commentUserName = view.findViewById(R.id.commentUserNameTextView);
            commentUserScreenName = view.findViewById(R.id.commentUserScreenNameTextView);
            commentBody = view.findViewById(R.id.commentBodyTextView);
            commentDate = view.findViewById(R.id.commentDateTextView);
        }
    }

    @Nullable
    @Override
    public Comment getItem(int position) {
        return commentList.get(position);
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
        commentsListView.setAdapter(this);
    }

}
