package edu.mtu.naoremotecontrol;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by EricMVasey on 10/6/2016.
 */

public class RemoteControlFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, View.OnTouchListener
{
    private RecyclerView scriptEditView;
    private GestureDetector gestureDetector;
    private ScriptEditViewAdapter adapter;
    private PopupMenu menu;
    private boolean isMenuVisible = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View v = inflater.inflate(R.layout.fragment_remotecontrol, container, false);

        RadioGroup manualAutomaticAnimation = (RadioGroup) v.findViewById(R.id.manualAutomaticAnimation);
        manualAutomaticAnimation.setOnCheckedChangeListener(this);
        manualAutomaticAnimation.check(manualAutomaticAnimation.getChildAt(0).getId());

        scriptEditView = (RecyclerView) v.findViewById(R.id.scriptEditView);
        scriptEditView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapter = new ScriptEditViewAdapter();
        scriptEditView.setAdapter(adapter);

        scriptEditView.setOnTouchListener(this);
        gestureDetector = new GestureDetector(getActivity(), new RecyclerViewGestureDetector());

        return v;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if (v.getId() == R.id.scriptEditView)
        {
            return gestureDetector.onTouchEvent(event);
        }

        return false;
    }

    private class RecyclerViewGestureDetector extends GestureDetector.SimpleOnGestureListener
    {
        public boolean onSingleTapUp(MotionEvent e)
        {
            return false;
        }

        public void onLongPress(MotionEvent e)
        {
            Log.d("LongPress","Detected");
            menu = new PopupMenu(getActivity(), scriptEditView.getChildAt(scriptEditView.getChildCount() - 1), Gravity.CENTER);
            menu.inflate(R.menu.menu_scriptpopup);

            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
            {
                @Override
                public boolean onMenuItemClick(MenuItem item)
                {
                    switch (item.getItemId())
                    {
                        case R.id.scripttext:
                            adapter.add("");
                            break;

                        case R.id.scriptbutton:
                            break;
                    }
                    return false;
                }
            });
            menu.show();

        }

        public boolean onDoubleTap(MotionEvent e)
        {
            return false;
        }

        public boolean onDoubleTapEvent(MotionEvent e)
        {
            return false;
        }

        public boolean onSingleTapConfirmed(MotionEvent e)
        {
            return false;
        }

        public void onShowPress(MotionEvent e)
        {

        }

        public boolean onDown(MotionEvent e)
        {
            return true;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, final float distanceX, float distanceY)
        {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
