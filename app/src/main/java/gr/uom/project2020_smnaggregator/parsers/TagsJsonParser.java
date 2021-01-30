package gr.uom.project2020_smnaggregator.parsers;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gr.uom.project2020_smnaggregator.objects.Tag;

public class TagsJsonParser {

    public static final String TAG = "MyAppTagsJsonParser";

    public List<Tag> parseTagData(String tagJsonData) {
        List<Tag> tagList = new ArrayList<>();

        try {
            JSONArray jsonTagArray = new JSONArray(tagJsonData);
            JSONObject jsonObject = jsonTagArray.getJSONObject(0);
            jsonTagArray = jsonObject.getJSONArray("trends");

            for (int i = 0; i < jsonTagArray.length(); i++) {
                JSONObject tagJsonObj = jsonTagArray.getJSONObject(i);
                String name = tagJsonObj.getString("name");

                if (name.charAt(0) == '#') {
                    Tag tag = new Tag();
                    tag.setHashtag(name);

                    tagList.add(tag);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error in json parsing", e);
        }

        return tagList;
    }
}
