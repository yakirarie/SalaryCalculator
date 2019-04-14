package com.example.yakirlaptop.salarycalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;


import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class Tab2Fragment extends Fragment {
    DatabaseOpenHelper dbhelper;
    AlertDialog.Builder builderSingle;
    String day = "";
    String month = "";
    String year = "";
    RecyclerView recyclerView;
    DataItemAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<DataItem> dataItemList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_hours,container,false);
        recyclerView = view.findViewById(R.id.recyclerView);
        RelativeLayout relativeLayout = view.findViewById(R.id.layout_f2);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
        populateList();
        return view;
    }
    private void populateList() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        dbhelper = new DatabaseOpenHelper(getContext());
        dataItemList = dbhelper.getMonthsRecyclerView();
        adapter = new DataItemAdapter(dataItemList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new DataItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final String get_month = extractmonth(dataItemList.get(position).getmText1());
                ArrayList<String> data = dbhelper.getDataForMonth(get_month);
                final ListAdapter adapter2 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,data);
                builderSingle = new AlertDialog.Builder(getContext());
                builderSingle.setTitle(R.string.Click_To_Delete);
                builderSingle.setPositiveButton(R.string.calculate_salary, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                        builderInner.setTitle("משכורתך לחודש "+dbhelper.getMonthInHebrew(get_month)+" היא "+dbhelper.getSalary(get_month));
                        builderInner.setNegativeButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                    }
                });
                builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builderSingle.setAdapter(adapter2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = (String)adapter2.getItem(which);
                        AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                        builderInner.setTitle(R.string.Delete_This_Day);
                        builderInner.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dbhelper.deleteDay(day,month,year);
                                Toasty.success(getContext(), R.string.Day_deleted, Toasty.LENGTH_LONG).show();
                                populateList();
                                dialog.dismiss();
                            }
                        });
                        builderInner.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                        builderInner.show();
                        String date = extractname(data);
                        String[] split = date.split("/");
                        day=split[0];
                        month=split[1];
                        year=split[2];
                    }
                });
                builderSingle.show();
            }

            @Override
            public void onDeleteClick(int position) {
                final String get_month = extractmonth(dataItemList.get(position).getmText1());
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
                builderInner.setTitle(R.string.Delete_This_Month);
                builderInner.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dbhelper.deleteAll(get_month);
                        Toasty.success(getContext(), R.string.Month_deleted, Toasty.LENGTH_LONG).show();
                        populateList();
                        dialog.dismiss();
                    }
                });
                builderInner.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }

        });
    }

    private String extractname(String name){
        String[] split = name.split("\n");
        String split2 = "";
        split2 = split[0].split(" ")[1];
        return split2;
    }
    private String extractmonth(String month){
        String[] split = month.split(" - ");
        return split[0];
    }

    @Override
    public void onResume() {
        super.onResume();
        populateList();

    }

}