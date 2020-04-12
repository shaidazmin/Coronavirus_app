package net.onlinebuzz.coronavirus.Adapter;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import net.onlinebuzz.coronavirus.R;
import net.onlinebuzz.coronavirus.model.DataModel;

import java.util.ArrayList;
import java.util.List;

public class CoronaAdapter extends RecyclerView.Adapter<CoronaAdapter.ViewHolder> implements Filterable {

   LayoutInflater inflater;
   List<DataModel> dataModelList;
   List<DataModel> dataModelFull;

   public CoronaAdapter (Context context, List<DataModel> dataModelList){
       this.inflater = LayoutInflater.from(context);
       this.dataModelList = dataModelList;
       dataModelFull = new ArrayList<>(dataModelList);

   }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view = inflater.inflate(R.layout.country_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


       holder.country.setText(dataModelList.get(position).getCountry());
       holder.totalTests.setText(dataModelList.get(position).getTotalTestes());
       holder.totalCases.setText(dataModelList.get(position).getTotalCases());
       holder.totalDeath.setText(dataModelList.get(position).getTotalDeaths());
       holder.totalRecovered.setText(dataModelList.get(position).getTotalRecovered());
       holder.activeCases.setText(dataModelList.get(position).getActiveCases());
       holder.newCases.setText(dataModelList.get(position).getNewCases());
       holder.seriousCases.setText(dataModelList.get(position).getCriticalCases());
       holder.newDeath.setText(dataModelList.get(position).getNewDeaths());


    }

    @Override
    public int getItemCount() {
        return dataModelList.size();
    }

    // >>>>>>>>


    @Override
    public Filter getFilter() {
        return resultFilter;
    }

    private Filter resultFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DataModel> dataModelList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                dataModelList.addAll(dataModelFull);
            }else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(DataModel dataModel : dataModelFull){
                    if(dataModel.getCountry().toLowerCase().contains(filterPattern)){
                        dataModelList.add(dataModel);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = dataModelList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            dataModelList.clear();
            dataModelList.addAll((List) results.values);
            notifyDataSetChanged();

        }
    };
   //>>>>>>>>>>>>>>>>>>>

    public class ViewHolder extends RecyclerView.ViewHolder{

        MaterialTextView country;
        MaterialTextView totalTests;
        MaterialTextView totalCases;
        MaterialTextView totalDeath;
        MaterialTextView totalRecovered;
        MaterialTextView activeCases;
        MaterialTextView newCases;
        MaterialTextView seriousCases;
        MaterialTextView newDeath;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            country = itemView.findViewById(R.id.country_id);
            totalTests = itemView.findViewById(R.id.total_test_id);
            totalCases = itemView.findViewById(R.id.total_cases_id);
            totalDeath = itemView.findViewById(R.id.total_death_id);
            totalRecovered = itemView.findViewById(R.id.total_recovered_id);
            activeCases = itemView.findViewById(R.id.active_case_id);
            newCases = itemView.findViewById(R.id.new_case_id);
            seriousCases = itemView.findViewById(R.id.serious_case_id);
            newDeath = itemView.findViewById(R.id.new_death_id);


        }

    }
}
