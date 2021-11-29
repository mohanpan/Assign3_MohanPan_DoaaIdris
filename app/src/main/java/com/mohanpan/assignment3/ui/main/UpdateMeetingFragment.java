package com.mohanpan.assignment3.ui.main;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mohanpan.assignment3.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.google.gson.Gson;

public class UpdateMeetingFragment extends Fragment {

    private UpdateMeetingViewModel updateViewModel;
    private MainViewModel mViewModel;

    EditText updateName;
    EditText updateLoc;
    EditText updateMeetingNotes;
    EditText updateMeetingAttendies;
    EditText updatedate;
    EditText updateTime;

    EditText updateNotificationDate;
    String updateNotificationDateText;
    String updateNotificationTimeText;

    DatePickerDialog notiDatePicker;
    TimePickerDialog notiTimePicker;

    DatePickerDialog datePicker;
    TimePickerDialog timePicker;

    RadioGroup meetingPriority;
    RadioGroup meetingType;

    RadioButton[] rbs = new RadioButton[4];
    RadioButton[] rbs2 = new RadioButton[3];

    ImageButton deleteButton;
    Button updateButton;
    ImageButton backButton;

    public static UpdateMeetingFragment newInstance() {
        return new UpdateMeetingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.update_meeting_fragment, container, false);

        updateViewModel = new ViewModelProvider(getActivity()).get(UpdateMeetingViewModel.class);
        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        updateName = root.findViewById(R.id.updateMeeting);
        updateLoc = root.findViewById(R.id.updateLocation);
        updateMeetingNotes = root.findViewById(R.id.updateNote);
        updateMeetingAttendies = root.findViewById(R.id.updateAttendees);
        updatedate = root.findViewById(R.id.updateDate);
        updateTime = root.findViewById(R.id.updateTime);

        updateNotificationDate = root.findViewById(R.id.updateNotification);

        meetingPriority = root.findViewById(R.id.priorityRadioGroup);
        meetingType = root.findViewById(R.id.typeRadioGroup);

        rbs[0] = root.findViewById(R.id.noneButton);
        rbs[1] = root.findViewById(R.id.lowButton);
        rbs[2] = root.findViewById(R.id.mediumButton);
        rbs[3] = root.findViewById(R.id.highButton);

        rbs2[0] = root.findViewById(R.id.workButton);
        rbs2[1] = root.findViewById(R.id.schoolButton);
        rbs2[2] = root.findViewById(R.id.otherButton);

        deleteButton = root.findViewById(R.id.deleteBtn);
        updateButton = root.findViewById(R.id.doneButton);
        backButton = root.findViewById(R.id.backBtn2);

        Bundle mBundle = new Bundle();
        mBundle = getArguments();
        mViewModel.selectedMeeting = mBundle.getInt("selectedIndex");
        System.out.println(mViewModel.selectedMeeting);

        int index = mViewModel.selectedMeeting;

        System.out.println(mViewModel.meetingsContent.get(0)[1].toString());

//        String notesUpdate = mViewModel.meetingsContent.get(index)[1];
//        String attendiesUpdate = mViewModel.meetingsContent.get(index)[2];
//        String dateUpdate = mViewModel.meetingsContent.get(index)[3];
//        String timeUpdate = mViewModel.meetingsContent.get(index)[4];
//        String locationUpdate = mViewModel.meetingsContent.get(index)[5];
//        String notiDateUpdate = mViewModel.meetingsContent.get(index)[6];
//        String notiTimeUpdate = mViewModel.meetingsContent.get(index)[7];
//        String priorityUpdate = mViewModel.meetingsContent.get(index)[8];
//        String typeUpdate = mViewModel.meetingsContent.get(index)[9];

//        updateName.setText(nameUpdate);
//        updateLoc.setText(locationUpdate);
//        updateMeetingNotes.setText(notesUpdate);
//        updateMeetingAttendies.setText(attendiesUpdate);
//        updatedate.setText(dateUpdate);
//        updateTime.setText(timeUpdate);
//        updateNotificationDateText = notiDateUpdate;
//        updateNotificationTimeText = notiTimeUpdate;
//        updateNotificationDate.setText(updateNotificationDateText + updateNotificationTimeText);
//
//        rbs[Integer.parseInt(priorityUpdate)].setChecked(true);
//        rbs[Integer.parseInt(typeUpdate)].setChecked(true);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if((nameUpdate.equals(updateName.getText().toString())) == true){
//                    mViewModel.meetings.remove(mViewModel.meetings.get(mViewModel.selectedMeeting));
//                    mViewModel.meetingsContent.remove(mViewModel.selectedMeeting);
//                    mViewModel.adapter.notifyDataSetChanged();
//                    updateName.setText("");
//                    updateLoc.setText("");
//                }
                saveList();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(updateName.getText().toString() != null){
                    mViewModel.meetings.set(mViewModel.selectedMeeting, updateName.getText().toString());
                    mViewModel.adapter.notifyDataSetChanged();
                }
                saveList();

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savedInstanceState == null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, MainFragment.newInstance())
                            .commitNow();
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateViewModel= ViewModelProviders.of(this).get(UpdateMeetingViewModel.class);
        // TODO: Use the ViewModel
    }

    public void saveList() {

        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mViewModel.meetings);
        editor.putString("meetings", json);
        editor.apply();

    }

}