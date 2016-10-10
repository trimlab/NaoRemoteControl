package edu.mtu.naoremotecontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import edu.mtu.naoremotecontrol.actiondialog.ActionDialogFragment;

public class RemoteControlFragment extends Fragment implements RadioGroup.OnCheckedChangeListener
{
    private RecyclerView scriptEditView;
    private ScriptEditViewAdapter adapter;

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
        adapter.setInsertListener(insertListener);
        scriptEditView.setAdapter(adapter);

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
            dialog.show(getChildFragmentManager(), "insert_dialog");
        }
    };
}
