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

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

public class TrendsArrayAdapter extends ArrayAdapter<Tag> {

    private List<Tag> tagList;
    private final LayoutInflater inflater;
    private final int layoutResource;

    private ListView tagListView;

    public TrendsArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Tag> obj, ListView listView) {
        super(context, resource, obj);
        tagList = obj;
        layoutResource = resource;
        inflater = LayoutInflater.from(context);
        tagListView = listView;
    }

    @Override
    public int getCount() {
        return tagList.size();
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

        Tag currentTag = tagList.get(position);

        viewHolder.hashtag.setText(currentTag.getHashtag());

        return convertView;
    }

    private class ViewHolder {
        public TextView hashtag;

        ViewHolder(View view) {
            hashtag = view.findViewById(R.id.hashtagText);
        }
    }

    public void setTagList(List<Tag> tagList) {
        this.tagList = tagList;
        tagListView.setAdapter(this);
    }
}
