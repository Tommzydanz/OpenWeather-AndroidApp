package com.example.openweather.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweather.R;
import com.example.openweather.WeatherActivity;
import com.example.openweather.model.City;
import com.example.openweather.model.WeatherModel;
import com.example.openweather.services.CityApiService;
import com.example.openweather.ui.home.CityFragment;

import java.awt.font.NumericShaper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;

public class CityListRVAdapter extends RecyclerView.Adapter<CityListRVAdapter.ViewHolder>{

    private List<City> cityList;
    private List<City> cityListAll;
    private Context context;

    public CityListRVAdapter(List<City> cityList, Context context) {
        this.cityList = cityList;
        this.context = context;
    }
    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
        cityListAll = new ArrayList<>(cityList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CityListRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListRVAdapter.ViewHolder holder, int position) {

        cityList.get(position).getId();
        holder.cityName.setText(cityList.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cityIntent = new Intent(context.getApplicationContext(), WeatherActivity.class);
                cityIntent.putExtra("city", cityList.get(position).getName());

                cityIntent.putExtra(CityFragment.EXTRA_POSITION, position);

                context.startActivity(cityIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView cityName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cityName = itemView.findViewById(R.id.city_name_textView);

        }
    }
    //get filter
    public Filter getFilter(){
        return cityFilter;
    }
    // Filter method used for  searching objects
    private Filter cityFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<City> filteredCityList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredCityList.addAll(cityListAll);
            }else{
                String filterPattern = constraint.toString().toLowerCase();
                String filterPatternUpper = constraint.toString().toUpperCase().trim();
                filterPattern.equalsIgnoreCase(filterPatternUpper);
                    for (City city : cityListAll) {
                        if (city.getName().toLowerCase().contains(filterPattern)) {
                            filteredCityList.add(city);
                        }
                    }

            }
            FilterResults results = new FilterResults();
            results.values = filteredCityList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            cityList.clear();
            cityList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
