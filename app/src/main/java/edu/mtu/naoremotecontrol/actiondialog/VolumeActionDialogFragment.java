package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.mtu.naoremotecontrol.R;

public class VolumeActionDialogFragment extends ActionDialogChildFragment
{
    private SeekBar volumeLevel;
    private TextView volumeLevelView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_volume, container, false);

        volumeLevel = (SeekBar) v.findViewById(R.id.action_dialog_volume_value);
        volumeLevelView = (TextView) v.findViewById(R.id.action_dialog_volume_value_display);

        volumeLevelView.setText(String.valueOf(volumeLevel.getProgress()));

        volumeLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                volumeLevelView.setText(String.valueOf(progress));
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

    @Override
    public String getData()
    {
        StringBuilder ret = new StringBuilder();
        ret.append("Volume: ");
        ret.append(volumeLevelView.getText());
        return ret.toString();
    }
}
