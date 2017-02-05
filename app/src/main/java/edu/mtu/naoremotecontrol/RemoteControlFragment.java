package edu.mtu.naoremotecontrol;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.aldebaran.qi.CallError;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import edu.mtu.naoremotecontrol.actiondialog.ActionDialogFragment;

public class RemoteControlFragment extends Fragment implements RadioGroup.OnCheckedChangeListener, ActionDialogFragment.OnDialogClosedListener
{
    public interface OnSaveListener
    {
        public void onSave(String fileName);
    }

    public interface OnLoadListener
    {
        public void onLoad(String fileName);
    }

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
        scriptEditView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        adapter = new ScriptEditViewAdapter();
        adapter.setInsertListener(insertListener);
        adapter.setRunListener(menuListener);
        //adapter.setMenuListener(menuListener);
        scriptEditView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.ACTION_STATE_SWIPE)
        {
            @Override
            public int getDragDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
            {
                ScriptEditViewAdapter.ButtonViewHolder buttonViewHolder = (ScriptEditViewAdapter.ButtonViewHolder) viewHolder;

                if(buttonViewHolder.isInsertButton())
                    return 0;

                return super.getDragDirs(recyclerView,viewHolder);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target)
            {
                final int from = viewHolder.getAdapterPosition();
                final int to;

                if(((ScriptEditViewAdapter.ButtonViewHolder) viewHolder).isInsertButton())
                    return false;

                if(target.getAdapterPosition() == adapter.getItemCount()-1)
                    to = target.getAdapterPosition()-1;
                else
                    to = target.getAdapterPosition();

                ScriptEditViewAdapter adapter = (ScriptEditViewAdapter) recyclerView.getAdapter();

                Collections.swap(adapter.getScript(), from, to);
                adapter.notifyItemMoved(from, to);

                Log.d("Moved", "Moved");

                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction)
            {

            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(itemTouchCallback);
        helper.attachToRecyclerView(scriptEditView);

        run = (Button) v.findViewById(R.id.runScript);
        pause = (Button) v.findViewById(R.id.pauseScript);
        stop = (Button)v.findViewById(R.id.stopScript);

        run.setOnClickListener(new View.OnClickListener()
        {
            private List<Pair<String, String[]>> naoScript;
            @Override
            public void onClick(View v)
            {
                naoScript = Script.toNaoCommandString(adapter.getScript().subList(0, adapter.getScript().size()-1));

                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        NaoRemoteControlApplication application = (NaoRemoteControlApplication) getActivity().getApplication();
                        for(int i = 0; i < naoScript.size(); i++)
                        {
                            Pair<String, String[]> action = naoScript.get(i);

                            try
                            {
                                application.runCommand(action);
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                            catch (CallError callError)
                            {
                                callError.printStackTrace();
                            }
                        }
                    }
                }).run();
            }
        });

        return v;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        MainActivity activity = (MainActivity) getActivity();

        activity.setOnSaveListener(new OnSaveListener()
        {
            @Override
            public void onSave(String fileName)
            {

                try
                {
                    Script.write(adapter.getScript(), fileName + ".json", getActivity());
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        activity.setOnLoadListener(new OnLoadListener()
        {
            @Override
            public void onLoad(String fileName)
            {

                try
                {
                    List<String> data = Script.read(fileName);
                    adapter.addAll(data);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onCheckedChanged(RadioGroup group, int i)
    {
        NaoRemoteControlApplication application = (NaoRemoteControlApplication)getActivity().getApplication();
        try
        {
            if (group.getChildAt(0).getId() == i)
            {
                application.setAutomaticAnimationEnabled(false);
            }
            else if (group.getChildAt(1).getId() == i)
            {
                application.setAutomaticAnimationEnabled(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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

    private View.OnClickListener runListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            NaoRemoteControlApplication application = (NaoRemoteControlApplication) getActivity().getApplication();
            int index = scriptEditView.getChildAdapterPosition(v);
            String command = adapter.getScript().get(index);

            Pair<String, String[]> commandPair = Script.toNaoCommandString(Collections.singletonList(command)).get(0);

            try
            {
                application.runCommand(commandPair);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (CallError callError)
            {
                callError.printStackTrace();
            }
        }
    };

    private View.OnClickListener menuListener = new View.OnClickListener()
    {
        @Override
        public void onClick(final View buttonView)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(buttonView.getContext());
            builder.setItems(new String[]{"Run", "Edit", "Delete"}, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if(which == 0)
                    {
                        NaoRemoteControlApplication application = (NaoRemoteControlApplication) getActivity().getApplication();
                        int index = scriptEditView.getChildAdapterPosition(buttonView);
                        String command = adapter.getScript().get(index);
                        Pair<String, String[]> commandPair = Script.toNaoCommandString(Collections.singletonList(command)).get(0);

                        try
                        {
                            application.runCommand(commandPair);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                        catch (CallError callError)
                        {
                            callError.printStackTrace();
                        }
                    }
                    else if(which == 1)
                    {
                        ActionDialogFragment edit = new ActionDialogFragment();
                        edit.setOnDialogClosedListener(RemoteControlFragment.this);
                        Bundle args = new Bundle();

                        int index = scriptEditView.getChildAdapterPosition(buttonView);
                        args.putInt("index", index);
                        args.putInt("type", ActionDialogFragment.TYPE_EDIT);
                        args.putString("data", adapter.getScript().get(index));
                        edit.setArguments(args);
                        edit.show(getChildFragmentManager(), "insert_dialog");
                    }
                    else if(which == 2)
                    {
                        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        builder.setTitle("Are you sure you want to delete this?");
                        builder.setPositiveButton("Yes", new android.content.DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialoginterface, int i)
                            {
                                adapter.remove(scriptEditView.getChildAdapterPosition(buttonView));
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                    }
                }
            });

            builder.show();
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