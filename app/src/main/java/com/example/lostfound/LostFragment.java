package com.example.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

import static android.widget.CompoundButton.*;

public class LostFragment extends Fragment {

    private static final String ARG_LOST_ID = "lost_id";
    private static final String DIALOG_DATE = "DialogDate";

    private static final int REQUEST_DATE = 0;
    private static final int  REQUEST_CONTACT = 1;
    //calls for data from DataPickerFragment

    private Lost mLost;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mFoundCheckBox;
    private Button mSuspectButton;
    private Button mReportButton;

    public static LostFragment newInstance(UUID lostId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_LOST_ID, lostId);

        LostFragment fragment = new LostFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID lostId = (UUID) getArguments().getSerializable(ARG_LOST_ID);
        mLost = LostLab.get(getActivity()).getLost(lostId);
    }
    //This is To Show Menu Bar

    @Override
    public void onPause() {
        super.onPause();

        LostLab.get(getActivity())
                .updateLost(mLost);
    }
    //updates "lost instance" when ever modifications in LostFragment  occur

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_lost, container, false);
                mDateButton = (Button) v.findViewById(R.id.lost_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mLost.getDate());
                dialog.setTargetFragment(LostFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mFoundCheckBox = (CheckBox)v.findViewById(R.id.lost_found);
        mFoundCheckBox.setChecked(mLost.isFound());
        mFoundCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mLost.setFound(isChecked);
            }
        });


        mTitleField = (EditText) v.findViewById(R.id.lost_title);
        mTitleField.setText(mLost.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(
                    CharSequence s, int start, int before, int count) {
                mLost.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });


        mReportButton = (Button) v.findViewById(R.id.lost_report);
        mReportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("Text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getLostReport());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.lost_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        mSuspectButton = (Button) v.findViewById(R.id.lost_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        if (mLost.getSuspect() !=null) {
            mSuspectButton.setText(mLost.getSuspect());
        }

        return v;
    }

    private void updateDate() {
        mDateButton.setText(mLost.getDate().toString());
    }

    private String getLostReport(){
        String solvedString = null;
        if (mLost.isFound()) {
            solvedString = getString(R.string.lost_report_found);
        } else {
            solvedString = getString(R.string.lost_report_notfound);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mLost.getDate()).toString();

        String suspect = mLost.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.lost_report_no_suspect);
        } else {
            suspect = getString(R.string.lost_report_suspect, suspect);
        }

        String report = getString(R.string.lost_report, mLost.getTitle(), dateString, solvedString, suspect);

        return report;

        //lines 112-132 are the preliminaries and they are complete so now I can run implicit intents
        //Intent communicates with the operating system and tells it what job you want done
    }
}
