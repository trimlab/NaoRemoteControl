package edu.mtu.naoremotecontrol.actiondialog;

import android.support.v4.app.Fragment;

/**
 * Created by EricMVasey on 10/15/2016.
 */

public abstract class ActionDialogChildFragment extends Fragment
{
    protected String[] inputData = null;
    public abstract String getData();

    public void setData(String... data)
    {
        inputData = data;
    }
}
