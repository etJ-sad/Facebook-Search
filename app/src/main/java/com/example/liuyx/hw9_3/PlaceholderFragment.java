package com.example.liuyx.hw9_3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Liuyx on 4/21/17.
 */

public class PlaceholderFragment extends Fragment {

    ArrayList<Item> items = new ArrayList<>();
    String type = "";
    CustomListAdapter adapter;

    public PlaceholderFragment() {

    }

    public void setType(String type) {
        this.type = type;
    }

    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    public void setArray() {
        items.clear();
        if (adapter == null) {
            return;
        }
        //adapter.setArray();
        if (getActivity() == null) {
            return;
        }
        SharedPreferences prefs = getActivity().getSharedPreferences("myFavorites", MODE_PRIVATE);
        int size = prefs.getInt("size", 0);

        for (int i = 0; i < size; i++) {
            String id = prefs.getString("id_" + i, "");
            String name = prefs.getString("name_" + i, "");
            String type = prefs.getString("type_" + i, "");
            String pictureURL = prefs.getString("pictureURL_" + i, "");
            boolean isFav = true;
            if (type.equals(this.type)) {
                items.add(new Item(id, name, type, pictureURL, isFav));
            }
        }
        //notifyAll();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        items.clear();

        SharedPreferences prefs = getActivity().getSharedPreferences("myFavorites", MODE_PRIVATE);
        int size = prefs.getInt("size", 0);

        for (int i = 0; i < size; i++) {
            String id = prefs.getString("id_" + i, "");
            String name = prefs.getString("name_" + i, "");
            String type = prefs.getString("type_" + i, "");
            String pictureURL = prefs.getString("pictureURL_" + i, "");
            boolean isFav = true;
            if (type.equals(this.type)) {
                items.add(new Item(id, name, type, pictureURL, isFav));
            }
        }
        View rootView = inflater.inflate(R.layout.fragment_fav_content, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.fav_listView);

        adapter = new CustomListAdapter(this.getActivity());
        lv.setAdapter(adapter);
        return rootView;
    }

    private class CustomListAdapter extends ArrayAdapter<Item> {

        Context context;
        //ArrayList<Item> myItems = new ArrayList<>();

        public CustomListAdapter(Context context) {
            super(context, 0, items);
            this.context = context;
        }

    public void setArray() {
        //myItems.clear();
        items.clear();
        SharedPreferences prefs = getActivity().getSharedPreferences("myFavorites", MODE_PRIVATE);
        int size = prefs.getInt("size", 0);

        for (int i = 0; i < size; i++) {
            String id = prefs.getString("id_" + i, "");
            String name = prefs.getString("name_" + i, "");
            String thistype = prefs.getString("type_" + i, "");
            String pictureURL = prefs.getString("pictureURL_" + i, "");
            boolean isFav = true;
            if (thistype.equals(type)) {
                items.add(new Item(id, name, type, pictureURL, isFav));
            }
        }
        //myItems = items;
        notifyDataSetChanged();
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            this.setArray();
            notifyDataSetChanged();
            if (items.size() == 0) {
                return LayoutInflater.from(getContext()).inflate(R.layout.fav_null, parent, false);
            }
            Item item = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
            // Populate the data into the template view using the data object
            tvName.setText(item.name);

            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            Picasso.with(context).load(item.getPictureURL()).resize(150,150).into(imageView);
            // Return the completed view to render on screen

            ImageView isFavo = (ImageView) convertView.findViewById(R.id.tvFav);
            SharedPreferences prefs = this.getContext().getSharedPreferences("myFavorites", MODE_PRIVATE);
            int size = prefs.getInt("size", 0);

            int i;
            for (i = 0; i < size; i++) {
                if (prefs.getString("id_" + i, "").equals(item.id)) {
                    isFavo.setImageResource(R.drawable.favorites_on);
                    break;
                }
            }
            if (i == size) {
                isFavo.setImageResource(R.drawable.favorites_off);
            }

            ImageView arrow = (ImageView)convertView.findViewById(R.id.tvDetail);
            arrow.setOnClickListener(new tvDetailListener(item));
            return convertView;

        }

        public class tvDetailListener implements View.OnClickListener {
            Item item;

            public tvDetailListener(Item item) {
                this.item = item;
            }

            @Override
            public void onClick(View view) {
                new JSONParse().execute();
            }

            private class JSONParse extends AsyncTask<String, String, JSONObject> {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected JSONObject doInBackground(String... args) {
                    JSONParser jParser = new JSONParser();

                    // Getting JSON from URL
                    JSONObject json = jParser.getJSONFromUrl("http://cs-server.usc.edu:20325/hw8.php?id=" + item.getId());

                    return json;
                }

                @Override
                protected void onPostExecute(JSONObject json) {
                    super.onPostExecute(json);

                    Intent intent = new Intent(getContext(), DetailsActivity.class);
                    intent.putExtra("itemId", item.id + "");
                    intent.putExtra("itemType", item.type + "");
                    intent.putExtra("itemName", item.getName());
                    intent.putExtra("itemPictURL", item.getPictureURL());

                    if (json != null) {
                        intent.putExtra("itemJson", json + "");
                    }
                    getContext().startActivity(intent);
                }
            }
        }
    }

}
