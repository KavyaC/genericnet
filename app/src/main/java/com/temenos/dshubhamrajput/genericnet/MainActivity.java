package com.temenos.dshubhamrajput.genericnet;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Typeface;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public DrawerLayout mDrawerLayout;
    public ActionBarDrawerToggle mDrawerToggle;
    public int backpress = 0;
    SessionManager session1;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    LinkedHashMap<String, List<String>> expandableListDetail;
    DrawerLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (DrawerLayout)findViewById(R.id.drawer_layout);
        session1 = new SessionManager(getApplicationContext());
        Intent intent = getIntent();
        getSupportActionBar().setTitle("TEMENOS");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final Intent Addbeneficiary = new Intent(MainActivity.this, Addbeneficiary.class);
        final Intent TransferBwAccounts = new Intent(MainActivity.this, TransferBwAccounts.class);
        final Intent TransferWithinBnk = new Intent(MainActivity.this, TransferWithinBnk.class);
        final Intent TransferOtherBnk = new Intent(MainActivity.this, TransferOtherBnk.class);

        setupDrawer();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        TextView marqueeText1 = (TextView) findViewById(R.id.textview7);
        marqueeText1.setSelected(true);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListView.setGroupIndicator(getResources().getDrawable(R.drawable.state_list));
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {



            @Override
            public void onGroupExpand(int groupPosition) {

               if(expandableListTitle.get(groupPosition).equals("Account Summary"))
               {
                   Intent intent = new Intent(MainActivity.this, AcctSumActivity.class);
                   startActivity(intent);
                   layout.closeDrawer(GravityCompat.START);


               }
               else if(expandableListTitle.get(groupPosition).equals("Account Statement"))
               {
                   Intent intent = new Intent(MainActivity.this, AcctStmtActivity.class);
                   startActivity(intent);
                   layout.closeDrawer(GravityCompat.START);

               }
               else if(expandableListTitle.get(groupPosition).equals("Logout"))
               {
                   logout();
                   Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                   startActivity(intent);
                   layout.closeDrawer(GravityCompat.START);

               }

            }
        });


        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                if (expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals( "Add Beneficiary"))

                    startActivity(Addbeneficiary);

                else if(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals("Transfer within Bank"))

                    startActivity(TransferWithinBnk);

                else if(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals("Transfer between my Accounts"))

                    startActivity(TransferBwAccounts);

                else if(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).equals("Transfer to other Bank"))

                    startActivity(TransferOtherBnk);

                return false;
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()

            }


            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml
        //noinspection SimplifiableIfStatement

        // Activate the navigation drawer toggle
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){


        if (layout.isDrawerOpen(GravityCompat.START)) {
            layout.closeDrawer(GravityCompat.START);
            backpress = 0;
        }
        else {
            backpress = (backpress + 1);
            if(backpress<1||backpress == 1) {
                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
            }

            if (backpress>1) {
                session1.logoutUser();
                this.finish();
            }
        }
    }
    public void logout()
    {
        session1.logoutUser();
        this.finish();
    }


}
