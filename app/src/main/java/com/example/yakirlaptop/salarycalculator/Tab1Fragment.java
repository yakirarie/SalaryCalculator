package com.example.yakirlaptop.salarycalculator;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import java.util.Calendar;

import es.dmoral.toasty.Toasty;


public class Tab1Fragment extends Fragment {
    EditText pay;
    String Day;
    String Month;
    String Year;
    String Hours;
    String Minutes;
    DatabaseOpenHelper dbhelper;
    Button chooseDay;
    Button chooseHours;
    Button chooseHours2;
    Button add;
    Calendar c;
    DatePickerDialog dpd;
    TimePickerDialog tpd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_work_day,container,false);
        chooseDay = view.findViewById(R.id.button_chooseday);
        chooseHours = view.findViewById(R.id.button_choosehours);
        chooseHours2 = view.findViewById(R.id.button_choosehours2);
        add = view.findViewById(R.id.button_add);
        pay = view.findViewById(R.id.ETpay);
        dbhelper = new DatabaseOpenHelper(getContext());
        ConstraintLayout constraintLayout = view.findViewById(R.id.layout_f1);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

        chooseDay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                dpd = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        chooseDay.setText(i2+"/"+(i1+1)+"/"+i);
                    }
                },year,month,day);
                dpd.show();
            }
        });

        chooseHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        chooseHours.setText(String.format("%02d:%02d", i, i1));
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(getContext()));
                tpd.show();
            }
        });

        chooseHours2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                tpd = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        chooseHours2.setText(String.format("%02d:%02d", i, i1));
                    }
                },hour,minute,android.text.format.DateFormat.is24HourFormat(getContext()));
                tpd.show();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chooseDay.getText().toString().equals(getString(R.string.choose_day))|| chooseHours.getText().toString().equals(getString(R.string.start_hour)) || chooseHours2.getText().toString().equals(getString(R.string.end_hour))|| pay.getText().toString().length() == 0){
                    Toasty.info(getContext(), R.string.please_fill_all_of_the_fields,Toasty.LENGTH_LONG).show();
                }
                else{
                    String[] date = chooseDay.getText().toString().split("/");
                    Day = date[0];
                    Month = date[1];
                    Year = date[2];
                    String[] time = chooseHours.getText().toString().split(":");
                    String[] time2 = chooseHours2.getText().toString().split(":");
                    Minutes = time[1];
                    Hours = time[0];
                    double exact_hours = get_exact_hours(Hours,Minutes);
                    Minutes = time2[1];
                    Hours = time2[0];
                    double exact_hours2 = get_exact_hours(Hours,Minutes);
                    double ans = exact_hours2-exact_hours;
                    if (ans<0)
                        Toasty.info(getContext(), R.string.please_enter_valid_ending_hours,Toasty.LENGTH_LONG).show();
                    else {
                        if (dbhelper.addDay(Month, Day + "/" + Month + "/" + Year, String.format("%.2f", ans), pay.getText().toString())) {
                            Toasty.success(getContext(), R.string.added_succesfully,Toasty.LENGTH_LONG).show();
                            MainActivity main = (MainActivity) getActivity();
                            main.getAdapter().getItem(0).onResume();

                        } else
                            Toasty.success(getContext(), R.string.error_occured,Toasty.LENGTH_LONG).show();
                    }

                }
            }
        });


        return view;

    }

    private double get_exact_hours(String hours, String minutes) {
        int h = Integer.parseInt(hours);
        double m = Double.parseDouble(minutes)/60;
        return h+m;
    }






}