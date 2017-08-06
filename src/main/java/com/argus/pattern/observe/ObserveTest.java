package com.argus.pattern.observe;

/**
 * @author xingding
 * @date 2017/8/6.
 */
public class ObserveTest {

    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        CurrentConditionDisplay currentConditionDisplay = new CurrentConditionDisplay(weatherData);
        //other Display classes
        weatherData.setMeasure(80,60);
        weatherData.setMeasure(70,100);//模拟改变
        weatherData.setMeasure(30,20);//模拟改变
    }
}
