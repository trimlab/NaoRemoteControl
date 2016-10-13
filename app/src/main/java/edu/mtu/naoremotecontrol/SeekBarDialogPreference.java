package edu.mtu.naoremotecontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

public class SeekBarDialogPreference extends DialogPreference
{
    private SeekBar seekBar;
    public SeekBarDialogPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    public SeekBarDialogPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setDialogLayoutResource(R.layout.preference_dialog_seekbar);
    }

    @Override
    public void onBindDialogView(View view)
    {
        super.onBindDialogView(view);

        seekBar = (SeekBar) view.findViewById(R.id.dialog_seekbar);
    }

    @Override
    public void onDialogClosed(boolean isPositiveResut)
    {
        super.onDialogClosed(isPositiveResut);
    }

    @Override
    public Object onGetDefaultValue(TypedArray a, int index)
    {
        return a.getInteger(index, 100);
    }

}
