package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import edu.mtu.naoremotecontrol.R;

public class TextActionDialogFragment extends ActionDialogChildFragment
{
    private EditText text;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_fragment_add_action_text, container, false);

        text = (EditText) v.findViewById(R.id.action_dialog_text);

        if(inputData != null)
        {
            text.setText(inputData[0]);
        }

        return v;
    }

    @Override
    public String getData()
    {
        return text.getText().toString();
    }
}
