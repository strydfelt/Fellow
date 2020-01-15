package sg.govtech.fellow.log;

import android.os.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class SDLog
{

    private static String appName = "SDLogging";
    private static final String FOLDER = "SDLogging";

    public static boolean[] checkSDState(){
        String state = Environment.getExternalStorageState();
        boolean writeable = false;
        boolean sdcard = false;
        if(state.equals(Environment.MEDIA_MOUNTED)){
            writeable = true;
            sdcard = true;
        }
        else if(state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
            writeable = false;
            sdcard = true;
        }
        else{
            writeable = false;
            sdcard = false;
        }
        return new boolean[] {sdcard, writeable};
    }

    public static boolean isWritable(){
        boolean[] states = checkSDState();
        return states[0] & states[1];
    }

    public static void setAppName(String name){
        appName = name;
    }

    public static void d(String message){
        if(!isWritable()){
            return;
        }

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER  + "/" + appName  +".txt";

        //date stamp for filename
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyyyMMdd_HHmmss");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyyMMdd");
        SimpleDateFormat timedf = new SimpleDateFormat("HH.mm.ss");
        String dateStamp = sdf.format(new Date());
        String timeStamp = timedf.format(new Date());


        File dir = new  File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER);
        dir.mkdirs();
        File file = new  File(dir, appName + "_" + dateStamp +".txt");

        try
        {
            //FileWriter fw = new FileWriter(filePath, true);
            FileWriter fw = new FileWriter(file, true);
//            fw.write(timeStamp);
//            fw.write("     ");
            fw.write(message);
            fw.write("\n");
            fw.flush();
            fw.close();
        }
        catch (IOException e){

        }
    }


}
