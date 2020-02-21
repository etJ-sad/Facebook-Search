package com.example.liuyx.hw9_3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private CallbackManager callbackManager;
    ShareDialog shareDialog;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private String id;
    private String name;
    private String pictURL;
    private String type;
    private JSONObject json;
    private boolean hasAlbums;
    private boolean hasPosts;
    private boolean isFav;
    private ArrayList<AlbumItem> albumArrayList;
    private ArrayList<PostItem> postArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        //shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {  });

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
        tabLayout.getTabAt(0).setIcon(R.drawable.albums);
        tabLayout.getTabAt(1).setIcon(R.drawable.posts);

        albumArrayList = new ArrayList<>();
        postArrayList = new ArrayList<>();

        Intent intent = getIntent();
        id = intent.getStringExtra("itemId");
        name = intent.getStringExtra("itemName");
        type = intent.getStringExtra("itemType");
        pictURL = intent.getStringExtra("itemPictURL");



        if (intent.getStringExtra("itemJson") == null) {
            hasAlbums = false;
            hasPosts = false;
        } else {
            try {

                json = new JSONObject(intent.getStringExtra("itemJson"));

                if (json.has("albums")) {
                    hasAlbums = true;

                    for (int i = 0; i < json.getJSONObject("albums").getJSONArray("data").length(); i++) {
                        JSONObject tempItem = json.getJSONObject("albums").getJSONArray("data").getJSONObject(i);
                        ArrayList<String> urls = new ArrayList<>();
                        String name = tempItem.getString("name");
                        for (int j = 0; j < tempItem.getJSONObject("photos").getJSONArray("data").length(); j++) {
                            urls.add(tempItem.getJSONObject("photos").getJSONArray("data").getJSONObject(j).getString("picture"));
                        }
                        albumArrayList.add(new AlbumItem(name, urls));

                    }
                } else {
                    hasAlbums = false;
                }


                if (json.has("posts")) {
                    hasPosts = true;
                    for (int i = 0; i < json.getJSONObject("posts").getJSONArray("data").length(); i++) {
                        JSONObject tempItem = json.getJSONObject("posts").getJSONArray("data").getJSONObject(i);
                        String name = json.getString("name");
                        String pictureUrl = json.getJSONObject("picture").getJSONObject("data").getString("url");
                        String message = "";
                        String createdTime = tempItem.getString("created_time");
                        if (tempItem.has("message")) {
                            message = tempItem.getString("message");
                        }
                        postArrayList.add(new PostItem(message, createdTime, pictureUrl,name));
                    }
                } else {
                    hasPosts = false;
                }
            } catch (JSONException e) {
                Log.e("Details JSON", "Could not parse malformed JSON: \"" + json + "\"");
            }
        }
        mSectionsPagerAdapter.fragmentAlbum.setArrayList(albumArrayList);
        mSectionsPagerAdapter.fragmentPost.setArrayList(postArrayList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        SharedPreferences prefs = this.getSharedPreferences("myFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        MenuItem menuItem = menu.findItem(R.id.action_fav);
        int size = prefs.getInt("size", 0);
        for (int i = 0; i < size; i++) {
            if (prefs.getString("id_" + i, "").equals(id)) {
                isFav = true;
                menuItem.setTitle("Remove from Favorites");
                return true;
            }
        }
        menuItem.setTitle("Add to Favorites");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        SharedPreferences prefs = this.getSharedPreferences("myFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int size = prefs.getInt("size", 0);

        int i;
        int selectId = item.getItemId();

        // find index i
        for (i = 0; i < size; i++) {
            if (prefs.getString("id_" + i, "").equals(id)) {
                break;
            }
        }

        // click listen
        if (selectId == R.id.action_fav) {
            if (isFav) {
                for (int j = i; j < size - 1; j++) {
                    editor.putString("id_" + j, prefs.getString("id_" + (j + 1), ""));
                    editor.putString("name_" + j, prefs.getString("name_" + (j + 1), ""));
                    editor.putString("type_" + j, prefs.getString("type_" + (j + 1), ""));
                    editor.putString("pictureURL_" + j, prefs.getString("pictureURL_" + (j + 1), ""));
                }

                editor.remove("id_" + (size - 1));
                editor.remove("name_" + (size - 1));
                editor.remove("type_" + (size - 1));
                editor.remove("pictureURL_" + (size - 1));
                editor.putInt("size", size - 1);

                Toast.makeText(this, "Removed from Favorites!", Toast.LENGTH_LONG).show();
                item.setTitle("Add to Favorites");
                isFav = false;
                editor.commit();
                FragmentFav.setFragArray();
            }
            else {
                setFavorite(item);
            }

        } else if (selectId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (selectId == R.id.action_share) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            shareDialog = new ShareDialog(this);
            LoginManager.getInstance().logOut();
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                Toast.makeText(this, "Sharing " + name + "!!", Toast.LENGTH_LONG).show();
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setImageUrl(Uri.parse(pictURL))
                        .setContentTitle(name)
                        .setContentDescription("FB SEARCH FROM USC CSCI571")
                        .build();
                shareDialog.show(linkContent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            Toast.makeText(this, "You shared this post.", Toast.LENGTH_LONG).show();
        }
    }

    private void setFavorite(MenuItem item) {

        SharedPreferences prefs = getSharedPreferences("myFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int size = prefs.getInt("size", 0);
        editor.putInt("size", size + 1);

        editor.putString("id_" + size, id);
        editor.putString("name_" + size, name);
        editor.putString("type_" + size, type);
        editor.putString("pictureURL_" + size, pictURL);
        item.setTitle("Remove from Favorites");
        isFav = true;
        Toast.makeText(this, "Added to Favorites!", Toast.LENGTH_LONG).show();
        editor.commit(); // This line is IMPORTANT. If you miss this one its not gonna work!
    }
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        FragmentAlbums fragmentAlbum = new FragmentAlbums();
        FragmentPosts fragmentPost = new FragmentPosts();

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return fragmentAlbum;
            } else if (position == 1) {
                return fragmentPost;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Albums";
                case 1:
                    return "Posts";
            }
            return null;
        }
    }


}
