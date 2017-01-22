package edu.mtu.naoremotecontrol;

import android.app.Application;
import android.support.v4.util.Pair;

import com.aldebaran.qimessaging.CallError;
import com.aldebaran.qimessaging.Session;
import com.aldebaran.qimessaging.helpers.al.ALAnimatedSpeech;
import com.aldebaran.qimessaging.helpers.al.ALAutonomousLife;
import com.aldebaran.qimessaging.helpers.al.ALBattery;
import com.aldebaran.qimessaging.helpers.al.ALBodyTemperature;
import com.aldebaran.qimessaging.helpers.al.ALMotion;
import com.aldebaran.qimessaging.helpers.al.ALPreferences;
import com.aldebaran.qimessaging.helpers.al.ALRobotPosture;
import com.aldebaran.qimessaging.helpers.al.ALSystem;
import com.aldebaran.qimessaging.helpers.al.ALTextToSpeech;

import java.util.List;

/**
 * Created by EricMVasey on 10/27/2016.
 */

public class NaoRemoteControlApplication extends Application
{
    private Session naoSession;
    private ALAnimatedSpeech naoAnimatedSpeech;
    private ALMotion naoMotion;
    private ALAutonomousLife naoAutonomousLife;
    private ALSystem naoSystem;
    private ALBattery naoBattery;
    private ALBodyTemperature naoBodyTemperature;
    private ALTextToSpeech naoTextToSpeech;
    private ALPreferences naoPreferences;
    private ALRobotPosture naoPosture;

    public boolean connect(String ip)
    {
        try
        {
            naoSession = new Session(ip);

            naoAnimatedSpeech = new ALAnimatedSpeech(naoSession);
            naoMotion = new ALMotion(naoSession);
            naoPosture = new ALRobotPosture(naoSession);
            naoAutonomousLife = new ALAutonomousLife(naoSession);
            naoSystem = new ALSystem(naoSession);
            naoBattery = new ALBattery(naoSession);
            naoBodyTemperature = new ALBodyTemperature(naoSession);
            naoTextToSpeech = new ALTextToSpeech(naoSession);

        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    public List<String> getGestures()
    {
        return null;
    }

    public List<String> getPrograms()
    {
        return null;
    }

    public List<String> getPoses()
    {
        return null;
    }

    public void runCommand(Pair<String, String[]> command) throws CallError, InterruptedException
    {
        if(command.first.equals("ALAnimatedSpeech"))
        {
            naoAnimatedSpeech.say(command.second[0]);
        }
        else if(command.first.equals("ALRobotPosture"))
        {
            naoPosture.goToPosture(command.second[0], Float.parseFloat(command.second[1]));
        }
    }
}
