package mx.app.fashionme.pojo;

import java.util.ArrayList;

public class WeatherResp {
    private ArrayList<Weather> weather;
    private Main main;
    private String name;
    private Sys sys;

    public WeatherResp() {
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WeatherResp{" +
                "weather=" + weather +
                ", main=" + main +
                ", name=" + name +
                '}';
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }
}
