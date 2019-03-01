package com.example.lostfound;

import android.support.v4.app.Fragment;

public class LostListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new LostListFragment();
    }
}
