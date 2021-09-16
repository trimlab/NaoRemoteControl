package edu.mtu.naoremotecontrol.actiondialog;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by EricMVasey on 10/11/2016.
 */

public class DialogViewPager extends ViewPager
{
    public DialogViewPager(Context context)
    {
        super(context);
    }

    public DialogViewPager(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
