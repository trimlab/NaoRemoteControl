package edu.mtu.naoremotecontrol;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.support.v4.util.Pair;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Script
{
    private Context c;

    public Script(Context c)
    {
        this.c = c;
    }

    private static String getSdCardDir()
    {
        String mExternalDirectory = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

        File externDir = new File(mExternalDirectory+"/nao_scripts/");

        if(!externDir.exists())
            externDir.mkdir();

        return externDir.getAbsolutePath();
    }

    public static List<String> read(String fileName) throws IOException
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
                String name = reader.nextName();
                sb.append(reader.nextString());

                if(name.equals("type"))
                {
                    sb.append(": ");
                }
                else
                {
                    sb.append("/");
                }
            }

            if(sb.charAt(sb.length()-1) == '/')
                sb.deleteCharAt(sb.length()-1);

            ret.add(sb.toString());
            reader.endObject();
        }

        return ret;
    }

    public static boolean write(List<String> entries, String fileName, Context c) throws IOException
    {
        JsonWriter writer = new JsonWriter(new FileWriter(getSdCardDir()+"/"+fileName));
        writer.setIndent("    ");
        writer.beginArray();
        for(String entry: entries.subList(0,entries.size()-1))
        {
            String type = entry.substring(0, entry.indexOf(':'));

            String data = entry.substring(entry.indexOf(':')+1,entry.length());
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

    public static List<Pair<String, String[]>> toNaoCommandString(List<String> input)
    {
        //Pair<Destination,Data>
        List<String> in = new ArrayList<>(input);
        ArrayList<Pair<String, String[]>> converted = new ArrayList<>();

        for(int i = 0; i < in.size(); i++)
        {
            String entry = in.get(i);

            String type = entry.substring(0, entry.indexOf(':'));
            String data = entry.substring(entry.indexOf(':')+1,entry.length());
            String value = null;
            String rate = null;

            if(type.equals("Pose"))
            {
                String[] split = data.split("/");
                value = split[0].trim();
                rate = split[1].trim();
            }
            else
                value = data.trim();

            if(type.equals("Text"))
                converted.add(new Pair<>("ALAnimatedSpeech", new String[]{value}));
            else if(type.equals("Volume"))
            {
                for(int adjust = i+1; adjust < in.size(); adjust++)
                {
                    String item = in.get(adjust);
                    String itemType = item.substring(0, item.indexOf(':'));

                    if(itemType.equals("Volume"))
                    {
                        break;
                    }

                    if(itemType.equals("Text"))
                    {
                        String itemData = item.substring(item.indexOf(':')+1,item.length());
                        itemData = "\\\\vol=" + value + "\\\\ " + itemData;
                        in.set(adjust, "Text: " + itemData);
                    }
                }
            }
            else if(type.equals("Rate"))
            {
                for(int adjust = i+1; adjust < in.size(); adjust++)
                {
                    String item = in.get(adjust);
                    String itemType = item.substring(0, item.indexOf(':'));

                    if(itemType.equals("Rate"))
                    {
                        break;
                    }

                    if(itemType.equals("Text"))
                    {
                        String itemData = item.substring(item.indexOf(':')+1,item.length());
                        itemData = "\\\\rspd=" + value + "\\\\ " + itemData;
                        in.set(adjust, "Text: " + itemData);
                    }
                }
            }
            else if(type.equals("Pitch"))
            {
                for(int adjust = i+1; adjust < in.size(); adjust++)
                {
                    String item = in.get(adjust);
                    String itemType = item.substring(0, item.indexOf(':'));

                    if(itemType.equals("Pitch"))
                    {
                        break;
                    }

                    if(itemType.equals("Text"))
                    {
                        String itemData = item.substring(item.indexOf(':')+1,item.length());
                        itemData = "\\\\vct=" + value + "\\\\ " + itemData;
                        in.set(adjust, "Text: " + itemData);
                    }
                }
            }
            else if(type.equals("Gesture"))
            {
                converted.add(new Pair<String, String[]>("ALAnimatedSpeech", new String[] {
                        (new StringBuilder()).append("^startTag(")
                                .append(value)
                                .append(") ^waitTag(").append(value).append(")").toString()
                }));

                Log.d("Gesture", value);
            }
            else if(type.equals("Walk"))
            {
                converted.add(new Pair<String, String[]>("ALMotion", new String[]{value}));
            }
            else if(type.equals("Pose"))
            {
                converted.add(new Pair<>("ALRobotPosture", new String[]{value, rate}));
            }
            else if(type.equals("Behavior"))
            {
                converted.add(new Pair<>("ALBehaviorManager", new String[]{value}));
            }

            List<Pair<String, String[]>> result = converted;

            StringBuilder sb = new StringBuilder();
            for(Pair pair: result)
            {
                sb.append(pair.first);
                sb.append(": ");
                sb.append(Arrays.toString((Object[]) pair.second));
                sb.append("\n");
            }
        }

        return converted;
    }
}