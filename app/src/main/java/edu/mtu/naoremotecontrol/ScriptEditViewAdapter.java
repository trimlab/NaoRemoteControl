package edu.mtu.naoremotecontrol;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScriptEditViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<String> script;
    private final int BUTTON = 1, INSERT_BUTTON = 2;
    private View.OnClickListener insertListener;
    private View.OnClickListener runListener;
    private View.OnLongClickListener menuListener;

    public ScriptEditViewAdapter()
    {
        script = new ArrayList<>();
        script.add("");
    }

    public void setInsertListener(View.OnClickListener listener)
    {
        insertListener = listener;
    }
    public void setRunListener(View.OnClickListener listener)
    {
        runListener = listener;
    }
    public void setMenuListener(View.OnLongClickListener listener)
    {
        menuListener = listener;
    }

    private class ButtonViewHolder extends RecyclerView.ViewHolder implements Button.OnClickListener
    {
        public Button view;
        private String text;
        private int index;
        private boolean isInsert;

        public ButtonViewHolder(Button itemView, boolean isInsert)
        {
            super(itemView);

            view = itemView;
            this.isInsert = isInsert;
        }

        public void setIndex(int index)
        {
            this.index = index;
        }

        public boolean isInsertButton()
        {
            return isInsert;
        }

        @Override
        public void onClick(View v)
        {

        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(position == script.size()-1)
            return INSERT_BUTTON;

        return BUTTON;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case BUTTON:
                Button b = new Button(parent.getContext());
                b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                if(runListener != null)
                    b.setOnClickListener(runListener);

                if(menuListener != null)
                    b.setOnLongClickListener(menuListener);

                return new ButtonViewHolder(b, false);

            case INSERT_BUTTON:
                Button insert = new Button(parent.getContext());
                insert.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                insert.setText("+");

                if(insertListener != null)
                    insert.setOnClickListener(insertListener);

                return new ButtonViewHolder(insert, true);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder instanceof ButtonViewHolder)
        {
            ButtonViewHolder buttonViewHolder = (ButtonViewHolder) holder;
            buttonViewHolder.setIndex(position);

            if(!buttonViewHolder.isInsertButton())
            {
                buttonViewHolder.view.setText(script.get(position));
            }
        }
    }

    @Override
    public int getItemCount()
    {
        return script.size();
    }

    public void add(String item)
    {
        script.add(script.size()-1, item);
        notifyItemInserted(script.size()-1);
    }

    public void addAll(List<String> items)
    {
        script.clear();
        script.addAll(items);
        script.add("");
        notifyDataSetChanged();
        Log.d("Loaded", Arrays.toString(script.toArray()));
    }

    public void update(String item, int index)
    {
        script.set(index, item);
        notifyItemChanged(index);
        Log.d("Updated", Arrays.toString(script.toArray()));
    }

    public void remove(int i)
    {
        script.remove(i);
        notifyItemRemoved(i);
    }

    public void clear()
    {
        for(int i = 0; i < script.size()-1; i++)
        {
            script.remove(i);
        }
    }

    public List<String> getScript()
    {
        return this.script;
    }
}
