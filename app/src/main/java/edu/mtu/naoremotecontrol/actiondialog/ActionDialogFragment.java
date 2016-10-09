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

import edu.mtu.naoremotecontrol.R;

/**
 * Created by EricMVasey on 10/8/2016.
 */

public class ActionDialogFragment extends DialogFragment
{
    private ViewPager viewPager;
    private ActionDialogPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.dialog_add_action, container);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.dialogActionTabLayout);
        viewPager = (ViewPager) v.findViewById(R.id.dialogActionViewPager);
        adapter = new ActionDialogPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }

    private class ActionDialogPagerAdapter extends FragmentPagerAdapter
    {
        private final String[] FRAGMENT_TITLES = {"Text","Pitch","Volume","Rate","Pose","Gesture"};
        private ActionDialogPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment ret = null;
            String prefix = "edu.mtu.naoremotecontrol.actiondialog.";
            String suffix = "ActionDialogFragment";
            try
            {
                ret = (Fragment) Class.forName(prefix + FRAGMENT_TITLES[position] + suffix).newInstance();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return ret;
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
