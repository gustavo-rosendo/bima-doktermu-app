package com.bima.dokterpribadimu.utils;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by gustavo.santos on 12/23/2016.
 */

public class LogUtils {

    private static final String TAG = LogUtils.class.getSimpleName();

    public static void printLogToFile(){
        String extr = Environment.getExternalStorageDirectory().toString();

        String filename = extr + "/Doktermu_" + System.currentTimeMillis() +  ".log";

        Log.d(TAG, "printLog() - filename: " + filename);

        String command = "logcat -d *:V";

        Log.d(TAG, "command: " + command);

        try{
            Process process = Runtime.getRuntime().exec(command);

            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            try{
                File file = new File(filename);
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                while((line = in.readLine()) != null){
                    writer.write(line + "\n");
                }
                writer.flush();
                writer.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
