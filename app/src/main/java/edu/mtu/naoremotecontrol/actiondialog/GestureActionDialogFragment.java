package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.mtu.naoremotecontrol.R;

/**
 * Created by EricMVasey on 10/8/2016.
 */

public class GestureActionDialogFragment extends Fragment
{
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_gesture, container);
        return v;
    }
}
