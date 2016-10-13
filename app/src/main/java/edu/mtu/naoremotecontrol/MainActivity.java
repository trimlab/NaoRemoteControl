package edu.mtu.naoremotecontrol;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity
{
    private RemoteControlPagerAdapter adapter;
    private ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.mainTabLayout);

        if (toolbar != null)
        {
            setSupportActionBar(toolbar);
        }

        viewPager = (ViewPager) findViewById(R.id.mainViewPager);

        adapter = new RemoteControlPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        showConnectionDialog();
    }

    private void showConnectionDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_connection, null);
        builder.setView(v);
        builder.setTitle("Connect to Nao");
        builder.setPositiveButton("CONNECT", null);

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener()
        {
            private EditText ipAddress;
            private View connectionDialogProgressContainer;
            @Override
            public void onShow(DialogInterface dialog)
            {
                ipAddress = (EditText) ((Dialog) dialog).findViewById(R.id.connectionDialogIpAddress);
                connectionDialogProgressContainer = ((Dialog) dialog).findViewById(R.id.connectionDialogProgressContainer);

                Button connect = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                connect.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        ipAddress.setVisibility(View.GONE);
                        connectionDialogProgressContainer.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_remotecontrol, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_save:
                break;

            case R.id.action_save_as:
                break;

            case R.id.action_load:
                break;

            case R.id.action_execute:
                break;

            case R.id.action_joystick:
                break;

            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private class RemoteControlPagerAdapter extends FragmentPagerAdapter
    {
        private RemoteControlPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            Fragment ret = null;

            if(position == 0)
            {
                ret = new RemoteControlFragment();
            }
            else if(position == 1)
            {
                ret = new SystemInfoFragment();
            }

            return ret;
        }

        @Override
        public int getCount()
        {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            CharSequence ret = null;

            if(position == 0)
            {
                ret = "Remote Control";
            }
            else if(position == 1)
            {
                ret = "System Info";
            }

            return ret;
        }
    }
}