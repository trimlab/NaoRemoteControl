package edu.mtu.naoremotecontrol.actiondialog;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import edu.mtu.naoremotecontrol.R;

public class ActionDialogFragment extends DialogFragment
{
    public interface OnDialogClosedListener
    {
        public void onDialogClosed(String data, int type, int index);
    }

    private ViewPager viewPager;
    private ActionDialogPagerAdapter adapter;
    private OnDialogClosedListener onDialogClosedListener;
    public static final int TYPE_CREATE = 0, TYPE_EDIT = 1;
    private int index;
    private int type;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_action, null);

        index = getArguments().getInt("index");
        type = getArguments().getInt("type");

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.dialogActionTabLayout);
        viewPager = (DialogViewPager) v.findViewById(R.id.dialogActionViewPager);
        viewPager.setOffscreenPageLimit(6);
        adapter = new ActionDialogPagerAdapter(getChildFragmentManager(),
                new Fragment[]{ new TextActionDialogFragment(),
                        new PitchActionDialogFragment(),
                        new VolumeActionDialogFragment(),
                        new RateActionDialogFragment(),
                        new PoseActionDialogFragment(),
                        new GestureActionDialogFragment()});

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        Button positive = (Button) v.findViewById(R.id.dialogActionPositiveButton);
        Button negative = (Button) v.findViewById(R.id.dialogActionNegativeButton);

        positive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ActionDialogChildFragment current = (ActionDialogChildFragment) adapter.getItem(viewPager.getCurrentItem());
                String header = adapter.getPageTitle(viewPager.getCurrentItem()) + ": ";
                onDialogClosedListener.onDialogClosed(header + current.getData(), type, index);
                dismiss();
            }
        });

        negative.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });

        return v;
    }

    public void setOnDialogClosedListener(OnDialogClosedListener listener)
    {
        this.onDialogClosedListener = listener;
    }

    private static class ActionDialogPagerAdapter extends FragmentPagerAdapter
    {
        private final String[] FRAGMENT_TITLES = {"Text","Pitch","Volume","Rate","Pose","Gesture"};
        private Fragment[] fragments;
        private ActionDialogPagerAdapter(FragmentManager fm, Fragment[] fragments)
        {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position)
        {
            return fragments[position];
        }

        @Override
        public int getCount()
        {
            return FRAGMENT_TITLES.length;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return FRAGMENT_TITLES[position];
        }
    }
}
