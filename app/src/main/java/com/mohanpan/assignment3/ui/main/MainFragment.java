package com.mohanpan.assignment3.ui.main;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mohanpan.assignment3.R;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    public ListView meetingList;

    FloatingActionButton addMeetingButton;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        View root = inflater.inflate(R.layout.main_fragment, container, false);

        if (savedInstanceState != null) {
            mViewModel.selectedMeeting = savedInstanceState.getInt("selectedIndex", mViewModel.selectedMeeting);
        }

        addMeetingButton = root.findViewById(R.id.addNewMeeting);
        meetingList = root.findViewById(R.id.meetingList);

        loadExistingMeetings();

        if (mViewModel.meetings == null) {
            mViewModel.meetings = new ArrayList<>();
        }

        mViewModel.meetingsContent = new ArrayList<>();

        addMeetingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (savedInstanceState == null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, AddMeetingFragment.newInstance())
                            .commitNow();

                }

            }
        });

        mViewModel.adapter = new ArrayAdapter<String>(root.getContext(), android.R.layout.simple_list_item_1, mViewModel.meetings);

        meetingList.setAdapter(mViewModel.adapter);

        meetingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                mViewModel.selectedMeeting = i;
                System.out.println( mViewModel.selectedMeeting);
                Bundle bundle = new Bundle();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                bundle.putInt("selectedIndex", mViewModel.selectedMeeting);
                UpdateMeetingFragment llf = new UpdateMeetingFragment();
                llf.setArguments(bundle);
                ft.replace(R.id.container, llf);
                ft.commit();

            }
        });

        return root;
    }


    private void loadExistingMeetings() {
        //Restore Meeting List
        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", 0);
        Gson gson = new Gson();
        String json = prefs.getString("meetings", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        mViewModel.meetings = gson.fromJson(json, type);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*FloatingActionButton addNewMeeting = findViewById(R.id.addNewMeeting);
        addNewMeeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });    */
    }
}