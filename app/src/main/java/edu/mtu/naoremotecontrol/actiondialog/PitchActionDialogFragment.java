package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.mtu.naoremotecontrol.R;

public class PitchActionDialogFragment extends ActionDialogChildFragment
{
    private SeekBar pitchLevel;
    private TextView pitchLevelView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_pitch, container, false);

        pitchLevel = (SeekBar) v.findViewById(R.id.action_dialog_pitch_value);
        pitchLevelView = (TextView) v.findViewById(R.id.action_dialog_pitch_value_display);

        pitchLevelView.setText(String.valueOf(pitchLevel.getProgress()+50));

        pitchLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                pitchLevelView.setText(String.valueOf(progress + 50));
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

        if(inputData != null)
        {
            pitchLevel.setProgress(Integer.parseInt(inputData[0]));
            pitchLevelView.setText(inputData[0]);
        }

        return v;
    }

    @Override
    public String getData()
    {
        StringBuilder ret = new StringBuilder();
        ret.append(pitchLevelView.getText());
        return ret.toString();
    }
}
