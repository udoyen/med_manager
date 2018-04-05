package com.example.android.medmanagerapplication.drugs.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.medmanagerapplication.R;

public class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.DrugViewHolder> {

    private int mNumberItems;

    public DrugListAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
    }

    @NonNull
    @Override
    public DrugListAdapter.DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.drug_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        DrugViewHolder viewHolder = new DrugViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DrugListAdapter.DrugViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DrugViewHolder extends RecyclerView.ViewHolder {

        public DrugViewHolder(View itemView) {
            super(itemView);
        }
    }
}
