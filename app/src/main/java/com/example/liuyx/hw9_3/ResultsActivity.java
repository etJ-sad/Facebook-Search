package com.example.liuyx.hw9_3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    JSONObject json;
    ArrayList<Item> userArray;
    ArrayList<Item> pageArray;
    ArrayList<Item> eventArray;
    ArrayList<Item> placeArray;
    ArrayList<Item> groupArray;
    Item clickedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.users);
        tabLayout.getTabAt(1).setIcon(R.drawable.pages);
        tabLayout.getTabAt(2).setIcon(R.drawable.events);
        tabLayout.getTabAt(3).setIcon(R.drawable.places);
        tabLayout.getTabAt(4).setIcon(R.drawable.groups);

        Intent intent = getIntent();

        try {

            json = new JSONObject(intent.getStringExtra("myJson"));

            userArray = mSectionsPagerAdapter.fragments[0].items;
            pageArray = mSectionsPagerAdapter.fragments[1].items;
            eventArray = mSectionsPagerAdapter.fragments[2].items;
            placeArray = mSectionsPagerAdapter.fragments[3].items;
            groupArray = mSectionsPagerAdapter.fragments[4].items;
            mSectionsPagerAdapter.fragments[0].type = "user";
            mSectionsPagerAdapter.fragments[1].type = "page";
            mSectionsPagerAdapter.fragments[2].type = "event";
            mSectionsPagerAdapter.fragments[3].type = "place";
            mSectionsPagerAdapter.fragments[4].type = "group";

            SharedPreferences prefs = this.getSharedPreferences("myFavorites", MODE_PRIVATE);
            int size = prefs.getInt("size", 0);


            for (int i = 0; i < json.getJSONObject("user").getJSONArray("data").length(); i++) {
                JSONObject tempItem = json.getJSONObject("user").getJSONArray("data").getJSONObject(i);
                String id = tempItem.getString("id");
                String name = tempItem.getString("name");
                String picURL = tempItem.getJSONObject("picture").getJSONObject("data").getString("url");
                boolean isFav = false;

                for (int j = 0; j < size; j++) {
                    if (prefs.getString("id_" + j, "").equals(id)) {
                        isFav = true;
                    }
                }
                userArray.add(new Item(id, name, "user", picURL, isFav));
            }
            if (json.getJSONObject("user").getJSONObject("paging").has("next")) {
                mSectionsPagerAdapter.fragments[0].hasNext = true;
                mSectionsPagerAdapter.fragments[0].nextPageURL = json.getJSONObject("user").getJSONObject("paging").getString("next");
            }
            if (json.getJSONObject("user").getJSONObject("paging").has("previous")) {
                mSectionsPagerAdapter.fragments[0].hasPrev = true;
                mSectionsPagerAdapter.fragments[0].prevPageURL = json.getJSONObject("user").getJSONObject("paging").getString("previous");
            }
            //mSectionsPagerAdapter.fragments[0].setArrayList(userArray);

            for (int i = 0; i < json.getJSONObject("page").getJSONArray("data").length(); i++) {
                JSONObject tempItem = json.getJSONObject("page").getJSONArray("data").getJSONObject(i);
                String id = tempItem.getString("id");
                String name = tempItem.getString("name");
                String picURL = tempItem.getJSONObject("picture").getJSONObject("data").getString("url");
                boolean isFav = false;

                for (int j = 0; j < size; j++) {
                    if (prefs.getString("id_" + j, "").equals(id)) {
                        isFav = true;
                    }
                }
                pageArray.add(new Item(id, name, "page", picURL, isFav));
            }
            if (json.getJSONObject("page").getJSONObject("paging").has("next")) {
                mSectionsPagerAdapter.fragments[1].hasNext = true;
                mSectionsPagerAdapter.fragments[1].nextPageURL = json.getJSONObject("page").getJSONObject("paging").getString("next");
            }
            if (json.getJSONObject("page").getJSONObject("paging").has("previous")) {
                mSectionsPagerAdapter.fragments[1].hasPrev = true;
                mSectionsPagerAdapter.fragments[1].prevPageURL = json.getJSONObject("page").getJSONObject("paging").getString("previous");
            }
            //mSectionsPagerAdapter.fragments[1].setArrayList(pageArray);

            for (int i = 0; i < json.getJSONObject("event").getJSONArray("data").length(); i++) {
                JSONObject tempItem = json.getJSONObject("event").getJSONArray("data").getJSONObject(i);
                String id = tempItem.getString("id");
                String name = tempItem.getString("name");
                String picURL = tempItem.getJSONObject("picture").getJSONObject("data").getString("url");
                boolean isFav = false;

                for (int j = 0; j < size; j++) {
                    if (prefs.getString("id_" + j, "").equals(id)) {
                        isFav = true;
                    }
                }
                eventArray.add(new Item(id, name, "event", picURL, isFav));
            }
            if (json.getJSONObject("event").getJSONObject("paging").has("next")) {
                mSectionsPagerAdapter.fragments[2].hasNext = true;
                mSectionsPagerAdapter.fragments[2].nextPageURL = json.getJSONObject("event").getJSONObject("paging").getString("next");
            }
            if (json.getJSONObject("event").getJSONObject("paging").has("previous")) {
                mSectionsPagerAdapter.fragments[2].hasPrev = true;
                mSectionsPagerAdapter.fragments[2].prevPageURL = json.getJSONObject("event").getJSONObject("paging").getString("previous");
            }
            //mSectionsPagerAdapter.fragments[2].setArrayList(eventArray);

            for (int i = 0; i < json.getJSONObject("place").getJSONArray("data").length(); i++) {
                JSONObject tempItem = json.getJSONObject("place").getJSONArray("data").getJSONObject(i);
                String id = tempItem.getString("id");
                String name = tempItem.getString("name");
                String picURL = tempItem.getJSONObject("picture").getJSONObject("data").getString("url");
                boolean isFav = false;

                for (int j = 0; j < size; j++) {
                    if (prefs.getString("id_" + j, "").equals(id)) {
                        isFav = true;
                    }
                }
                placeArray.add(new Item(id, name, "place", picURL, isFav));
            }
            if (json.getJSONObject("place").getJSONObject("paging").has("next")) {
                mSectionsPagerAdapter.fragments[3].hasNext = true;
                mSectionsPagerAdapter.fragments[3].nextPageURL = json.getJSONObject("place").getJSONObject("paging").getString("next");
            }
            if (json.getJSONObject("place").getJSONObject("paging").has("previous")) {
                mSectionsPagerAdapter.fragments[3].hasPrev = true;
                mSectionsPagerAdapter.fragments[3].prevPageURL = json.getJSONObject("place").getJSONObject("paging").getString("previous");
            }
            //mSectionsPagerAdapter.fragments[3].setArrayList(placeArray);

            for (int i = 0; i < json.getJSONObject("group").getJSONArray("data").length(); i++) {
                JSONObject tempItem = json.getJSONObject("group").getJSONArray("data").getJSONObject(i);
                String id = tempItem.getString("id");
                String name = tempItem.getString("name");
                String picURL = tempItem.getJSONObject("picture").getJSONObject("data").getString("url");
                boolean isFav = false;

                for (int j = 0; j < size; j++) {
                    if (prefs.getString("id_" + j, "").equals(id)) {
                        isFav = true;
                    }
                }
                groupArray.add(new Item(id, name, "group", picURL, isFav));
            }
            if (json.getJSONObject("group").getJSONObject("paging").has("next")) {
                mSectionsPagerAdapter.fragments[4].hasNext = true;
                mSectionsPagerAdapter.fragments[4].nextPageURL = json.getJSONObject("group").getJSONObject("paging").getString("next");
            }
            if (json.getJSONObject("group").getJSONObject("paging").has("previous")) {
                mSectionsPagerAdapter.fragments[4].hasPrev = true;
                mSectionsPagerAdapter.fragments[4].prevPageURL = json.getJSONObject("group").getJSONObject("paging").getString("previous");
            }
            //mSectionsPagerAdapter.fragments[4].setArrayList(groupArray);


        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        //0 user, 1 page, 2 event, 3 place, 4 group
        ResHolderFragment[] fragments = new ResHolderFragment[]{
                new ResHolderFragment(),
                new ResHolderFragment(),
                new ResHolderFragment(),
                new ResHolderFragment(),
                new ResHolderFragment()
        };


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            return fragments[position];
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Users";
                case 1:
                    return "Pages";
                case 2:
                    return "Events";
                case 3:
                    return "Places";
                case 4:
                    return "Groups";
            }
            return null;
        }
    }

}
