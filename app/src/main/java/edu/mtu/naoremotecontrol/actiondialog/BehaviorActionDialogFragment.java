package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import edu.mtu.naoremotecontrol.NaoRemoteControlApplication;
import edu.mtu.naoremotecontrol.R;

/**
 * Created by EricMVasey on 3/30/2017.
 */

public class BehaviorActionDialogFragment extends ActionDialogChildFragment
{
    private Spinner behaviorSpinner;
    private ArrayAdapter<String> behaviorSpinnerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_behavior, container, false);

        behaviorSpinner = (Spinner) v.findViewById(R.id.action_dialog_behavior);

        try
        {
            behaviorSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                    ((NaoRemoteControlApplication) getActivity().getApplication()).getBehaviors());

            behaviorSpinner.setAdapter(behaviorSpinnerAdapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(inputData != null)
        {
            behaviorSpinner.setSelection(behaviorSpinnerAdapter.getPosition(inputData[0]));
        }

        return v;
    }

    @Override
    public String getData()
    {
        StringBuilder ret = new StringBuilder();
        ret.append( (String) behaviorSpinner.getSelectedItem());
        return ret.toString();
    }
}
