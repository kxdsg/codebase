package com.argus.pattern.observe;

import java.util.Observable;
import java.util.Observer;

/**
 * @author xingding
 * @date 2017/8/6.
 */
public class CurrentConditionDisplay implements Observer {

    private float temperature;
    private float pressure;

    public CurrentConditionDisplay(Observable observable) {
        observable.addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof WeatherData){
            WeatherData weatherData = (WeatherData)o;
            this.temperature = weatherData.getTemperature();
            this.pressure = weatherData.getPresusure();
            display();
        }
    }

    public void display(){
        System.out.println("Current conditions, temperature: " + this.temperature + " pressure: " + this.pressure);
    }
}
