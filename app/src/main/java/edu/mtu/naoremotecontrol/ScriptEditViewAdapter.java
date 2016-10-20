package edu.mtu.naoremotecontrol;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class ScriptEditViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<String> script;
    private final int BUTTON = 1, INSERT_BUTTON = 2;
    private View.OnClickListener insertListener;

    public ScriptEditViewAdapter()
    {
        script = new LinkedList<>();
        script.add("");
    }

    public void setInsertListener(View.OnClickListener listener)
    {
        insertListener = listener;
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
            Toast.makeText(v.getContext(), "Button!", Toast.LENGTH_SHORT).show();
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

    public void update(String item, int index)
    {
        script.set(index, item);
        notifyItemChanged(index);
    }

    public List<String> getScript()
    {
        return this.script;
    }
}
