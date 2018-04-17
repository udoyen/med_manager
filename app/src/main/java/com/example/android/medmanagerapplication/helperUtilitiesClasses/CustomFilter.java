package com.example.android.medmanagerapplication.helperUtilitiesClasses;

import android.widget.Filter;

import com.example.android.medmanagerapplication.drugs.ui.DrugListAdapter;

import java.util.ArrayList;

class CustomFilter extends Filter {

    private DrugListAdapter adapter;
    private ArrayList<String> filterList;

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint = constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<String> filteredPlayers = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }

            results.count = filteredPlayers.size();
            results.values = filteredPlayers;
        }else
        {
            results.count = filterList.size();
            results.values = filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {



        adapter.notifyDataSetChanged();

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
