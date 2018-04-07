package com.example.android.medmanagerapplication.drugs.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.medmanagerapplication.R;
import com.example.android.medmanagerapplication.drugs.DrugContract;

public class DrugListAdapter extends RecyclerView.Adapter<DrugListAdapter.DrugViewHolder> {

    private static final String TAG = AddDrugActivity.class.getSimpleName();

    //declare interface
    private OnItemClicked onClick;

    //make interface like this
    public interface OnItemClicked {
        void onItemClick(int position);
    }


    private int mNumberItems;
    private Context mContext;
    private Cursor mCursor;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public DrugListAdapter(Context context, Cursor  cursor) {
        this.mContext = context;
        this.mCursor = cursor;

    }

    @NonNull
    @Override
    public DrugViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("Adapter", "Drug Adapter onCreateViewHolder called");
        // TODO: Clean up
        int layoutIdForListItem = R.layout.drug_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        DrugViewHolder viewHolder = new DrugViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final DrugListAdapter.DrugViewHolder holder, int position) {
        Log.v("Adapter", "onBindViewHolder called");
        mCursor.moveToPosition(position);
        int idIndex = mCursor.getColumnIndex(DrugContract.DrugEntry._ID);
        int drugNameIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.NAME);
        int intervalIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.INTERVAL);
        int startDateIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.START_DATE);
        int endDateIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.END_DATE);
        int durationIndex = mCursor.getColumnIndex(DrugContract.DrugEntry.DURATION);

        long drugId = mCursor.getLong(idIndex);
        String drugName = mCursor.getString(drugNameIndex);
        String startDate = mCursor.getString(startDateIndex);
        String endDate = mCursor.getString(endDateIndex);
        long interval = mCursor.getLong(intervalIndex);
        long duration = mCursor.getLong(durationIndex);


        holder.endDateTextView.setText(endDate);
        holder.startDateTextView.setText(startDate);
        holder.durationTextView.setText(String.valueOf(duration));
        holder.drugNameTextView.setText(drugName);
        holder.intervalTextView.setText(String.valueOf(interval));
        holder.drugNameTextView.setTag(drugId);

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }


    public void swapCursor(Cursor newCursor) {
        Log.v("Adapter", "swapCursor called");
        mCursor = newCursor;
        this.notifyDataSetChanged();
        // TODO: Tidy

    }

    /**
     * Returns the number of items in the cursor
     *
     * @return Number of items in the cursor, or 0 if null
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) return 0;
        return mCursor.getCount();
    }

    public class DrugViewHolder extends RecyclerView.ViewHolder {


        TextView drugNameTextView;
        TextView intervalTextView;
        TextView durationTextView;
        TextView startDateTextView;
        TextView endDateTextView;
        ConstraintLayout constraintLayout;

        public DrugViewHolder(View itemView) {
            super(itemView);
            Log.v("Adapter", "DrugViewHolder called");
            drugNameTextView = itemView.findViewById(R.id.drugName_textView);
            intervalTextView = itemView.findViewById(R.id.interval_textView);
            durationTextView = itemView.findViewById(R.id.duration_textView);
            startDateTextView = itemView.findViewById(R.id.starDate_textView);
            endDateTextView = itemView.findViewById(R.id.endDate_textView);
            constraintLayout = itemView.findViewById(R.id.contrain_box);

        }

    }

    public void setOnClick(OnItemClicked onClick)
    {
        Log.v(TAG, "setOnClick called from DrugListAdapter");
        this.onClick = onClick;
    }
}
