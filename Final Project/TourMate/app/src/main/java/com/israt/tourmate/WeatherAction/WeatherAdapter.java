package com.israt.tourmate.WeatherAction;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.israt.tourmate.R;
import com.israt.tourmate.WeatherAction.Weather.WeatherResult;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewGroup>{

    Context context;
    WeatherResult weatherResult;
    private ProgressDialog loadinbar;
//    private List<WeatherResult> weatherResult;

    public WeatherAdapter() {
    }


//    public WeitherAdapter(List<WeatherResult> weatherResult) {
//        this.weatherResult = weatherResult;
//    }

    public WeatherAdapter(WeatherResult weatherResult) {
        //this.context = context;
        this.weatherResult = weatherResult;
    }

    @NonNull
    @Override
    public ViewGroup onCreateViewHolder(@NonNull android.view.ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.weather_items, viewGroup, false);

        return new ViewGroup(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // loadinbar = new ProgressDialog(context);



        Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/")
                .append(weatherResult.getList().get(i).getWeather().get(0).getIcon())
                .append(".png").toString()).into(viewGroup.weatherIcon);

        SimpleDateFormat dateandTimeSDF = new SimpleDateFormat("dd MMMM yyyy");

        Date date = new Date();
        date.setDate((weatherResult.getList().get(i).getDt()));
        viewGroup.weatherDate.setText("Date : " + weatherResult.getList().get(i).getDt_txt());



        viewGroup.weatherDescription.setText("Status : " + weatherResult.getList().get(i).getWeather().get(0).getDescription());
        viewGroup.weatherTemp.setText(("Temp : " + weatherResult.getList().get(i).getMain().getTemp() + " °C"));
        viewGroup.weatherWind.setText("Wind : " + weatherResult.getList().get(i).getWind().getSpeed() + " km/h");
        // loadinbar.dismiss();
        //viewGroup.weatherLoactionTv.setText(""+weatherResult.getCity().getCountry());
        //viewGroup.weitherHumidity.setText(("Humidity :"+weatherResult.getList().get(i).getMain().getHumidity()+" %"));
    }

    @Override
    public int getItemCount() {
//        return weathers.size();
        return weatherResult.getList().size();
    }

    public class ViewGroup extends RecyclerView.ViewHolder {
        private ImageView weatherIcon;
        private TextView weatherDate, weatherTemp, weatherWind, weatherHumidity, weatherDescription, weatherLoactionTv;

        public ViewGroup(@NonNull View itemView) {
            super(itemView);

            weatherIcon = itemView.findViewById(R.id.weatherItemIvId);
            weatherDate = itemView.findViewById(R.id.dateWeitherItemTvId);
            weatherTemp = itemView.findViewById(R.id.tempWeitherItemTvId);
            weatherWind = itemView.findViewById(R.id.windWeitherItemTvId);
            weatherDescription = itemView.findViewById(R.id.weitherDiscriptionTvId);
            //weatherLoactionTv = itemView.findViewById(R.id.cityStatusCurrentTvId);
            //weitherHumidity = itemView.findViewById(R.id.humidityWeitherItemTvId);


        }
    }

}
