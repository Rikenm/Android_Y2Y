package com.rikenmaharjan.y2yc.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.rikenmaharjan.y2yc.fragments.HomeFragment;

/**
 * Created by Riken on 3/18/18.
 */

/** public class HomeActivity extends BaseFragmentActivity {



    String data;

    //BaseFragmentActivity calls the following method and create HomeFragment to add to this activity

    @Override
    Fragment createFragment() {
        // getting value that I got from login fragment
        Bundle bundle = new Bundle();
        HomeFragment fragInfo = new HomeFragment();
        bundle.putString("id", data );
        fragInfo.setArguments(bundle);
        return fragInfo;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {


        Bundle extras = getIntent().getExtras();
        data = extras.getString("id");

        if(getIntent() != null) {   //we never go inside here
            data = extras.getString("id");     //never goes inside here why

            Log.i("data",data);




        }

        super.onCreate(savedInstanceState, persistentState);





    }
}
**/