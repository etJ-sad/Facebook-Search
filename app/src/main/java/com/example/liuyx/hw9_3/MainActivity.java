package com.example.liuyx.hw9_3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentHome fragment_home = new FragmentHome();
    FragmentFav fragment_fav = new FragmentFav();
    //FragmentMe fragment_me = new FragmentMe();
    //URL to get JSON Array
    private static String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (fragment_home == null) {
            System.out.println("nullHome");
        }


        /*
        SharedPreferences prefs = getSharedPreferences("myFavorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();*/

        // home fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.main, fragment_home).addToBackStack(null).commit();
        //fragment_home = (FragmentHome) getSupportFragmentManager().findFragmentById(R.id.main);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the home action
            getSupportFragmentManager().beginTransaction().replace(R.id.main, fragment_home).addToBackStack(null).commit();
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Search on FB");
        } else if (id == R.id.nav_fav) {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Favorites");
            getSupportFragmentManager().beginTransaction().replace(R.id.main, fragment_fav).addToBackStack(null).commit();

        } else if (id == R.id.nav_me) {
            Intent intent = new Intent(MainActivity.this, MeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearEventHandler(View view) {
        fragment_home.editText.setText("");
    }

    public void searchEventHandler(View view) {
        String keyword = fragment_home.editText.getText().toString();
        if (keyword.equals("")) {
            Toast.makeText(this, "Please enter a keyword!", Toast.LENGTH_LONG).show();
            return;
        }
        url = "http://sample-env-1.nvxagyvii3.us-west-2.elasticbeanstalk.com/hw8.php?lat=34.019481&long=-118.289549&keyword=" + keyword;
        //http://cs-server.usc.edu:20325/
        new JSONParse().execute();

    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        //private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();

            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("myJson", json.toString());
            getApplicationContext().startActivity(intent);

        }
    }
}
