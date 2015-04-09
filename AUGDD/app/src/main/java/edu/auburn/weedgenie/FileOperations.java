package edu.auburn.weedgenie;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Created by zachary on 3/10/15.
 */
public class FileOperations {

    public static List<ListItem> readPlantsFromFile(Context context, String tag) {
        List<ListItem> list = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(context.getPackageName() + tag);
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    fileInputStream);
            list = (List<ListItem>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void writePlantsToFile(Context context, List<ListItem> list, String tag) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(
                    context.getPackageName() + tag, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<WeatherItem> readWeatherFromFile(Context context, String tag) {
        List<WeatherItem> list = null;
        try {
            FileInputStream fileInputStream = context.openFileInput(context.getPackageName() + tag);
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    fileInputStream);
            list = (List<WeatherItem>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void writeWeatherToFile(Context context, List<WeatherItem> items, String tag) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(
                    context.getPackageName() + tag, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                    fileOutputStream);
            objectOutputStream.writeObject(items);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
