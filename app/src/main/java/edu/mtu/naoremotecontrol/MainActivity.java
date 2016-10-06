package edu.mtu.naoremotecontrol;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.aldebaran.qi.CallError;
import com.aldebaran.qi.Session;
import com.aldebaran.qi.helper.proxies.ALAnimatedSpeech;
import com.aldebaran.qi.helper.proxies.ALAudioPlayer;
import com.aldebaran.qi.helper.proxies.ALAutonomousLife;
import com.aldebaran.qi.helper.proxies.ALMotion;
import com.aldebaran.qi.helper.proxies.ALRobotPosture;
import com.aldebaran.qi.helper.proxies.ALTextToSpeech;

public class MainActivity extends AppCompatActivity
{
    private Session session;
    private ALTextToSpeech tts;
    private ALRobotPosture posture;
    private ALAnimatedSpeech animatedSpeech;
    private ALAudioPlayer audioPlayer;
    private ALAutonomousLife autonomousLife;
    private ALMotion navigation;


    private String robotUrl = "141.219.123.186:9559";
    private String robotSSHUsername = "nao";
    private String robotSSHPassword = "";
    private int soundID = -1;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getPreferences(MODE_PRIVATE);

        connectionDialog();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        try
        {
            if(session.isConnected())
                autonomousLife.setState("solitary");
        }
        catch (CallError callError)
        {
            callError.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        session.close();
    }

    private void connectionDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.dialog_pickip, null, false);
        final EditText ipAddress = (EditText) v.findViewById(R.id.ipAddress);
        final EditText sshUname = (EditText) v.findViewById(R.id.sshUsername);
        final EditText sshPass = (EditText) v.findViewById(R.id.sshPassword);
        final CheckBox savePassword = (CheckBox) v.findViewById(R.id.savePassword);
        final CheckBox showPassword =  (CheckBox) v.findViewById(R.id.showPassword);

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {

                if(isChecked)
                {
                    sshPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    sshPass.setSelection(sshPass.length());
                }
                else
                {
                    sshPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    sshPass.setSelection(sshPass.length());
                }
            }
        });

        ipAddress.setText(robotUrl);
        sshUname.setText(robotSSHUsername);

        if(preferences.contains("savedPassword"))
        {
            sshPass.setText(preferences.getString("savedPassword", ""));
            savePassword.setChecked(true);
        }

        builder.setView(v);
        //builder.setCancelable(false);

        builder.setTitle("Connect to NAO");

        builder.setPositiveButton("Connect", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                robotUrl = ipAddress.getText().toString();
                robotSSHUsername = sshUname.getText().toString();
                robotSSHPassword = sshPass.getText().toString();

                if(savePassword.isChecked())
                    preferences.edit().putString("savedPassword", robotSSHPassword).apply();
                else
                {
                    if(preferences.contains("savedPassword"))
                        preferences.edit().remove("savedPassword").apply();
                }

                init();
            }
        });

        builder.show();
    }

    private void init()
    {

        try
        {
            session = new Session();
            session.connect(robotUrl).get();
            tts = new ALTextToSpeech(session);
            posture = new ALRobotPosture(session);
            animatedSpeech = new ALAnimatedSpeech(session);
            audioPlayer = new ALAudioPlayer(session);
            autonomousLife = new ALAutonomousLife(session);
            navigation = new ALMotion(session);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}