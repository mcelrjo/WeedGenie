package edu.auburn.augdd;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by zachary on 3/10/15.
 */
public class FileOperations {

    public static void writePlantsToFile(Context context, List<ListItem> list){
        Gson gson = new Gson();
        writeToFile(gson.toJson(list), "/example/filepath");
    }

    private static void writeToFile(String json, String filePath) {
        try {
            File myFile = new File(filePath);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(json);
            myOutWriter.close();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("WRITE_TO_FILE", "Unable to write to file");
        }
    }
}
