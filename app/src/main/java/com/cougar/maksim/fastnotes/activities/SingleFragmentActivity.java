package com.cougar.maksim.fastnotes.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.cougar.maksim.fastnotes.R;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swap_activity_fragment);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.single_fragment_container);

        if(fragment == null){
            fragment=createFragment();
            fm.beginTransaction()
                    .add(R.id.single_fragment_container,fragment)
                    .commit();
        }
    }
}
