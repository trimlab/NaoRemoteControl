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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity
{

    private RemoteControlPagerAdapter adapter;
    private ViewPager viewPager;
    private RemoteControlFragment.OnSaveListener saveListener;
    private RemoteControlFragment.OnLoadListener loadListener;

    public void setOnSaveListener(RemoteControlFragment.OnSaveListener listener)
    {
        this.saveListener = listener;
    }

    public void setOnLoadListener(RemoteControlFragment.OnLoadListener listener)
    {
        this.loadListener = listener;
    }

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v;
        switch (item.getItemId())
        {
            case R.id.action_save:
                builder.setTitle("Save");
                v = getLayoutInflater().inflate(R.layout.dialog_save, null);
                final EditText fileNameField = (EditText) v.findViewById(R.id.dialog_save_filename);
                builder.setView(v);
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        saveListener.onSave(fileNameField.getText().toString());
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                builder.show();
                break;

            case R.id.action_load:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, 450);
                break;

            case R.id.action_execute:
                builder.setTitle("Execute");
                v = getLayoutInflater().inflate(R.layout.dialog_execute, null);
                builder.setView(v);
                builder.setPositiveButton("Run", null);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener()
                {
                    private Spinner programList;
                    private View container;
                    @Override
                    public void onShow(DialogInterface dialog)
                    {
                        programList = (Spinner) ((Dialog) dialog).findViewById(R.id.dialog_program_spinner);
                        container = ((Dialog) dialog).findViewById(R.id.executeDialogProgressContainer);

                        ArrayAdapter<String> programs = new ArrayAdapter<String>(MainActivity.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                new String[]{"Tai-Chi", "Custom1"});

                        programList.setAdapter(programs);

                        Button run = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);

                        run.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                v.setEnabled(false);
                                programList.setVisibility(View.GONE);
                                container.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

                dialog.show();
                break;

            case R.id.action_joystick:
                Intent joystick = new Intent(MainActivity.this, JoystickActivity.class);
                startActivity(joystick);
                break;

            case R.id.action_settings:
                Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settings);
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