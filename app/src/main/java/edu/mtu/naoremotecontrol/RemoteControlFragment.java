package edu.mtu.naoremotecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import java.util.Arrays;

import edu.mtu.naoremotecontrol.actiondialog.ActionDialogFragment;

public class RemoteControlFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ActionDialogFragment.OnDialogClosedListener
{
    private RecyclerView scriptEditView;
    private ScriptEditViewAdapter adapter;
    private Button run, pause, stop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
    {
        View v = inflater.inflate(R.layout.fragment_remotecontrol, container, false);

        RadioGroup manualAutomaticAnimation = (RadioGroup) v.findViewById(R.id.manualAutomaticAnimation);
        manualAutomaticAnimation.setOnCheckedChangeListener(this);
        manualAutomaticAnimation.check(manualAutomaticAnimation.getChildAt(0).getId());

        scriptEditView = (RecyclerView) v.findViewById(R.id.scriptEditView);
        scriptEditView.setLayoutManager(new GridLayoutManager(container.getContext(), 2));

        adapter = new ScriptEditViewAdapter();
        adapter.setInsertListener(insertListener);
        scriptEditView.setAdapter(adapter);

        run = (Button) v.findViewById(R.id.runScript);
        pause = (Button) v.findViewById(R.id.pauseScript);
        stop = (Button)v.findViewById(R.id.stopScript);

        run.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("Script", Arrays.toString(adapter.getScript().toArray()));
            }
        });

        return v;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {

    }

    private View.OnClickListener insertListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            ActionDialogFragment dialog = new ActionDialogFragment();
            dialog.setOnDialogClosedListener(RemoteControlFragment.this);
            Bundle args = new Bundle();

            args.putInt("index", adapter.getItemCount()-1);
            args.putInt("type", ActionDialogFragment.TYPE_CREATE);

            dialog.setArguments(args);
            dialog.show(getChildFragmentManager(), "insert_dialog");
        }
    };

    @Override
    public void onDialogClosed(String data, int type, int index)
    {
        if(type == ActionDialogFragment.TYPE_CREATE)
        {
            adapter.add(data);
        }
        else if(type == ActionDialogFragment.TYPE_EDIT)
        {
            adapter.update(data, index);
        }
    }
}