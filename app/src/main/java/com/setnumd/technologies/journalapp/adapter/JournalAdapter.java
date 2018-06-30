package com.setnumd.technologies.journalapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.setnumd.technologies.journalapp.R;
import com.setnumd.technologies.journalapp.contracts.Journal;



import java.util.ArrayList;
import java.util.List;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {
    private List<Journal> journalArrayList;
    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;

    public JournalAdapter(ArrayList<Journal> journalArrayList,ItemClickListener listener) {
        this.journalArrayList = journalArrayList;
       mItemClickListener = listener;
    }

    public void setData(List<Journal> arrayList) {
        journalArrayList = arrayList;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }

    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View journalView = inflater.inflate(R.layout.journal_layout_list, parent, false);

        // Return a new holder instance
        JournalViewHolder viewHolder = new JournalViewHolder(journalView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        Journal journal = journalArrayList.get(position);

        // Set item views based on your views and data model
        holder.title.setText(journal.getTitle());
        holder.content.setText(journal.getContent()+"...........");


    }

    @Override
    public int getItemCount() {
        if(journalArrayList != null){

            return journalArrayList.size();
        }
        return 0;

    }

    public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView content;

        public JournalViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.journalTitle);
            content = itemView.findViewById(R.id.journalcontent);
            itemView.setOnClickListener(this);
        }


        /**
         * When data changes, this method updates the list of taskEntries
         * and notifies the adapter to use the new values on it
         */
        public void setTasks(ArrayList<Journal> taskEntries) {
            journalArrayList = taskEntries;
            notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {
            int elementId = journalArrayList.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}
