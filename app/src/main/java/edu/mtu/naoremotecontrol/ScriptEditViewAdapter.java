package edu.mtu.naoremotecontrol;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by EricMVasey on 10/6/2016.
 */

public class ScriptEditViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private List<String> script;
    private final int BUTTON = 1, TEXT = 2, INVISIBLE = 3;

    public ScriptEditViewAdapter()
    {
        script = new LinkedList<>();
        script.add("");
    }

    private class EditTextHolder extends RecyclerView.ViewHolder
    {
        public EditText view;
        private int index;

        public EditTextHolder(EditText itemView)
        {
            super(itemView);

            view = itemView;
            view.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after)
                {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count)
                {

                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    script.set(index, s.toString());
                }
            });
        }

        public void setIndex(int index)
        {
            this.index = index;
        }
    }

    private class ButtonViewHolder extends RecyclerView.ViewHolder implements Button.OnClickListener
    {
        public Button view;
        private String text;
        private int index;

        public ButtonViewHolder(Button itemView)
        {
            super(itemView);

            view = itemView;
        }

        public void setIndex(int index)
        {
            this.index = index;
        }

        @Override
        public void onClick(View v)
        {
            //TODO open dialog
            Toast.makeText(v.getContext(), "Button!", Toast.LENGTH_SHORT).show();
        }
    }

    public static class InvisibleInsertViewHolder extends RecyclerView.ViewHolder
    {
        public View v;
        public InvisibleInsertViewHolder(View itemView)
        {
            super(itemView);
            v = itemView;
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        if(script.get(position).startsWith("BUTTON:"))
        {
            return BUTTON;
        }

        if(position == script.size()-1)
            return INVISIBLE;

        return TEXT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        switch(viewType)
        {
            case BUTTON:
                Button b = new Button(parent.getContext());
                b.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new ButtonViewHolder(b);

            case TEXT:
                EditText t = new EditText(parent.getContext());
                t.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new EditTextHolder(t);

            case INVISIBLE:
                LinearLayout v = new LinearLayout(parent.getContext());
                v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                return new InvisibleInsertViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(holder instanceof EditTextHolder)
        {
            EditTextHolder editTextHolder = (EditTextHolder) holder;
            editTextHolder.setIndex(position);
            editTextHolder.view.setText(script.get(position));
        }
        else if(holder instanceof ButtonViewHolder)
        {
            ButtonViewHolder buttonViewHolder = (ButtonViewHolder) holder;
            buttonViewHolder.setIndex(position);
            buttonViewHolder.view.setText(script.get(position));
        }
        else if(holder instanceof InvisibleInsertViewHolder)
        {

        }
    }

    @Override
    public int getItemCount()
    {
        return script.size();
    }

    public void add(String item)
    {
        script.add(script.size()-2, item);
        notifyDataSetChanged();
    }

    public void addAll(List<String> items)
    {
        script.addAll(script.size()-2, items);
    }
}
