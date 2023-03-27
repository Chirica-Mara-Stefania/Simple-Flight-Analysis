package repository;

import domain.Flight;

import org.sqlite.SQLiteDataSource;
import java.sql.*;
import java.util.ArrayList;

public class Repository {
    private static final String JDBC_URL = "jdbc:sqlite:data/test_db.db";

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn == null)
            openConnection();
        return conn;
    }

    public Repository() throws Exception{
        createTable();
    }

    public void createTable() throws Exception {
        openConnection();
        final Statement s = conn.createStatement();
        s.executeUpdate("CREATE TABLE IF NOT EXISTS FlightsInfo(SourceCity VARCHAR(50), Destination VARCHAR(50), Departure FLOAT, Arrival FLOAT, Temperature INT, RainProbability INT, WeatherDescription VARCHAR(50));");
        s.close();
    }

    public boolean add(Flight flight) throws Exception {
        openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO FlightsInfo VALUES (?, ?, ?, ?, ?, ?, ?);");
            statement.setString(1, flight.getSource());
            statement.setString(2, flight.getDestination());
            statement.setFloat(3, flight.getDepartureTime());
            statement.setFloat(4, flight.getArrivalTime());
            statement.setInt(5, flight.getTemperature());
            statement.setInt(6, flight.getRainProbability());
            statement.setString(7, flight.getWeatherDescription());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean update(Flight flight) throws Exception {
        openConnection();
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE FlightsInfo SET RainProbability = ?, WeatherDescription = ? WHERE Departure = ? AND Arrival = ?;");
            statement.setInt(1, flight.getRainProbability());
            statement.setString(2, flight.getWeatherDescription());
            statement.setFloat(3, flight.getDepartureTime());
            statement.setFloat(4, flight.getArrivalTime());
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Flight> getAll() throws Exception {
        openConnection();
        ArrayList<Flight> flights = new ArrayList<>();

        PreparedStatement statement = conn.prepareStatement("SELECT * FROM FlightsInfo");
        ResultSet rs = statement.executeQuery();
        while (rs.next()) {
            Flight d = new Flight(
                    rs.getString("SourceCity"),
                    rs.getString("Destination"),
                    rs.getFloat("Departure"),
                    rs.getFloat("Arrival"),
                    rs.getInt("Temperature"),
                    rs.getInt("RainProbability"),
                    rs.getString("WeatherDescription"));
            flights.add(d);
        }
        rs.close();
        statement.close();
        return flights;
    }

    private static void openConnection()
    {
        try
        {
            SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl(JDBC_URL);
            if (conn == null || conn.isClosed())
                conn = ds.getConnection();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void closeConnection()
    {
        try
        {
            conn.close();
            conn = null;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
