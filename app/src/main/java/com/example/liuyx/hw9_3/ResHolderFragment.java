package com.example.liuyx.hw9_3;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Liuyx on 4/25/17.
 */

public class ResHolderFragment extends Fragment implements View.OnClickListener{

    ArrayList<Item> items = new ArrayList<>();
    String type = "";
    boolean hasPrev = false;
    boolean hasNext = false;
    String prevPageURL = "";
    String nextPageURL = "";
    CustomListAdapter adapter;
    ListView lv = null;

    public ResHolderFragment() {

    }

    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);

        // listView
        lv = (ListView) rootView.findViewById(R.id.listView);
        adapter = new CustomListAdapter(this.getActivity(), this.items);
        lv.setAdapter(adapter);

        // buttons :)
        Button prevButton = (Button) rootView.findViewById(R.id.previousPage);
        Button nextButton = (Button) rootView.findViewById(R.id.nextPage);
        prevButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        if (hasPrev) {
            prevButton.setTextColor(Color.parseColor("#000000"));
        } else {
            prevButton.setTextColor(Color.parseColor("#b5b5b5"));
        }


        if (hasNext) {
            nextButton.setTextColor(Color.parseColor("#000000"));
        } else {
            nextButton.setTextColor(Color.parseColor("#b5b5b5"));
        }


        return rootView;
    }

    @Override
    public void onClick(View v) {
        JSONParse jsonParse = new JSONParse();
        switch (v.getId()) {

            case R.id.previousPage:
                if (!hasPrev) {
                    return;
                }
                jsonParse.setUrl(prevPageURL);
                break;

            case R.id.nextPage:
                if (!hasNext) {
                    return;
                }
                jsonParse.setUrl(nextPageURL);
                break;

            default:
                break;
        }
        jsonParse.execute();
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        //private ProgressDialog pDialog;
        private String url;

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl("http://sample-env-1.nvxagyvii3.us-west-2.elasticbeanstalk.com/hw8.php?lat=34.019481&long=-118.289549" + url);
            //http://sample-env-1.nvxagyvii3.us-west-2.elasticbeanstalk.com/
            //http://cs-server.usc.edu:20325/
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            SharedPreferences prefs = getActivity().getSharedPreferences("myFavorites", MODE_PRIVATE);
            int size = prefs.getInt("size", 0);
            items.clear();
            try {
                for (int i = 0; i < json.getJSONArray("data").length(); i++) {
                    JSONObject tempItem = json.getJSONArray("data").getJSONObject(i);
                    String id = tempItem.getString("id");
                    String name = tempItem.getString("name");
                    String picURL = tempItem.getJSONObject("picture").getJSONObject("data").getString("url");
                    boolean isFav = false;

                    for (int j = 0; j < size; j++) {
                        if (prefs.getString("id_" + j, "").equals(id)) {
                            isFav = true;
                        }
                    }
                    items.add(new Item(id, name, type, picURL, isFav));
                }

                Button prevButton = (Button) getView().findViewById(R.id.previousPage);
                Button nextButton = (Button) getView().findViewById(R.id.nextPage);

                if (json.getJSONObject("paging").has("next")) {
                    hasNext = true;
                    nextPageURL = json.getJSONObject("paging").getString("next");
                } else {
                    hasNext = false;
                }
                if (json.getJSONObject("paging").has("previous")) {
                    hasPrev = true;
                    prevPageURL = json.getJSONObject("paging").getString("previous");
                } else {
                    hasPrev = false;
                }

                if (hasPrev) {
                    prevButton.setTextColor(Color.parseColor("#000000"));
                } else {
                    prevButton.setTextColor(Color.parseColor("#b5b5b5"));
                }

                if (hasNext) {
                    nextButton.setTextColor(Color.parseColor("#000000"));
                } else {
                    nextButton.setTextColor(Color.parseColor("#b5b5b5"));
                }

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
            }

            adapter.notifyDataSetChanged();
        }
    }
}

