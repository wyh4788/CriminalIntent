package com.bignerdranch.android.criminalintent;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * Created by stephen on 10/6/15.
 */
public class CrimeFragment extends Fragment {


    private static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private  static final int REQUEST_DATE = 0;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;

    public  static  CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeID = (UUID) getArguments().getSerializable(CrimeActivity.ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeID);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_del_crime:
                CrimeLab.get(getActivity()).delCrime(mCrime);
                getActivity().finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container,false);
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    mCrime.setTitle(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {  }
                });

                mDateButton = (Button)v.findViewById(R.id.crime_date);
                mDateButton.setText(DateFormat.getDateInstance().format(mCrime.getDate()));  ;
                mDateButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        FragmentManager manager = getFragmentManager();
                        DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                        dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                        dialog.show(manager, DIALOG_DATE);
                    }
                });

                mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
                mSolvedCheckBox.setChecked(mCrime.isSolved());
                mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                        mCrime.setSolved(isChecked);
                    }
                });

                return v;
            }

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data){
                if(resultCode != Activity.RESULT_OK){
                    return;
                }
                if(requestCode == REQUEST_DATE){
                    Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
                    mCrime.setDate(date);
                    mDateButton.setText(mCrime.getDate().toString());
                }
            }



        }