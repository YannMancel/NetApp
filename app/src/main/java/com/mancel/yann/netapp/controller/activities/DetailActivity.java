package com.mancel.yann.netapp.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mancel.yann.netapp.R;
import com.mancel.yann.netapp.controller.fragments.DetailFragment;

public class DetailActivity extends AppCompatActivity {

    // FIELDS --------------------------------------------------------------------------------------

    private DetailFragment mDetailFragment;

    // METHODS -------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associates the layout file to this class
        setContentView(R.layout.activity_detail);

        // Configures and shows the main fragment
        this.configureAndShowDetailFragment();
    }

    /**
     * Configures and shows the main fragment
     */
    private void configureAndShowDetailFragment() {
        // Creates a Fragment object to retrieve the fragment [FragmentManager -> Fragment]
        this.mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.activity_detail_frame_layout);

        // If the fragment is not displayed
        if (this.mDetailFragment == null) {
            // Creates a MainFragment object
            this.mDetailFragment = new DetailFragment();

            // Adds the transaction to create the fragment [FragmentManager -> FragmentTransaction -> int]
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.activity_detail_frame_layout, this.mDetailFragment)
                                       .commit();
        }
    }
}
