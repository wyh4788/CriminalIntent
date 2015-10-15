package com.bignerdranch.android.criminalintent;

/**
 * Created by stephen on 10/8/15.
 */



import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}