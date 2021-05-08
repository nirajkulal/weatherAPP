package com.example.weather.model.localDatabase.realm.database;

import com.example.weather.model.localDatabase.data.ViewData;
import com.example.weather.model.localDatabase.realm.models.WeatherModel;
import com.example.weather.utils.Helper;

public class ViewDataParser {

    /**
     * Convert from realm to Java models
     * @param weatherModel
     * @return
     */
    public static ViewData getParsedData(WeatherModel weatherModel) {
        ViewData viewData = new ViewData();
        if (weatherModel != null) {
            viewData.setLocation(weatherModel.getName());
            viewData.setStatus(weatherModel.getWeather().get(0).getDescription());
            viewData.setCurrentTemp(Helper.getCelsius(weatherModel.getMain().getTemp()));
            viewData.setFeelsLike("Feels Like " + Helper.getCelsius(weatherModel.getMain().getFeelsLike()));
            int min = Helper.getCelsiusInt(weatherModel.getMain().getTempMin());
            int max = Helper.getCelsiusInt(weatherModel.getMain().getTempMax());
            viewData.setMinMaxTemp(max + "/" + min);
            viewData.setLastUpdated("Last Update " + Helper.epochToDate(weatherModel.getLastUpdated()));
            viewData.setIcon(Helper.getIcon(weatherModel.getWeather().get(0).getIcon()));
            viewData.setSunRise("Sun Rise " + Helper.epochToDate2((weatherModel.getSys().getSunrise())));
            viewData.setSunSet("Sun Set  " + Helper.epochToDate2((weatherModel.getSys().getSunset())));
            weatherModel.getRealm().close();
        }
        return viewData;

    }
}
