package edu.mtu.naoremotecontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.aldebaran.qi.CallError;
import com.erz.joysticklibrary.JoyStick;

/**
 * Created by EricMVasey on 10/15/2016.
 */

public class JoystickActivity extends AppCompatActivity implements JoyStick.JoyStickListener
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joystick);

        JoyStick joyStick = (JoyStick) findViewById(R.id.movementJoystick);
        joyStick.setType(JoyStick.TYPE_4_AXIS);
    }

    @Override
    public void onMove(JoyStick joyStick, double angle, double power, int direction)
    {
        NaoRemoteControlApplication application = (NaoRemoteControlApplication) getApplication();
        try
        {
            switch (direction)
            {
                case JoyStick.DIRECTION_UP:
                    application.move(NaoRemoteControlApplication.WalkDirection.FORWARD);
                    break;

                case JoyStick.DIRECTION_LEFT:
                    application.move(NaoRemoteControlApplication.WalkDirection.LEFT);
                    break;

                case JoyStick.DIRECTION_RIGHT:
                    application.move(NaoRemoteControlApplication.WalkDirection.RIGHT);
                    break;

                case JoyStick.DIRECTION_DOWN:
                    application.move(NaoRemoteControlApplication.WalkDirection.BACKWARD);
                    break;

                case JoyStick.DIRECTION_UP_RIGHT:
                    break;

                case JoyStick.DIRECTION_RIGHT_DOWN:
                    break;

                case JoyStick.DIRECTION_DOWN_LEFT:
                    break;

                case JoyStick.DIRECTION_LEFT_UP:
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onTap()
    {
        try
        {
            ((NaoRemoteControlApplication) getApplication()).say("Manual Control Enabled");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (CallError callError)
        {
            callError.printStackTrace();
        }
    }

    @Override
    public void onDoubleTap()
    {

    }
}
