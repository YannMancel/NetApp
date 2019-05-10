package com.mancel.yann.netapp.controller.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mancel.yann.netapp.R;
// import com.mancel.yann.netapp.controller.fragments.MainFragment;
import com.mancel.yann.netapp.controller.fragments.MainFragmentWithRecyclerView;

public class MainActivity extends AppCompatActivity {

    // FIELDS --------------------------------------------------------------------------------------

    private MainFragmentWithRecyclerView mMainFragment;

    // METHODS -------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associates the layout file to this class
        setContentView(R.layout.activity_main);

        // Configures and shows the main fragment
        this.configureAndShowMainFragment();
    }

    /**
     * Configures and shows the main fragment
     */
    private void configureAndShowMainFragment() {
        // Creates a Fragment object to retrieve the fragment [FragmentManager -> Fragment]
        this.mMainFragment = (MainFragmentWithRecyclerView) getSupportFragmentManager().findFragmentById(R.id.activity_main_frame_layout);

        // If the fragment is not displayed
        if (this.mMainFragment == null) {
            // Creates a MainFragment object
            this.mMainFragment = new MainFragmentWithRecyclerView();

            // Adds the transaction to create the fragment [FragmentManager -> FragmentTransaction -> int]
            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.activity_main_frame_layout, this.mMainFragment)
                                       .commit();
        }
    }
}
