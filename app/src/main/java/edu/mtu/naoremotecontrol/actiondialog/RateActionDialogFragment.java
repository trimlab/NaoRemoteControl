package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.mtu.naoremotecontrol.R;

public class RateActionDialogFragment extends Fragment
{
    private SeekBar rateLevel;
    private TextView rateLevelView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_rate, container, false);

        rateLevel = (SeekBar) v.findViewById(R.id.action_dialog_rate_value);
        rateLevelView = (TextView) v.findViewById(R.id.action_dialog_rate_value_display);

        rateLevelView.setText(String.valueOf(rateLevel.getProgress()+50));

        rateLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                rateLevelView.setText(String.valueOf(progress + 50));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });

        return v;
    }
}
