package edu.mtu.naoremotecontrol;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricMVasey on 10/23/2016.
 */

public class Script
{
    private Context c;

    public Script(Context c)
    {
        this.c = c;
    }

    private String getSdCardDir()
    {
        String mExternalDirectory = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

        File externDir = new File(mExternalDirectory+"/nao_scripts/");

        if(!externDir.exists())
            externDir.mkdir();

        return externDir.getAbsolutePath();
    }

    public List<String> read(String fileName) throws IOException
    {
        List<String> ret = new ArrayList<>();
        JsonReader reader = new JsonReader(new FileReader(getSdCardDir()+"/"+fileName));
        reader.beginArray();

        while(reader.hasNext())
        {
            reader.beginObject();

            StringBuilder sb = new StringBuilder();

            while(reader.hasNext())
            {
                sb.append(reader.nextString());
            }

            ret.add(sb.toString());
            reader.endObject();
        }

        return ret;
    }

    public boolean write(List<String> entries, String fileName) throws IOException
    {
        JsonWriter writer = new JsonWriter(new FileWriter(getSdCardDir()+"/"+fileName));
        writer.setIndent("    ");
        writer.beginArray();
        for(String entry: entries.subList(0,entries.size()-1))
        {
            String type = entry.substring(0, entry.indexOf(':'));

            String data = entry.substring(entry.indexOf(':')+2,entry.length()-1);
            String value = null;
            String rate = null;


            if(type.equals("Pose") || type.equals("Gesture"))
            {
                String[] split = data.split("/");
                value = split[0].trim();
                rate = split[1].trim();
            }
            else
                value = data.trim();

            Log.d("Json", type + " " + value);

            writer.beginObject();
            writer.name("type").value(type);
            writer.name("value").value(value);

            if(rate != null)
                writer.name("rate").value(rate);

            writer.endObject();
        }

        writer.endArray();
        writer.close();
        MediaScannerConnection.scanFile(c, new String[]{getSdCardDir()+"/"+fileName}, new String[]{"text/*"}, null);

        return true;
    }
}
