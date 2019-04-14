package com.example.yakirlaptop.salarycalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String[] TABLE_NAMES = {
            new String("January"),
            new String("February"),
            new String("March"),
            new String("April"),
            new String("May"),
            new String("June"),
            new String("July"),
            new String("August"),
            new String("September"),
            new String("October"),
            new String("November"),
            new String("December")};
    private static final String[] TABLE_COLUMNS = {new String("Day"),new String("Hours"),new String("pay")};

    public DatabaseOpenHelper(Context context) {
        super(context, "db",null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (int i=0;i<12;i++){
            String createTable = "CREATE TABLE "+TABLE_NAMES[i]+" ("+TABLE_COLUMNS[0]+" TEXT,"+TABLE_COLUMNS[1]+" TEXT,"+TABLE_COLUMNS[2]+" TEXT)";
            sqLiteDatabase.execSQL(createTable);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        for (int ind=0;ind<TABLE_NAMES.length;ind++){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAMES[ind]);

        }
        onCreate(sqLiteDatabase);
    }

    public boolean addDay(String month, String day ,String hours, String pay){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TABLE_COLUMNS[0],day);
        cv.put(TABLE_COLUMNS[1],hours);
        cv.put(TABLE_COLUMNS[2],pay);

        long result = db.insert(TABLE_NAMES[Integer.parseInt(month)-1],null,cv);
        if (result == -1){
            return false;
        }
        return true;
    }

    public ArrayList<String> getDataForMonth(String month){
        String s = "";
        ArrayList<String> ans = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAMES[Integer.parseInt(month)-1];
        Cursor data = db.rawQuery(query,null);
        while(data.moveToNext()) {
            s = ("יום: "+data.getString(0)+"\n" + "שכר שעתי(₪): " + data.getString(2) +"\n" +"שעות: " + data.getString(1)+"\n");
            ans.add(s);
        }

        return ans;

    }



    public ArrayList<String> getMonths(){
        String s = "";
        ArrayList<String> ans = new ArrayList<>();
        Cursor[] data = new Cursor[TABLE_NAMES.length];
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i=0;i<TABLE_NAMES.length;i++){
            String query = "SELECT * FROM "+TABLE_NAMES[i];
            data[i] = db.rawQuery(query,null);
            while(data[i].moveToNext()){
                s = (data[i].getString(0).split("/")[1]+" - "+getMonthInHebrew(data[i].getString(0).split("/")[1]));
            }
            if (s != "")
                ans.add(s);
            s = "";
        }
        return ans;

    }

    public ArrayList<DataItem> getMonthsRecyclerView(){
        String s = "";
        ArrayList<DataItem> ans = new ArrayList<>();
        Cursor[] data = new Cursor[TABLE_NAMES.length];
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i=0;i<TABLE_NAMES.length;i++){
            String query = "SELECT * FROM "+TABLE_NAMES[i];
            data[i] = db.rawQuery(query,null);
            while(data[i].moveToNext()){
                s = (data[i].getString(0).split("/")[1]+" - "+getMonthInHebrew(data[i].getString(0).split("/")[1]));
            }
            if (s != "")
                ans.add(new DataItem(R.drawable.ic_alarm,s,getMonthHours(i)));
            s = "";
        }
        return ans;

    }

    private String getMonthHours(int i){
        double s = 0;
        SQLiteDatabase db = this.getWritableDatabase();
            String query = "SELECT * FROM "+TABLE_NAMES[i];
            Cursor data  = db.rawQuery(query,null);
            while(data.moveToNext()){
                s += Double.parseDouble(data.getString(1));
            }
        return s+" שעות";
    }






    public double getSalary(String month){
        double salary = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAMES[Integer.parseInt(month)-1];
        Cursor data = db.rawQuery(query,null);
        while(data.moveToNext()){
            salary += Double.parseDouble(data.getString(1)) * Double.parseDouble(data.getString(2));
        }
        return salary;

    }

    public void deleteDay(String day,String month,String year) {
        SQLiteDatabase db = this.getWritableDatabase();
        String date = day+"/"+month+"/"+year;
        String query = "DELETE FROM " + TABLE_NAMES[Integer.parseInt(month)-1]+" WHERE "+TABLE_COLUMNS[0]+" = '"+date+"'";
        db.execSQL(query);
    }

    public void deleteAll(String month) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAMES[Integer.parseInt(month)-1];
        db.execSQL(query);
    }

    public String getMonthInHebrew(String month){
        switch (month){
            case "1":
                return "ינואר";
            case "2":
                return "פברואר";
            case "3":
                return "מרץ";
            case "4":
                return "אפריל";
            case "5":
                return "מאי";
            case "6":
                return "יוני";
            case "7":
                return "יולי";
            case "8":
                return "אוגוסט";
            case "9":
                return "ספטמבר";
            case "10":
                return "אוקטובר";
            case "11":
                return "נובמבר";
            default:
                return "דצמבר";
        }
    }

}
