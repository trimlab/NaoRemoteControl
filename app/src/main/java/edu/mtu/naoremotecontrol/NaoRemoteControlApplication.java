package edu.mtu.naoremotecontrol;

import android.app.Application;
import android.support.v4.util.Pair;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALAudioDevice;
import com.aldebaran.qi.helper.proxies.ALAutonomousLife;
import com.aldebaran.qi.helper.proxies.ALBattery;
import com.aldebaran.qi.helper.proxies.ALBehaviorManager;
import com.aldebaran.qi.helper.proxies.ALBodyTemperature;
import com.aldebaran.qi.helper.proxies.ALMemory;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALPreferences;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALSystem;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

import java.util.Arrays;
import java.util.List;

/**
 * Created by EricMVasey on 10/27/2016.
 */

public class NaoRemoteControlApplication extends Application
{
    public enum WalkDirection
    {
        FORWARD("forward", 0.040f, 0.0f, 0),
        BACKWARD("backward", -0.040f, 0.0f, 0),
        RIGHT("right", 0, 0.140f, (float) (-1 * Math.PI/2.0f)),
        LEFT("left", 0, 0.140f, (float) (Math.PI/2.0f));

        private String toString;
        private float x,y, theta;

        WalkDirection(String str, float x, float y, float theta)
        {
            this.toString = str;
            this.x = x;
            this.y = y;
            this.theta = theta;
        }

        public float getX()
        {
            return x;
        }

        public float getY()
        {
            return y;
        }

        public float getTheta()
        {
            return theta;
        }

        public String toString()
        {
            return toString;
        }
    }

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
    private ALAudioDevice naoAudioDevice;
    private ALMemory naoMemory;
    private ALBehaviorManager naoBehaviorManager;

    public boolean connect(String ip)
    {
        try
        {
            naoSession = new Session(ip);

            naoAnimatedSpeech = new ALAnimatedSpeech(naoSession);
            naoMotion = new ALMotion(naoSession);
            naoPosture = new ALRobotPosture(naoSession);
            naoAutonomousLife = new ALAutonomousLife(naoSession);
            naoAutonomousLife.setState("disabled");
            naoSystem = new ALSystem(naoSession);
            naoBattery = new ALBattery(naoSession);
            naoBodyTemperature = new ALBodyTemperature(naoSession);
            naoTextToSpeech = new ALTextToSpeech(naoSession);
            naoBattery = new ALBattery(naoSession);
            naoAudioDevice = new ALAudioDevice(naoSession);
            naoMemory = new ALMemory(naoSession);

            naoBehaviorManager = new ALBehaviorManager(naoSession);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void disconnect() throws InterruptedException, CallError
    {
        naoAutonomousLife.setState("solitary");
        naoSession.close();
    }

    public int getBatteryLevel()
            throws Exception
    {
        return naoBattery.getBatteryCharge();
    }

    public String getCPUTemperature()
            throws Exception
    {
        return naoMemory.getData("Device/SubDeviceList/Head/Temperature/Sensor/Value").toString();
    }

    public List<String> getGestures()
    {

        return Arrays.asList(
                "affirmative", "alright", "beg", "beseech", "body language", "bow", "call", "clear", "enthusiastic", "entreat",
                "explain", "happy", "hello", "hey", "hi", "I", "implore", "indicate", "me", "my",
                "myself", "negative", "no", "not know", "ok", "oppose", "please", "present", "rapturous", "raring",
                "refute", "reject", "rousing", "show", "supplicate", "unacquainted", "undetermined", "undiscovered", "unfamiliar", "unknown",
                "warm", "yeah", "yes", "yoo-hoo", "you", "your", "zestful"
        );
    }

    public Float getGlobalSpeechRate()
            throws Exception
    {
        return naoTextToSpeech.getParameter("speed");
    }

    public int getGlobalVolume()
            throws Exception
    {
        return naoAudioDevice.getOutputVolume();
    }

    public List<String> getPoses()
    {
        return Arrays.asList(
                "Crouch", "LyingBack", "LyingBelly", "Sit", "SitRelax", "Stand"
        );
    }

    public List<String> getPosesTitles()
    {
        return Arrays.asList(
                "Crouch", "Lying Back", "Lying Belly", "Sit", "Sit Relax", "Stand"
        );
    }

    public List<String> getPrograms() throws InterruptedException, CallError
    {
        return naoBehaviorManager.getUserBehaviorNames();
    }

    public void runProgram(String name) throws InterruptedException, CallError
    {
        naoBehaviorManager.runBehavior(name);
    }

    public void stopProgram(String name) throws InterruptedException, CallError
    {
        naoBehaviorManager.stopBehavior(name);
    }

    public void runCommand(Pair<String, String[]> command) throws CallError, InterruptedException
    {
        if(command.first.equals("ALAnimatedSpeech"))
        {
            say(command.second[0]);
        }
        else if(command.first.equals("ALRobotPosture"))
        {
            naoPosture.goToPosture(command.second[0], Float.parseFloat(command.second[1])/100.0f);
        }
        else if(command.first.equals("ALMotion"))
        {
            NaoRemoteControlApplication.WalkDirection direction = null;

            if(command.second[0].equals("Forward"))
            {
                direction = WalkDirection.FORWARD;
            }
            else if(command.second[0].equals("Left"))
            {
                direction = WalkDirection.LEFT;
            }
            else if(command.second[0].equals("Right"))
            {
                direction = WalkDirection.RIGHT;
            }
            else if(command.second[0].equals("Backward"))
            {
                direction = WalkDirection.BACKWARD;
            }

            if(direction != null)
                naoMotion.walkTo(direction.getX(), direction.getY(), direction.getTheta());
        }
    }

    public void setAutomaticAnimationEnabled(boolean enabled)
            throws Exception
    {
        if (enabled)
        {
            naoAnimatedSpeech.setBodyLanguageModeFromStr("contextual");
        }
        else
        {
            naoAnimatedSpeech.setBodyLanguageModeFromStr("disabled");
        }
    }

    public void setGlobalSpeechRate(float f)
            throws Exception
    {
        naoTextToSpeech.setParameter("speed", Float.valueOf(f / 100F));
    }

    public void setGlobalVolume(int i)
            throws Exception
    {
        naoAudioDevice.setOutputVolume(Integer.valueOf(i / 100));
    }

    public void say(String text) throws InterruptedException, CallError
    {
        naoAnimatedSpeech.say(text);
    }

    public void move(WalkDirection direction) throws InterruptedException, CallError
    {
        naoMotion.move(direction.getX(), direction.getY(), direction.getTheta());
    }
}
