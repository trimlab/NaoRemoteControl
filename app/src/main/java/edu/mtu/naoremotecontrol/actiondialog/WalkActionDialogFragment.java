package edu.mtu.naoremotecontrol.actiondialog;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.mtu.naoremotecontrol.R;

public class WalkActionDialogFragment extends ActionDialogChildFragment implements View.OnClickListener
{
    private int lastPressed;
    private Button[] directions = new Button[4];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_walk, null);

        directions[0] = (Button) v.findViewById(R.id.forwardArrow);
        directions[1] = (Button) v.findViewById(R.id.leftArrow);
        directions[2] = (Button) v.findViewById(R.id.rightArrow);
        directions[3] = (Button) v.findViewById(R.id.backArrow);

        for(Button b: directions)
            b.setOnClickListener(this);

        return v;
    }

    @Override
    public String getData()
    {
        StringBuilder ret = new StringBuilder();

        switch (lastPressed)
        {
            case R.id.forwardArrow:
                ret.append("Forward");
                break;

            case R.id.leftArrow:
                ret.append("Left");
                break;

            case R.id.rightArrow:
                ret.append("Right");
                break;

            case R.id.backArrow:
                ret.append("Backward");
        }

        return ret.toString();
    }

    @Override
    public void onClick(View v)
    {
        lastPressed = v.getId();
        v.getBackground().setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);

        for(Button b: directions)
        {
            if(b.getId() != lastPressed)
                b.getBackground().clearColorFilter();
        }
    }
}
