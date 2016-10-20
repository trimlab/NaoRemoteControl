package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import edu.mtu.naoremotecontrol.R;

public class PoseActionDialogFragment extends ActionDialogChildFragment
{
    private Spinner pose;
    private SeekBar poseSpeed;
    private TextView poseSpeedView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_pose, container, false);

        pose = (Spinner) v.findViewById(R.id.action_dialog_pose);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                new String[]{"Stand", "Sit"});

        pose.setAdapter(adapter);

        poseSpeed = (SeekBar) v.findViewById(R.id.action_dialog_posespeed_value);
        poseSpeedView = (TextView) v.findViewById(R.id.action_dialog_posespeed_value_display);

        poseSpeedView.setText(String.valueOf(poseSpeed.getProgress()+50));

        poseSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                poseSpeedView.setText(String.valueOf(progress+50));
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
        ret.append((String) pose.getSelectedItem());
        ret.append("/");
        ret.append(poseSpeedView.getText());

        return ret.toString();
    }
}
