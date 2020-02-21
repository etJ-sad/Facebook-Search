package com.example.liuyx.hw9_3;

import android.content.Context;
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

import java.util.ArrayList;

/**
 * Created by Liuyx on 4/21/17.
 */

public class FragmentAlbums extends Fragment{
    ArrayList<AlbumItem> items = null;

    public FragmentAlbums() {

    }

    public void setArrayList(ArrayList<AlbumItem> arrayList) {
        this.items = arrayList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.albumListView);
        TextView noAlbum = (TextView) rootView.findViewById(R.id.noAlbum);
        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), items);
        lv.setAdapter(adapter);
        if (items.size() == 0) {
            noAlbum.setVisibility(View.VISIBLE);
        } else {
            noAlbum.setVisibility(View.GONE);
        }
        return rootView;
    }

    private class CustomListAdapter extends ArrayAdapter<AlbumItem> {

        ArrayList<AlbumItem> items;
        Context context;

        public CustomListAdapter(Context context, ArrayList<AlbumItem> items) {
            super(context, 0, items);
            this.items = items;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            AlbumItem item = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.album_item, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.albumName);
            // Populate the data into the template view using the data object
            tvName.setText(item.name);
            // Return the completed view to render on screen

            ImageView arrow = (ImageView)convertView.findViewById(R.id.albumIcon);
            arrow.setOnClickListener(new tvDetailListener(item, convertView, position));

            ImageView imageView1 = (ImageView) convertView.findViewById(R.id.albumPhoto1);
            ImageView imageView2 = (ImageView) convertView.findViewById(R.id.albumPhoto2);
            if (item.getUrls().size() == 1) {
                Picasso.with(context).load(item.getUrls().get(0)).resize(1000,1000).into(imageView1);
                imageView1.setVisibility(View.GONE);
            } else if (item.getUrls().size() == 2) {
                Picasso.with(context).load(item.getUrls().get(0)).resize(1000,1000).into(imageView1);
                imageView1.setVisibility(View.GONE);
                Picasso.with(context).load(item.getUrls().get(1)).resize(1000,1000).into(imageView2);
                imageView2.setVisibility(View.GONE);
            }

            return convertView;

        }

        private class tvDetailListener implements View.OnClickListener {
            AlbumItem item;
            View convertView;
            int position;

            public tvDetailListener(AlbumItem item, View convertView, int position) {

                this.item = item;
                this.convertView = convertView;
                this.position = position;
            }

            @Override
            public void onClick(View view) {
                ImageView imageView1 = (ImageView) convertView.findViewById(R.id.albumPhoto1);
                ImageView imageView2 = (ImageView) convertView.findViewById(R.id.albumPhoto2);
                if(imageView1.isShown()) {
                    imageView1.setVisibility(View.GONE);
                    imageView2.setVisibility(View.GONE);
                } else {
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                }

            }


        }
    }
}
