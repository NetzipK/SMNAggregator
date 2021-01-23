package gr.uom.project2020_smnaggregator;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TagsJsonParser {
    public List<Tag> parsePostData(String tagJsonData) {
        List<Tag> tagList = new ArrayList<>();

        try {
//            JSONObject json = new JSONObject(tagJsonData);

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
            Log.e("MyAppTagsJsonParser", "Error in json parsing", e);
        }

        return tagList;
    }
}
