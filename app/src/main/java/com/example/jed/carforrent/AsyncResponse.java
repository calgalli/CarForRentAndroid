package com.example.jed.carforrent;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by cake on 7/30/15 AD.
 */
public interface AsyncResponse {
    void processFinish(JSONObject output);
    void processFinishID(JSONObject output);
    void loginFinish(JSONObject output);
    void processFinishString(String output, String idd);
    void processFinishArray(JSONArray output);


}
