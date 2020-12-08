package com.example.openweather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openweather.R;
import com.example.openweather.model.WeatherList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FiveDayRVAdapter extends RecyclerView.Adapter<FiveDayRVAdapter.ViewHolder> {

    public void setList(List<WeatherList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private List<WeatherList> list;
    private final Context context;


    public FiveDayRVAdapter(List<WeatherList> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public FiveDayRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.five_day_list_item, parent, false);
        return  new ViewHolder(view);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, \ndd/LL", Locale.US);
        return dateFormat.format(dateObject);
    }

    @Override
    public void onBindViewHolder(@NonNull FiveDayRVAdapter.ViewHolder holder, int position) {
        WeatherList report = list.get(position);

        Date dateObject = new Date(report.getDt()*1000L);
        String formattedDate = formatDate(dateObject);
        holder.daysView.setText(formattedDate);
        holder.tempMinMaxView.setText(String.format("%s-%s", (int) report.getMainList().getTemp_min(),(int) report.getMainList().getTemp_max() + "\u00B0"));
        Glide.with(context)
                .load("https://openweathermap.org/img/w/" + report.getWeather().get(0).getIcon() + ".png")
                .fitCenter()
                .into(holder.weatherStateImageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tempMinMaxView, daysView;
        private final ImageView weatherStateImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tempMinMaxView = itemView.findViewById(R.id.temp_minMaxView);
            daysView = itemView.findViewById(R.id.days_view);
            weatherStateImageView = itemView.findViewById(R.id.weather_image_view2);
        }
    }
}
