package edu.mtu.naoremotecontrol.actiondialog;

import edu.mtu.naoremotecontrol.NaoRemoteControlApplication;

/**
 * Created by EricMVasey on 1/24/2017.
 */

public class WalkActionDialogFragment extends ActionDialogChildFragment
{
    private NaoRemoteControlApplication.WalkDirection direction;
    private int numSteps;

    @Override
    public String getData()
    {
        StringBuilder ret = new StringBuilder();

        for(int i = 0; i < numSteps; i++)
        {

        }

        return null;
    }
}
