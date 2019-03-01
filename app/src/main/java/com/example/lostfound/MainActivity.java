package com.example.lostfound;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

public class MainActivity extends SingleFragmentActivity {

    private static final String EXTRA_LOST_ID =
            "com.example.lostfound.lost_id";

    public static Intent newIntent(Context packageContext, UUID lostId) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_LOST_ID, lostId);
        return intent;
    }

    @Override
    protected Fragment createFragment() {
        UUID lostId = (UUID) getIntent().getSerializableExtra(EXTRA_LOST_ID);
        return LostFragment.newInstance(lostId);
    }
}
