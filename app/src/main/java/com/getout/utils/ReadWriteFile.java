package com.getout.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReadWriteFile {

    public static void saveToFile(Context context, String file_name, String content){
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();

        Log.d("date", getDate(tsLong));

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("route-"+ ts + "-" + file_name + ".json", Context.MODE_PRIVATE));
            outputStreamWriter.write(content);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public static String readFromFile(Context context, String file_name){
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput(file_name);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static ArrayList<File> listFiles(Context context){
        File dir = context.getFilesDir();
        File[] subFiles = dir.listFiles();
        ArrayList<File> files = new ArrayList<File>();

        if (subFiles != null)
        {
            for (File file : subFiles)
            {
                if(file.getName().split("-")[0].equals("route"))
                    files.add(file);
            }
        }

        return files;
    }

    private static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis((int) milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
