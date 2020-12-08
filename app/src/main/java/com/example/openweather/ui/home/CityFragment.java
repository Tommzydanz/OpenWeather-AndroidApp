package com.example.openweather.ui.home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.openweather.R;
import com.example.openweather.adapters.FiveDayRVAdapter;
import com.example.openweather.adapters.ThreeHoursRVAdapter;
import com.example.openweather.model.WeatherList;
import com.example.openweather.model.WeatherModel;
import com.example.openweather.services.CityApiService;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityFragment extends Fragment {



    //extra position string
    public static final String EXTRA_POSITION = "extra_position";

    //default position string
    private static final int DEFAULT_POSITION = -1;

    private GifImageView weatherBackdrop;

    ThreeHoursRVAdapter threeHoursRVAdapter;

    FiveDayRVAdapter fiveDayRVAdapter;

    private WeatherModel weatherModel;
    private TextView cityNameView;
    private TextView dateView;
    private TextView feels_likeView;
    private TextView tempMinMaxView;
    private TextView weatherDescView;
    private TextView windDegView;
    private TextView humidityView;
    private TextView smallFeelsLikeView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        List<WeatherList> weatherList = new ArrayList<>();
        List<WeatherList> weatherLists = new ArrayList<>();
        threeHoursRVAdapter = new ThreeHoursRVAdapter(weatherList, getContext());
        fiveDayRVAdapter = new FiveDayRVAdapter(weatherLists, getContext());
        //feels_like
        feels_likeView = root.findViewById(R.id.big_feels_like_tempView);
        //weather Description
        weatherDescView = root.findViewById(R.id.weather_desc_view);
        // min and max temp
        tempMinMaxView = root.findViewById(R.id.min_max_tempView);
        //date
        dateView = root.findViewById(R.id.main_date_view);
        //wind degree which shows direction
        windDegView = root.findViewById(R.id.windDeg_view);
        // humidity view
        humidityView = root.findViewById(R.id.humidity_view);
        // feels like with the icon
        smallFeelsLikeView = root.findViewById(R.id.feels_like_tempView);
        cityNameView = root.findViewById(R.id.city_name_textView);
        //open weather map web address
        TextView moreWeatherForcastView = root.findViewById(R.id.weather_address_view);
        moreWeatherForcastView.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.openweathermap.org/"));
            if (browserIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                startActivity(browserIntent);
            }
        });


        weatherBackdrop = root.findViewById(R.id.weather_backdrop_view);
        //recyclerview1 {horizontal}
        RecyclerView recyclerView1 = root.findViewById(R.id.recycler_list1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setAdapter(threeHoursRVAdapter);
        //ImageView weatherBackdrop = root.findViewById(R.id.weather_backdrop_view);
        // RecyclerView List1
        RecyclerView recyclerView2 = root.findViewById(R.id.recycler_list2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(fiveDayRVAdapter);


        /* Intent method and getIntent */
        getIntentMethod();
        String cityName;
        Intent intent = requireActivity().getIntent();
        cityName = intent.getStringExtra("city");
        cityNameView.setText(cityName);
        int cityId;
        cityId = intent.getIntExtra("id", 2350592);


        Call<WeatherModel> weatherModelCall = CityApiService.getInstance().getApi().getList(cityId, "acef04100ca9abf95a99dafcaceddd7b", "metric");
        weatherModelCall.enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(@NotNull Call<WeatherModel> call, @NotNull Response<WeatherModel> response) {
                weatherModel = response.body();
                assert weatherModel != null;
                populateUIText(weatherModel);
                threeHoursRVAdapter.setList(weatherModel.getList());
                fiveDayRVAdapter.setList(weatherModel.getList());
            }

            @Override
            public void onFailure(@NotNull Call<WeatherModel> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    // populate the weather fragment UI
    @SuppressLint("SetTextI18n")
    public void populateUIText(WeatherModel weatherModel) {

        cityNameView.setText(weatherModel.getCity().getName());
        feels_likeView.setText((int) weatherModel.getList().get(0).getMainList().getFeels_like() + "\u00B0");
        smallFeelsLikeView.setText("feels like" + "\n\n" + (int) weatherModel.getList().get(0).getMainList().getFeels_like()+ "\u00B0");
        getBackdrop();
        weatherDescView.setText(weatherModel.getList().get(0).getWeather().get(0).getCloud_desc());
        windDegView.setText(requireContext().getString(R.string.levels_detail, weatherModel.getList().get(0).getWind().getWind_deg()));
        humidityView.setText("Humidity" + "\n\n" + weatherModel.getList().get(0).getMainList().getHumidity() + "%");
        //max and min temp
        tempMinMaxView.setText(String.format("%s-%s\u00B0", (int) weatherModel.getList().get(0).getMainList().getTemp_min(), (int) weatherModel.getList().get(0).getMainList().getTemp_max()));
        Date dateObject = new Date(weatherModel.getList().get(0).getDt()*1000L);
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);
        dateView.setText(String.format("%s, %s", formattedDate, formattedTime));

    }

    //Intent method that gets the position of the views from the API
    public void getIntentMethod() {
        Intent intent = requireActivity().getIntent();
        assert intent != null;
        intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLLL dd, yyyy", Locale.US);
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
        return timeFormat.format(dateObject);
    }



    public void getBackdrop(){
        final String CLEAR_SKY = "clear sky";
        final String FEW_CLOUDS = "few clouds";
        final String SCATTERED_CLOUDS = "scattered clouds";
        final String BROKEN_CLOUDS = "broken clouds";
        final String OVERCAST_CLOUDS = "overcast clouds";
        final String LIGHT_RAIN = "light rain";
        final String RAIN = "rain";
        final String THUNDERSTORM = "thunderstorm";
        final String SNOW = "snow";
        final String MIST = "mist";

        switch (weatherModel.getList().get(0).getWeather().get(0).getCloud_desc()) {
            case CLEAR_SKY:
                Glide.with(requireContext())
                        .load(R.raw.clear_sky_1).into(weatherBackdrop);
                break;
            case FEW_CLOUDS:
                Glide.with(requireContext())
                        .load(R.raw.few_clouds).centerCrop().into(weatherBackdrop);
                break;
            case SCATTERED_CLOUDS:
                Glide.with(requireContext())
                        .load(R.raw.scattered_cloud_1).centerCrop().into(weatherBackdrop);
                break;
            case BROKEN_CLOUDS:
                Glide.with(requireContext())
                        .load(R.raw.broken_cloud_1).centerCrop().into(weatherBackdrop);
                break;
            case OVERCAST_CLOUDS:
                Glide.with(requireContext())
                     .load(R.raw.overcast_cloud_1).centerCrop().into(weatherBackdrop);
            case LIGHT_RAIN:
                Glide.with(requireContext())
                        .load(R.raw.shower_rain_1).centerCrop().into(weatherBackdrop);
                break;
            case RAIN:
                Glide.with(requireContext())
                        .load(R.raw.rain_2).centerCrop().into(weatherBackdrop);
                break;
            case THUNDERSTORM:
                Glide.with(requireContext())
                        .load(R.raw.thunderstorm_1).centerCrop().into(weatherBackdrop);
                break;
            case SNOW:
                Glide.with(requireContext())
                        .load(R.raw.snow).centerCrop().into(weatherBackdrop);
                break;
            case MIST:
                Glide.with(requireContext()).load(R.raw.mist).into(weatherBackdrop);
                break;
        }
    }
}