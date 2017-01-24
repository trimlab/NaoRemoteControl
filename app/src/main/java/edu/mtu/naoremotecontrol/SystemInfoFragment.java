package edu.mtu.naoremotecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SystemInfoFragment extends Fragment
{
    private TextView battery_level;
    private TextView cpu_temperature;
    private Timer infoUpdateTimer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View v = inflater.inflate(R.layout.fragment_systeminfo, container, false);

        cpu_temperature = (TextView) v.findViewById(R.id.cpu_temperature);
        battery_level = (TextView) v.findViewById(R.id.battery_level);

        return v;
    }

    public void onResume()
    {
        super.onResume();
        infoUpdateTimer = new Timer();
        infoUpdateTimer.schedule(new TimerTask()
        {
            private NaoRemoteControlApplication application;

            public void run()
            {
                application = (NaoRemoteControlApplication) getActivity().getApplication();
                getActivity().runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            cpu_temperature.setText((new StringBuilder()).append("CPU Temperature: ")
                                    .append(application.getCPUTemperature())
                                    .append(" ")
                                    .append('\u00B0')
                                    .append("C").toString());
                            battery_level.setText((new StringBuilder()).
                                    append("Battery Level: ").
                                    append(application.getBatteryLevel()).append("%").toString());
                        }
                        catch (Exception exception)
                        {
                            exception.printStackTrace();
                        }
                    }
                });
            }
        }, 250);
    }
}
