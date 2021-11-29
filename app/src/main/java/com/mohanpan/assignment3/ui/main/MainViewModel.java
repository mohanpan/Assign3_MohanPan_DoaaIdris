package com.mohanpan.assignment3.ui.main;

import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainViewModel extends ViewModel {

    public ArrayList<String> meetings;
    public ArrayAdapter<String> adapter;
    public ArrayList<String[]> meetingsContent;
    public int selectedMeeting;

}