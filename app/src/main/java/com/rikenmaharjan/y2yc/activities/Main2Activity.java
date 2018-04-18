package com.rikenmaharjan.y2yc.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.fragments.FeedBackSubmitFragment;
import com.rikenmaharjan.y2yc.fragments.HomeFragment;
import com.rikenmaharjan.y2yc.fragments.ViewLotteryResultFragment;
import com.rikenmaharjan.y2yc.utils.SessionManager;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private ConstraintLayout constraintLayout;
    private FeedBackSubmitFragment fbsf;
    private ViewLotteryResultFragment vlrf;
    private FragmentManager fm;
    private HomeFragment hm;
    public String sender;
    public String name;
    public SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(Color.TRANSPARENT);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);



        // Status bar :: Transparent
        Window window = this.getWindow();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }





        setSupportActionBar(toolbar);
        session = new SessionManager(getApplicationContext());
        //get all name and id here


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                session.logoutUser();

            }
        });


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fbsf = new FeedBackSubmitFragment();
        vlrf = new ViewLotteryResultFragment();
        hm = new HomeFragment();

        fm = getFragmentManager();

        FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
        ft.add(R.id.constraintLayout, hm, "tag1");  //now we have added our fragement to our Activity programmatically.  The other fragments exist, but have not been added yet.
        ft.addToBackStack ("myFrag1");  //why do we do this?
        ft.commit ();

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
        getMenuInflater().inflate(R.menu.main2, menu);
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



        if (id == R.id.nav_camera) {
            if (vlrf == null)
                vlrf = new ViewLotteryResultFragment();

            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, vlrf);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();

        } else if (id == R.id.nav_gallery) {
            if (fbsf == null)
                fbsf = new FeedBackSubmitFragment();

            FragmentTransaction ft = fm.beginTransaction ();  //Create a reference to a fragment transaction.
            ft.replace(R.id.constraintLayout, fbsf);
            ft.addToBackStack ("myFrag2");  //why do we do this?
            ft.commit();


        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* protected void onResume() {
        super.onResume();


        sender = this.getIntent().getExtras().getString("id");
        name  = this.getIntent().getExtras().getString("name");


        //IF ITS THE FRAGMENT THEN RECEIVE DATA
        if(sender != null)
        {
            session = new SessionManager(getApplicationContext());
            Toast.makeText(this, "Received", Toast.LENGTH_SHORT).show();



            String id  = sender;


            session.createLoginSession("Android Hive", id);


            Log.i("data",sender);
            Bundle args = new Bundle();
            args.putString("id",id);
            args.putString("name",name);


             fbsf.setArguments(args);
             vlrf.setArguments(args);

             hm.setArguments(args);




        }
    }
    */


}
