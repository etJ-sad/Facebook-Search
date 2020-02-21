package com.example.liuyx.hw9_3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Liuyx on 4/23/17.
 */

public class CustomListAdapter extends ArrayAdapter<Item> {

    Context context;
    ArrayList<Item> items;

    public CustomListAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
        this.context = context;
        this.items = items;
    }
    /*
    public void setArray() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("myFavorites", MODE_PRIVATE);
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
        this.items = items;
    }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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
            new tvDetailListener.JSONParse().execute();
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