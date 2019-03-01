package com.example.lostfound;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class LostPagerActivity extends AppCompatActivity {
    private static final String EXTRA_LOST_ID =
            "com.example.lostfound.lost_id";

    private  ViewPager mViewPager;
    private List<Lost> mLosts;

    public static Intent newIntent(Context packageContext, UUID lostId) {
        Intent intent = new Intent(packageContext, LostPagerActivity.class);
        intent.putExtra(EXTRA_LOST_ID, lostId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_pager);

        UUID lostId = (UUID) getIntent().getSerializableExtra(EXTRA_LOST_ID);

        mViewPager = (ViewPager) findViewById(R.id.lost_view_pager);

        mLosts = LostLab.get(this).getLosts();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Lost lost = mLosts.get(position);
                return LostFragment.newInstance(lost.getId());
            }

            @Override
            public int getCount() {
                return mLosts.size();
            }
        });

        for (int i = 0; i < mLosts.size(); i++) {
            if (mLosts.get(i).getId().equals(lostId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
