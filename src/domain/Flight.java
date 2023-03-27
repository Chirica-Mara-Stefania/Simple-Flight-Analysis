package domain;

public class Flight {
    private String source;
    private String destination;
    private float departureTime;
    private float arrivalTime;
    private int temperature;
    private int rainProbability;
    private String weatherDescription;

    public Flight(String source, String destination, float departureTime, float arrivalTime, int temperature, int rainProbability, String weatherDescription) {
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.temperature = temperature;
        this.rainProbability = rainProbability;
        this.weatherDescription = weatherDescription;
    }


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public float getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(float departureTime) {
        this.departureTime = departureTime;
    }

    public float getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(float arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getRainProbability() {
        return rainProbability;
    }

    public void setRainProbability(int rainProbability) {
        this.rainProbability = rainProbability;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    @Override
    public String toString() {
        return source +
                "|" + destination +
                "|" + departureTime +
                "|" + arrivalTime +
                "|" + temperature +
                "|" + rainProbability +
                "|" + weatherDescription;
    }
}
