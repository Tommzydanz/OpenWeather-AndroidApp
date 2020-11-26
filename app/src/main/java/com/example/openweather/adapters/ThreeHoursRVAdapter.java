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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ThreeHoursRVAdapter extends RecyclerView.Adapter<ThreeHoursRVAdapter.ViewHolder> {


    public ThreeHoursRVAdapter(List<WeatherList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setList(List<WeatherList> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    List<WeatherList> list;
    Context context;



    @NonNull
    @Override
    public ThreeHoursRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_day_list_item, parent, false);
        return new ViewHolder(view);
    }


    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        return timeFormat.format(dateObject);
    }


    @Override
    public void onBindViewHolder(@NonNull ThreeHoursRVAdapter.ViewHolder holder, int position) {
        WeatherList report = list.get(position);


        Date dateObject = new Date(report.getDt()*1000L);
        String formattedTime24hrs = formatTime(dateObject);
        holder.tempView.setText(String.format(Locale.getDefault(),"%d°", (int) report.getMainList().getTemp()));
        holder.timeView.setText(formattedTime24hrs);

        Glide.with(context)
                .load("https://openweathermap.org/img/wn/" + report.getWeather().get(0).getIcon() + ".png")
                .fitCenter()
                .into(holder.weatherStateImageView);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView tempView;
        private final TextView timeView;
        private final ImageView weatherStateImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tempView = itemView.findViewById(R.id.temp_view_hr);
            timeView = itemView.findViewById(R.id.weather_time_view);
            weatherStateImageView = itemView.findViewById(R.id.weather_image_view);
        }
    }
}
