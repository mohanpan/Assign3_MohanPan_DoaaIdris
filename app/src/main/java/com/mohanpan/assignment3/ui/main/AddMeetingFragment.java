package com.mohanpan.assignment3.ui.main;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.mohanpan.assignment3.NotificationBroadcaster;
import com.mohanpan.assignment3.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import com.google.gson.Gson;

import java.util.Calendar;


@RequiresApi(api = Build.VERSION_CODES.O)
public class AddMeetingFragment extends Fragment {

    EditText meetingNameInput;
    EditText meetingNotes;
    EditText meetingAttendiesInput;
    EditText dateInput;
    EditText timeInput;
    EditText locationInput;

    EditText notificationDate;
    String notificationDateText;
    String notificationTimeText;

    Button addButton;
    ImageButton backButton;

    DatePickerDialog notiDatePicker;
    TimePickerDialog notiTimePicker;

    DatePickerDialog datePicker;
    TimePickerDialog timePicker;

    RadioGroup meetingPriority;
    RadioGroup meetingType;

    RadioButton[] rbs = new RadioButton[4];
    RadioButton[] rbs2 = new RadioButton[3];


    private AddMeetingViewModel addMeetingViewModel;
    private MainViewModel mViewModel;

    //FOR NOTIFICATIONS
    private static final String CHANNEL = "default";
    private static final int NOTIFICATION_DEFAULT = 1100;
    int notificationId;
    NotificationManager notificationManager;

    Calendar c = Calendar.getInstance();

    public static AddMeetingFragment newInstance() {
        return new AddMeetingFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.add_meeting_fragment, container, false);

        addMeetingViewModel = new ViewModelProvider(getActivity()).get(AddMeetingViewModel.class);

        mViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        meetingNameInput = root.findViewById(R.id.nameDetail);
        meetingNotes = root.findViewById(R.id.notesDetail);
        meetingAttendiesInput = root.findViewById(R.id.attendeesDetail);
        dateInput = root.findViewById(R.id.dateDetail);
        timeInput = root.findViewById(R.id.timeDetail);
        locationInput = root.findViewById(R.id.locationDetail);
        notificationDate = root.findViewById(R.id.notificationDetail);

        meetingPriority = root.findViewById(R.id.priorityRadioGroup);
        meetingType = root.findViewById(R.id.typeRadioGroup);

        rbs[0] = root.findViewById(R.id.noneButton);
        rbs[1] = root.findViewById(R.id.lowButton);
        rbs[2] = root.findViewById(R.id.mediumButton);
        rbs[3] = root.findViewById(R.id.highButton);

        rbs2[0] = root.findViewById(R.id.workButton);
        rbs2[1] = root.findViewById(R.id.schoolButton);
        rbs2[2] = root.findViewById(R.id.otherButton);

        addButton = root.findViewById(R.id.doneButton);
        backButton = root.findViewById(R.id.backBtn);

        notificationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int day = c.get(Calendar.DAY_OF_MONTH);
                int month = c.get(Calendar.MONTH);
                int year = c.get(Calendar.YEAR);

                notiDatePicker = new DatePickerDialog(root.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                int mHour = c.get(Calendar.HOUR_OF_DAY);
                                int mMinute = c.get(Calendar.MINUTE);

                                notiTimePicker = new TimePickerDialog(root.getContext(),
                                        new TimePickerDialog.OnTimeSetListener() {

                                            @Override
                                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                                                notificationDateText = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                                                notificationTimeText = hourOfDay + ":" + minute;
                                                notificationDate.setText(notificationDateText + "  " + notificationTimeText);

                                                c.set(year, monthOfYear, dayOfMonth, hourOfDay, minute, 00);
                                            }
                                        }, mHour, mMinute, false);
                                notiTimePicker.show();
                            }
                        }, year, month, day);
                notiDatePicker.show();
            }

        });

        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                datePicker = new DatePickerDialog(root.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                dateInput.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, year, month, day);
                datePicker.show();
            }
        });

        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                timePicker = new TimePickerDialog(root.getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeInput.setText(hourOfDay + ":" + minute);
                    }

                }, mHour, mMinute, false);
                timePicker.show();
            }

        });

        NotificationChannel chn = new NotificationChannel(CHANNEL,
                "CHANNEL",
                NotificationManager.IMPORTANCE_HIGH);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.createNotificationChannel(chn);

        //Add meeting Button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = meetingNameInput.getText().toString();
                String text2 = meetingNotes.getText().toString();
                String text3 = meetingAttendiesInput.getText().toString();
                String text4 = dateInput.getText().toString();
                String text5 = timeInput.getText().toString();
                String text6 = locationInput.getText().toString();
                String text7 = notificationDateText;
                String text8 = notificationTimeText;

                int selectedId = meetingPriority.getCheckedRadioButtonId();
                String text9 = Integer.toString(selectedId);

                int selectedId2 = meetingType.getCheckedRadioButtonId();
                String text10 = Integer.toString(selectedId2);

                //System.out.println(text9);

                //System.out.println(System.currentTimeMillis());
                System.out.println(c.getTimeInMillis());

                if(text == null || text.length() == 0){

                    System.out.println("Enter an item");
                }else{
                    String[] meeting = {text, text2, text3, text4, text5, text6, text7, text8, text9, text10};
                    mViewModel.meetingsContent.add(meeting);
                    String meetingText = meeting[0] + " is at " + meeting[5] + "\n" + meeting[3] + " " + meeting[4];
                    int difference = (int) (c.getTimeInMillis() - System.currentTimeMillis());

                    addMeeting(meetingText);

                    if(notificationDateText != null){
                        notificationId = mViewModel.meetings.indexOf(meeting);
                        scheduleNotification(notificationId, "Notification", meetingText, difference);
                    }

                }

                System.out.println(mViewModel.meetingsContent.get(0)[1]);

                saveList();
            }
        });

        //Button to go back
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
        addMeetingViewModel = ViewModelProviders.of(this).get(AddMeetingViewModel.class);
        // TODO: Use the ViewModel
    }

    public void addMeeting(String meeting){

        mViewModel.meetings.add(meeting);
        mViewModel.adapter.notifyDataSetChanged();

    }

    public void saveList() {

        SharedPreferences prefs = getActivity().getSharedPreferences("my_prefs", 0);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mViewModel.meetings);
        editor.putString("meetings", json);
        editor.apply();

    }

    public Notification buildNotification(String title, String body){

        Notification.Builder nb = new Notification.Builder(getActivity(), CHANNEL)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.notification_icon)
                .setAutoCancel(true)
                .setColorized(true)
                .setColor(Color.CYAN);

        Notification noti = nb.build();

        return noti;
    }

    public void scheduleNotification(int id, String title, String notiText, int delay){

        Notification noti = buildNotification(title, notiText);

        Intent notificationIntent = new Intent(getContext(), NotificationBroadcaster.class);
        notificationIntent.putExtra(NotificationBroadcaster.NOTIFICATION_ID, id);
        notificationIntent.putExtra(NotificationBroadcaster.NOTIFICATION, noti);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long myAlarmTime =  System.currentTimeMillis() + delay;
        AlarmManager al = (AlarmManager)getActivity().getSystemService(Context.ALARM_SERVICE);
        al.set(AlarmManager.RTC_WAKEUP, myAlarmTime, pendingIntent);
    }


}