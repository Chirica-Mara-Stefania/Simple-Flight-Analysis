package gui;

import domain.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import service.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Controller {
    private final Service service;

    public Controller(Service service) {
        this.service = service;
    }

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TextField textField;

    @FXML
    private Button button;

    @FXML
    private Button refreshButton;

    @FXML
    private ListView<Flight> listView;

    private final ObservableList<Flight> intervals = FXCollections.observableArrayList();

    public void displayFlights() throws Exception {
        listView.getItems().clear();

        intervals.addAll(service.getAll()
                .stream()
                .sorted((o1, o2) -> {
                    return Float.compare(o1.getDepartureTime(), o2.getArrivalTime());
                })
                .collect(Collectors.toCollection(ArrayList::new)));
        listView.setItems(intervals);
    }

    public void listClicked(MouseEvent event) {

        FlowPane updatePanel = new FlowPane(Orientation.VERTICAL, 50, 20);
        Label invisible = new Label("");
        Label probabilityLabel = new Label();
        Label descriptionLabel = new Label();
        TextField probabilityTextField = new TextField();
        TextArea descriptionTextArea = new TextArea();
        Button update = new Button("Update");

        probabilityTextField.setMaxWidth(100);
        descriptionTextArea.setMaxWidth(300);

        String[] interval = listView.getSelectionModel().getSelectedItem().toString().split(" ", 9);
        probabilityTextField.setText(interval[6]);
        descriptionTextArea.setText(interval[8]);

        probabilityLabel.setText("Precipitations probability");
        descriptionLabel.setText("Weather description");

        updatePanel.getChildren().add(invisible);
        updatePanel.getChildren().add(probabilityLabel);
        updatePanel.getChildren().add(probabilityTextField);
        updatePanel.getChildren().add(descriptionLabel);
        updatePanel.getChildren().add(descriptionTextArea);
        updatePanel.getChildren().add(update);

        Scene sc = new Scene(updatePanel, 300, 420);
        Stage s = new Stage();
        s.setTitle("Update");
        s.setScene(sc);
        s.show();

        EventHandler<ActionEvent> click = e -> {
            try {
                String[] interval1 = listView.getSelectionModel().getSelectedItem().toString().split(" ", 9);

                String sourceCity = interval1[0];
                String destination = interval1[2];
                float departure = Float.parseFloat(interval1[4]);
                float arrival = Float.parseFloat(interval1[6]);
                int temperature = Integer.parseInt(interval1[8]);
                int rainProbability = Integer.parseInt(interval1[10]);
                String weatherDescription = interval1[12];

                if (!probabilityTextField.getText().isEmpty())
                    rainProbability = Integer.parseInt(probabilityTextField.getText());

                if (!descriptionTextArea.getText().isEmpty())
                    weatherDescription = descriptionTextArea.getText();

                Flight ti = new Flight(sourceCity, destination, departure, arrival, temperature, rainProbability, weatherDescription);

                service.update(ti);
                displayFlights();
                s.close();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };

        update.setOnAction(click);
    }

    public void selectDescription(ActionEvent action) throws Exception {
        String description = comboBox.getValue();
        listView.getItems().clear();
        intervals.addAll(service.getAll()
                .stream()
                .sorted((o1, o2) -> {
                    if (o1.getDepartureTime() > o2.getArrivalTime())
                        return 1;
                    if (o1.getDepartureTime() == o2.getArrivalTime())
                        return 0;
                    else return -1;
                })
                .filter(obj -> obj.getWeatherDescription().contains(description))
                .collect(Collectors.toCollection(ArrayList::new)));
        listView.setItems(intervals);
    }

    public void calculate(ActionEvent action) throws Exception {
        float hours = 0;
        String description = textField.getText();
        for (var obj : service.getAll()) {
            if (obj.getWeatherDescription().contains(description))
                hours += (obj.getArrivalTime() - obj.getDepartureTime());
        }

        Alert alert;
        if (description.isEmpty())
            alert = new Alert(Alert.AlertType.NONE, "Please input description!", ButtonType.OK);

        else
            alert = new Alert(Alert.AlertType.NONE, "" + description + " - " + hours + " hours", ButtonType.OK);
        alert.show();
    }

    public void refresh(ActionEvent action) throws Exception {
        displayFlights();
    }

    public void initialize() throws Exception {
        displayFlights();

        ObservableList<String> queries = FXCollections.observableArrayList();
        queries.addAll(service.getAll()
                .stream()
                .map(obj -> obj.getWeatherDescription().split(", ")[0])
                .collect(Collectors.toCollection(ArrayList::new)));

        comboBox.setItems(queries);
    }
}
