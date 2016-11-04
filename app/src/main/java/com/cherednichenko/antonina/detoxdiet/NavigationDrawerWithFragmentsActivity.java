package com.cherednichenko.antonina.detoxdiet;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DataProcessor;
import com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list.DetoxDietLaunchMode;
import com.cherednichenko.antonina.detoxdiet.navigation_drawer.DrawerItemCustomAdapter;
import com.cherednichenko.antonina.detoxdiet.navigation_drawer.NavigationDataModel;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class NavigationDrawerWithFragmentsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private String[] navigationItems;


    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_with_fragments);

        mTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        //mDrawerLayout.setScrimColor(getResources().getColor(R.color.colorPrimary));

        //download content from backend service
        new DownloadContentTask().execute();

        NavigationDataModel[] drawerItems = new NavigationDataModel[5];

        drawerItems[0] = new NavigationDataModel(R.drawable.ic_navdrawer_all, getResources().getString(R.string.navbar_programs));
        drawerItems[1] = new NavigationDataModel(R.drawable.ic_navdrawer_favourite, getResources().getString(R.string.navbar_favorites));
        drawerItems[2] = new NavigationDataModel(R.drawable.ic_navdrawer_new, getResources().getString(R.string.navbar_new));
        drawerItems[3] = new NavigationDataModel(R.drawable.ic_navdrawer_schedule, getResources().getString(R.string.navbar_schedule));
        drawerItems[4] = new NavigationDataModel(R.drawable.ic_navdrawer_about, getResources().getString(R.string.navbar_about));

        navigationItems = getResources().getStringArray(R.array.navigation_drawer_items_array);

        // Set the adapter for the list view
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItems);
        mDrawerList.setAdapter(adapter);


        View header = getLayoutInflater().inflate(R.layout.navigation_header, null);
        mDrawerList.addHeaderView(header);

        //setup default fragment with program cards
        Fragment fragment = new AllDetoxDietTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("mode", DetoxDietLaunchMode.STANDARD.getMode());
        fragment.setArguments(bundle);

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }

        setupToolbar();
        setTitle("Programs");

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

        //handle search results
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            Fragment fragment = new AllDetoxDietTabFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("mode", DetoxDietLaunchMode.SEARCH.getMode());
            bundle.putString("searchQuery", query);
            fragment.setArguments(bundle);

            if (fragment != null) {
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .commit();
            }
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /**
     * Swaps fragments in the main content view
     */

    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment;
        System.out.println("position = " + position);
        switch (position) {
            case 1: {
                //Receipes item
                fragment = new AllDetoxDietTabFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("mode", DetoxDietLaunchMode.STANDARD.getMode());
                fragment.setArguments(bundle);
                break;

            }
            case 2: {
                //Favourites item
                fragment = new AllDetoxDietTabFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("mode", DetoxDietLaunchMode.LIKED.getMode());
                fragment.setArguments(bundle);
                break;
            }
            case 3: {
                //Favourites item
                fragment = new AllDetoxDietTabFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("mode", DetoxDietLaunchMode.NEW.getMode());
                fragment.setArguments(bundle);
                break;
            }
            case 4: {
                //Schedule item
                fragment = new ScheduleFragment();
                break;
            }
            case 5: {
                //About item
                fragment = new AboutFragment();
                break;
            }
            default: {
                fragment = null;
                break;
            }
        }

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(navigationItems[position - 1]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Log.e("ReceipesActivity", "Error in creating fragment");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }


    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setupDrawerToggle() {
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        //This is necessary to change the icon of the Drawer Toggle upon state change.
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    private class DownloadContentTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(ProgressBar.VISIBLE);
            mDrawerLayout.setVisibility(FrameLayout.INVISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            // Instantiate the RequestQueue.
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL("https://mighty-bayou-30907.herokuapp.com/data");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                return IOUtils.toString(in);

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return "{}";
        }

        protected void onPostExecute(String result) {
            try {
                FileOutputStream fos = openFileOutput(DataProcessor.DATA_FILENAME, Context.MODE_PRIVATE);
                fos.write(result.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            progressBar.setVisibility(ProgressBar.INVISIBLE);
            mDrawerLayout.setVisibility(DrawerLayout.VISIBLE);
        }

    }

}

