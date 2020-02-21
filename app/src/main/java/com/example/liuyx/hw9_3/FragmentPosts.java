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

public class FragmentPosts extends Fragment{
    ArrayList<PostItem> items = null;

    public FragmentPosts() {

    }

    public void setArrayList(ArrayList<PostItem> arrayList) {
        this.items = arrayList;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        ListView lv = (ListView) rootView.findViewById(R.id.postListView);
        TextView noPosts = (TextView) rootView.findViewById(R.id.noPosts);
        CustomListAdapter adapter = new CustomListAdapter(this.getActivity(), items);
        lv.setAdapter(adapter);
        if (items.size() == 0) {
            noPosts.setVisibility(View.VISIBLE);
        } else {
            noPosts.setVisibility(View.GONE);
        }
        return rootView;
    }

    private class CustomListAdapter extends ArrayAdapter<PostItem> {

        ArrayList<PostItem> items;
        Context context;

        public CustomListAdapter(Context context, ArrayList<PostItem> items) {
            super(context, 0, items);
            this.items = items;
            this.context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            PostItem item = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.post_item, parent, false);
            }
            // Lookup view for data population
            ImageView image = (ImageView)convertView.findViewById(R.id.postImage);
            Picasso.with(context).load(item.getPictureUrl()).resize(200,200).into(image);
            TextView tvName = (TextView) convertView.findViewById(R.id.postName);
            // Populate the data into the template view using the data object
            tvName.setText(item.name);
            // Return the completed view to render on screen
            TextView tvTime = (TextView) convertView.findViewById(R.id.postTime);
            tvTime.setText(item.getTime());
            TextView tvMessage = (TextView) convertView.findViewById(R.id.postMessage);
            tvMessage.setText(item.getMessage());

            return convertView;

        }
    }
}

