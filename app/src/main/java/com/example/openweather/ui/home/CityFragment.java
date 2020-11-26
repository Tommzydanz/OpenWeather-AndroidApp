package com.example.openweather.ui.home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.Transliterator;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweather.R;
import com.example.openweather.adapters.FiveDayRVAdapter;
import com.example.openweather.adapters.ThreeHoursRVAdapter;
import com.example.openweather.model.City;
import com.example.openweather.model.WeatherList;
import com.example.openweather.model.WeatherModel;
import com.example.openweather.services.CityApiService;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.recyclerview.widget.RecyclerView.*;

public class CityFragment extends Fragment {

    //extra position string
    public static final String EXTRA_POSITION = "extra_position";

    //default positon string
    private static final int DEFAULT_POSITION = -1;

    private CityViewModel cityViewModel;
    private ImageView weatherBackdrop;
    private City city;

    ThreeHoursRVAdapter threeHoursRVAdapter;

    FiveDayRVAdapter fiveDayRVAdapter;

    private WeatherModel weatherModel;

    private TextView cityNameView, dateView, feels_likeView, tempMinMaxView, weatherDescView, windDegView, humidityView, smallFeelsLikeView,

    moreWeatherForcastView;

    private RecyclerView recyclerView1, recyclerView2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        List<WeatherList> weatherList = new ArrayList<>();
        List<WeatherList> weatherLists = new ArrayList<>();
        threeHoursRVAdapter = new ThreeHoursRVAdapter(weatherList, getContext());
        fiveDayRVAdapter = new FiveDayRVAdapter(weatherLists, getContext());
        //TextViews
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
        moreWeatherForcastView = root.findViewById(R.id.weather_address_view);
        moreWeatherForcastView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.openweathermap.org/"));
                if (browserIntent.resolveActivity(requireActivity().getPackageManager()) != null) {
                    startActivity(browserIntent);
                }
            }
        });
        //recyclerview1 {horizontal}
        recyclerView1 = root.findViewById(R.id.recycler_list1);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setHasFixedSize(true);
        recyclerView1.setAdapter(threeHoursRVAdapter);
        //ImageView weatherBackdrop = root.findViewById(R.id.weather_backdrop_view);
        // RecyclerView List1
        recyclerView2 = root.findViewById(R.id.recycler_list2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setAdapter(fiveDayRVAdapter);
        getIntentMethod();
        String cityName;
        Intent intent = requireActivity().getIntent();
        cityName = intent.getStringExtra("city");
        cityNameView.setText(cityName);

        Call<WeatherModel> weatherModelCall = CityApiService.getInstance().getApi().getList(2350592, "acef04100ca9abf95a99dafcaceddd7b", "metric");
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
            public void onFailure(Call<WeatherModel> call, Throwable t) {
                Toast.makeText(getContext(), "Unable to connect to server", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    // populate the weather fragment UI
    @SuppressLint("SetTextI18n")
    public void populateUIText(WeatherModel weatherModel) {
        cityNameView.setText(weatherModel.getCity().getName());
        feels_likeView.setText(weatherModel.getList().get(0).getMainList().getFeels_like() + "\u00B0");
        smallFeelsLikeView.setText(requireContext().getString(R.string.feels_like_detail) + "\n\n" +weatherModel.getList().get(0).getMainList().getFeels_like()+ "\u00B0");
        weatherDescView.setText(weatherModel.getList().get(0).getWeather().get(0).getCloud_desc());
        windDegView.setText(getContext().getString(R.string.levels_detail, weatherModel.getList().get(0).getWind().getWind_deg()));
        humidityView.setText("Humidity" + "\n\n" + weatherModel.getList().get(0).getMainList().getHumidity() + "%");
        //max and min temp
        tempMinMaxView.setText(String.format("%s-%s\u00B0", weatherModel.getList().get(0).getMainList().getTemp_min(), weatherModel.getList().get(0).getMainList().getTemp_max()));
        Date dateObject = new Date(weatherModel.getList().get(0).getDt()*1000L);
        String formattedDate = formatDate(dateObject);
        String formattedTime = formatTime(dateObject);
        dateView.setText(String.format("%s, %s", formattedDate, formattedTime));

    }

    //Intent method that gets the position of the views from the API
    public Intent getIntentMethod() {

        String cityName;
        Intent intent = getActivity().getIntent();
        cityName = intent.getStringExtra("city");

        cityNameView.setText(cityName);
        assert intent != null;
        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // if EXTRA_POSITION not found in intent
            return intent;
        }
        return intent;
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

}