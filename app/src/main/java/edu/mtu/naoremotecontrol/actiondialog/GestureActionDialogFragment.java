package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import edu.mtu.naoremotecontrol.R;

public class GestureActionDialogFragment extends ActionDialogChildFragment
{
    private Spinner gestureSpinner;
    private SeekBar gestureSpeedLevel;
    private TextView gestureSpeedLevelView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_gesture, container, false);

        gestureSpinner = (Spinner) v.findViewById(R.id.action_dialog_gesture);
        gestureSpeedLevel = (SeekBar) v.findViewById(R.id.action_dialog_gesturespeed_value);
        gestureSpeedLevelView = (TextView) v.findViewById(R.id.action_dialog_gesturespeed_value_display);

        gestureSpeedLevelView.setText(String.valueOf(gestureSpeedLevel.getProgress()+50));

        gestureSpeedLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                gestureSpeedLevelView.setText(String.valueOf(progress+50));
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
        ret.append("Gesture/");
        ret.append(gestureSpeedLevelView.getText());
        ret.append(": ");
        ret.append((String) gestureSpinner.getSelectedItem());

        return ret.toString();
    }
}
