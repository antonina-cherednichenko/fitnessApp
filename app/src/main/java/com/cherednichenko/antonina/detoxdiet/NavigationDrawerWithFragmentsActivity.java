package com.cherednichenko.antonina.detoxdiet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cherednichenko.antonina.detoxdiet.detox_diet_data.DayInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_data.ProgramInfo;
import com.cherednichenko.antonina.detoxdiet.detox_diet_programs_list.DetoxDietProgramsListFragment;
import com.cherednichenko.antonina.detoxdiet.navigation_drawer.DrawerItemCustomAdapter;
import com.cherednichenko.antonina.detoxdiet.navigation_drawer.NavigationDataModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerWithFragmentsActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private String[] navigationItems;
    private List<ProgramInfo> receipes;

    private int[] images = {R.drawable.green_detox_program, R.drawable.citrus_detox_program, R.drawable.apple_detox_program,
            R.drawable.juice_detox_program, R.drawable.rice_detox_program};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_with_fragments);

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        NavigationDataModel[] drawerItems = new NavigationDataModel[4];

        drawerItems[0] = new NavigationDataModel(R.drawable.navdrawer_receipes, "Receipes");
        drawerItems[1] = new NavigationDataModel(R.drawable.navdrawer_favourite, "Favorites");
        drawerItems[2] = new NavigationDataModel(R.drawable.navdrawer_time, "Schedule");
        drawerItems[3] = new NavigationDataModel(R.drawable.navdrawer_about, "About");

        navigationItems = getResources().getStringArray(R.array.navigation_drawer_items_array);

        // Set the adapter for the list view
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItems);
        mDrawerList.setAdapter(adapter);

        View header = getLayoutInflater().inflate(R.layout.navigation_header, null);
        mDrawerList.addHeaderView(header);

        receipes = createReceipeList();

        //setup default fragment with program cards
        Fragment fragment = new DetoxDietProgramsListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("receipes", (Serializable) receipes);
        bundle.putBoolean("mode", false);
        fragment.setArguments(bundle);

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }

        setupToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();

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
        switch (position) {
            case 1: {
                //Receipes item
                fragment = new DetoxDietProgramsListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("receipes", (Serializable) receipes);
                bundle.putBoolean("mode", false);
                fragment.setArguments(bundle);
                break;

            }
            case 2: {
                //Favourites item
                fragment = new DetoxDietProgramsListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("receipes", (Serializable) receipes);
                bundle.putBoolean("mode", true);
                fragment.setArguments(bundle);
                break;

            }
            case 3: {
                //Schedule item
                fragment = new ScheduleFragment();
                break;
            }
            case 4: {
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
            FragmentManager fragmentManager = getFragmentManager();
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

    private List<ProgramInfo> createReceipeList() {
        List<ProgramInfo> receipes = new ArrayList<>();
        try {
            String chatFileData = loadProgramsData();
            JSONObject jsonData = new JSONObject(chatFileData);
            JSONArray allPrograms = jsonData.getJSONArray("data");
            for (int i = 0; i < allPrograms.length(); i++) {
                JSONObject program = allPrograms.getJSONObject(i);
                JSONArray schedule = program.getJSONArray("schedule");
                List<DayInfo> days = new ArrayList<>();
                for (int j = 0; j < schedule.length(); j++) {
                    JSONObject day = schedule.getJSONObject(j);
                    days.add(new DayInfo(day.getString("name"), day.getString("description")));
                }

                ProgramInfo receipe = new ProgramInfo();
                receipe.setDays(days);
                receipe.setDescription(program.getString("description"));
                receipe.setDuration(program.getInt("duration"));
                receipe.setLiked(false);
                receipe.setName(program.getString("name"));
                receipe.setPhotoId(images[i]);
                receipe.setShortDescription(program.getString("shortDescription"));
                receipes.add(receipe);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return receipes;
    }

    private String loadProgramsData() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.programs_data);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String receiveString;
        StringBuilder stringBuilder = new StringBuilder();

        while ((receiveString = bufferedReader.readLine()) != null) {
            stringBuilder.append(receiveString);
            stringBuilder.append("\n");
        }

        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();


        return stringBuilder.toString();
    }


}

