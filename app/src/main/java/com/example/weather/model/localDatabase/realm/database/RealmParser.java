package com.example.weather.model.localDatabase.realm.database;

import com.example.weather.model.localDatabase.realm.models.Clouds;
import com.example.weather.model.localDatabase.realm.models.Coord;
import com.example.weather.model.localDatabase.realm.models.Main;
import com.example.weather.model.localDatabase.realm.models.Sys;
import com.example.weather.model.localDatabase.realm.models.Weather;
import com.example.weather.model.localDatabase.realm.models.WeatherModel;
import com.example.weather.model.localDatabase.realm.models.Wind;

import java.util.List;

import io.realm.RealmList;

public class RealmParser {

    /**
     * Convert Java models to realm models
     * @param body
     * @return
     */
    public static WeatherModel getParsedModel(com.example.weather.model.network.networkModels.WeatherModel body) {

        WeatherModel weatherModel = getWeatherModel(body);
        return weatherModel;

    }

    private static WeatherModel getWeatherModel(com.example.weather.model.network.networkModels.WeatherModel body) {

        WeatherModel weatherModel = new WeatherModel();

        weatherModel.setLastUpdated(System.currentTimeMillis());

        weatherModel.setPrimaryID(1);

        if (body.getBase() != null) {
            weatherModel.setBase(body.getBase());
        }

        weatherModel.setCod(body.getCod());

        weatherModel.setName(body.getName());

        weatherModel.setId(body.getId());

        weatherModel.setTimezone(body.getTimezone());

        weatherModel.setDt(body.getDt());

        if (body.getCoord() != null) {
            weatherModel.setCoord(getParsedCoord(body.getCoord()));
        }

        if (body.getWeather() != null) {
            weatherModel.setWeather(getParsedWeather(body.getWeather()));
        }

        if (body.getMain() != null) {
            weatherModel.setMain(getParsedMain(body.getMain()));
        }

        if (body.getWind() != null) {
            weatherModel.setWind(getParsedWind(body.getWind()));
        }

        if (body.getClouds() != null) {
            weatherModel.setClouds(getParedClouds(body.getClouds()));
        }

        if (body.getSys() != null) {
            weatherModel.setSys(getParedSys(body.getSys()));
        }

        return weatherModel;
    }

    private static Sys getParedSys(com.example.weather.model.network.networkModels.Sys data) {
        Sys sys = new Sys();

        sys.setPrimaryID(1);

        if (data.getType() != null) {
            sys.setType(data.getType());
        }

        if (data.getId() != null) {
            sys.setId(data.getId());
        }

        if (data.getMessage() != null) {
            sys.setMessage(data.getMessage());
        }

        if (data.getCountry() != null) {
            sys.setCountry(data.getCountry());
        }

        if (data.getSunrise() != null) {
            sys.setSunrise(data.getSunrise());
        }

        if (data.getSunset() != null) {
            sys.setSunset(data.getSunset());
        }


        return sys;
    }

    private static Clouds getParedClouds(com.example.weather.model.network.networkModels.Clouds data) {
        Clouds clouds = new Clouds();

        clouds.setPrimaryID(1);

        if (data.getAll() != null) {
            clouds.setAll(data.getAll());
        }

        return clouds;
    }

    private static Wind getParsedWind(com.example.weather.model.network.networkModels.Wind data) {
        Wind wind = new Wind();

        wind.setPrimaryID(1);

        if (data.getDeg() != null) {
            wind.setDeg(data.getDeg());
        }

        if (data.getSpeed() != null) {
            wind.setSpeed(data.getSpeed());
        }

        return wind;
    }

    private static Main getParsedMain(com.example.weather.model.network.networkModels.Main data) {
        Main main = new Main();

        main.setPrimaryID(1);

        if (data.getTemp() != null) {
            main.setTemp(data.getTemp());
        }

        if (data.getFeelsLike() != null) {
            main.setFeelsLike(data.getFeelsLike());
        }

        if (data.getTempMin() != null) {
            main.setTempMin(data.getTempMin());
        }

        if (data.getTempMax() != null) {
            main.setTempMax(data.getTempMax());
        }

        if (data.getPressure() != null) {
            main.setPressure(data.getPressure());
        }

        if (data.getHumidity() != null) {
            main.setHumidity(data.getHumidity());
        }

        return main;
    }

    private static RealmList<Weather> getParsedWeather(List<com.example.weather.model.network.networkModels.Weather> weatherList) {
        RealmList<Weather> weatherRealmList = new RealmList<>();
        for (int loop = 0; loop < weatherList.size(); loop++) {
            if (weatherList.get(loop) != null) {
                Weather weather = new Weather();

                weather.setPrimaryID(loop);
                if (weatherList.get(loop).getDescription() != null) {
                    weather.setDescription(weatherList.get(loop).getDescription());
                }

                if (weatherList.get(loop).getIcon() != null) {
                    weather.setIcon(weatherList.get(loop).getIcon());
                }

                if (weatherList.get(loop).getId() != null) {
                    weather.setId(weatherList.get(loop).getId());
                }

                if (weatherList.get(loop).getMain() != null) {
                    weather.setMain(weatherList.get(loop).getMain());
                }
                weatherRealmList.add(weather);

            }
        }
        return weatherRealmList;
    }

    private static Coord getParsedCoord(com.example.weather.model.network.networkModels.Coord data) {
        Coord coord = new Coord();

        coord.setPrimaryID(1);

        if (data.getLat() != null) {
            coord.setLat(data.getLat());
        }

        if (data.getLon() != null) {
            coord.setLon(data.getLon());
        }

        return coord;
    }
}
