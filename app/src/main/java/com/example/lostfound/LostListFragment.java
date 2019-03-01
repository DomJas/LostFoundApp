package com.example.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LostListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    //Fixes rotational issue

    private class LostAdapter extends RecyclerView.Adapter<LostHolder> {

        private List<Lost> mLosts;

        public LostAdapter(List<Lost> losts) {
            mLosts = losts;
        }

        @Override
        public LostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from((getActivity()));
            return new LostHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(LostHolder holder, int position) {
            Lost lost = mLosts.get(position);
            holder.bind(lost);

        }

        @Override
        public int getItemCount() {
            return mLosts.size();
        }

        public void setLosts(List<Lost> losts) {
            mLosts = losts;

            //This allows the losts to be displayed and swapped out from setLost(List<Lost>) to LostAdapter
        }
    }

    private RecyclerView mLostRecycleView;
    private LostAdapter mAdapter;
    private boolean mSubtitleVisable;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class LostHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Lost mLost;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mFoundImageView;


        public LostHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_lost, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.lost_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.lost_date);
            mFoundImageView = (ImageView) itemView.findViewById(R.id.lost_found);
        }

        @Override
        public void onClick(View view) {
            Intent intent = LostPagerActivity.newIntent(getActivity(), mLost.getId());
            startActivity(intent);
        }

        public void bind(Lost lost) {
            mLost = lost;
            mTitleTextView.setText(mLost.getTitle());
            mDateTextView.setText(mLost.getDate().toString());
            mFoundImageView.setVisibility(lost.isFound() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lost_list, container, false);
        mLostRecycleView = (RecyclerView) view
                .findViewById(R.id.lost_recycler_view);
        mLostRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState !=null) {
            mSubtitleVisable = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
                    //Fix rotational issue
        }

        updateUI();

        return view;

    }


    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisable);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_lost_list, menu);
        //This is where Android calls Activity method for the menu (onCreateOptionMenu)

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
        if (mSubtitleVisable) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_lost:
                Lost lost = new Lost();
                LostLab.get(getActivity()).addLost(lost);
                Intent intent = LostPagerActivity
                        .newIntent(getActivity(), lost.getId());
                startActivity(intent);
                return true;
            case R.id.show_subtitle:
                mSubtitleVisable = !mSubtitleVisable;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
                //Part of the display that shows lost items
            default:
                return super.onOptionsItemSelected(item);
                //This whole piece is the code that creates a new lost item by adding it to LostLabs then starting an instance of LostPagerActivity to edit the new Lost item
        }
    }

    private void updateSubtitle() {
        LostLab lostLab = LostLab.get(getActivity());
        int lostCount = lostLab.getLosts().size();
        String subtitle = getString(R.string.subtitle_format, lostCount);

        if (!mSubtitleVisable) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
        //This lets users show/hide a display showing the number of lost items
    }

    private void updateUI() {
        LostLab lostLab = LostLab.get((getActivity()));
        List<Lost> losts = lostLab.getLosts();

        if (mAdapter == null) {
            mAdapter = new LostAdapter(losts);
            mLostRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.setLosts(losts);
            mAdapter.notifyDataSetChanged();
            //This setLosts(List<Lost>) in the updateUI()
        }
        updateSubtitle();

    }
}
