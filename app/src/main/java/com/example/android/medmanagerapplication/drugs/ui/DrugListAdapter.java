package com.example.android.medmanagerapplication.drugs.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.medmanagerapplication.R;
import com.example.android.medmanagerapplication.drugs.DrugContract;

public class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.DrugViewHolder> {

    private int mNumberItems;
    private Context mContext;
    private Cursor mCursor;

    public DrugListAdapter(Context context, Cursor  cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @NonNull
    @Override
    public DrugListAdapter.DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // TODO: Clean up
//        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.drug_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        DrugViewHolder viewHolder = new DrugViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DrugListAdapter.DrugViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int idIndex = mCursor.getColumnIndex(DrugContract.DrugEntry._ID);
        int drugNameIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.NAME);
        int intervalIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.INTERVAL);
        int startDateIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.START_DATE);
        int endDateIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.END_DATE);



    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DrugViewHolder extends RecyclerView.ViewHolder {

        TextView drugNameTextView;
        TextView intervalTextView;
        TextView durationTextView;

        public DrugViewHolder(View itemView) {
            super(itemView);
            drugNameTextView = itemView.findViewById(R.id.drugName_textView);
            intervalTextView = itemView.findViewById(R.id.interval_textView);
            durationTextView = itemView.findViewById(R.id.duration_textView);
        }

        void bind( int listIndex) {
            drugNameTextView.setText(String.valueOf(listIndex));
            intervalTextView.setText(String.valueOf(listIndex));
            durationTextView.setText(String.valueOf(listIndex));
        }
    }
}
