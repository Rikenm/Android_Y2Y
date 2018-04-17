package com.rikenmaharjan.y2yc.activities;

import android.app.Fragment;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rikenmaharjan.y2yc.fragments.ViewActionFragment;

public class ViewActionActivity extends BaseFragmentActivity {

    @Override
    Fragment createFragment() {
        return ViewActionFragment.newInstance();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        // hiding action abr

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
