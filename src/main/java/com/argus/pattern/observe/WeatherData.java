package com.argus.pattern.observe;

import java.util.Observable;

/**
 * @author xingding
 * @date 2017/8/6.
 */
public class WeatherData extends Observable { //主题对象
    private float temperature;
    private float presusure;

    public WeatherData() {
    }

    public void measureChanged(){
        setChanged(); //设置changed=true
        notifyObservers();
    }

    public void setMeasure(float temperature, float presusure){
        this.temperature = temperature;
        this.presusure = presusure;
        measureChanged();
    }

    public float getTemperature() {
        return temperature;
    }

    public float getPresusure() {
        return presusure;
    }
}
