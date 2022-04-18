

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.text.DecimalFormat;


public class View extends Application {
     Service service = new Service("Poland");


    @Override
    public void start(Stage stage) throws Exception {

        stage.setTitle("Application");
        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: rgb(255,255,255)");
        WikiBrowser wikiBrowser = new WikiBrowser();
        borderPane.setCenter(wikiBrowser);
        VBox left = new VBox();
        left.setSpacing(20);
        VBox right = new VBox();
        right.setSpacing(20);
        HBox top = new HBox();
        right.setSpacing(20);


        //Left box
        Label temperature = new Label("Temperature");
        temperature.setFont(new Font(20));
        temperature.setPadding(new Insets(10,10,10,10));
        Label humidity = new Label("Humidity");
        humidity.setFont(new Font(20));
         humidity.setPadding(new Insets(10,10,10,10));
        Label pressure = new Label("Pressure");
        pressure.setFont(new Font(20));
        pressure.setPadding(new Insets(10,10,10,10));
        Label feelsLike = new Label("Feels Like");
        feelsLike.setFont(new Font(20));
        feelsLike.setPadding(new Insets(10,10,10,10));

        //Right box
        Label plnLabel = new Label("PLN rate:");
        plnLabel.setFont(new Font(20));
        plnLabel.setPadding(new Insets(10,10,10,10));
        Label currencyRateLabel = new Label("Currency rate:");
        currencyRateLabel.setFont(new Font(20));
        currencyRateLabel.setPadding(new Insets(10,10,10,10));


        //Top box
        Label countryLabel = new Label("Country:");
        countryLabel.setFont(new Font(20));
        Label cityLabel = new Label("City:");
        cityLabel.setFont(new Font(20));
        Label currencyLabel = new Label("Currency:");
        currencyLabel.setFont(new Font(20));
        TextField countryField = new TextField("Poland");
        TextField cityField = new TextField("Warsaw");
        TextField currencyField = new TextField("USD");

        Button button = new Button("Submit");
        button.setStyle("-fx-background-color: rgb(26,147,43)");
        button.setFont(new Font(20));


        JSONParser parser = new JSONParser();
        button.setOnAction(a ->{
            service = new Service(countryField.getText());
            wikiBrowser.load(cityField.getText());
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) parser.parse(service.getWeather(cityField.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String temp = (((JSONObject) jsonObject.get("main")).get("temp")).toString();
            int celsiusTemp= (int)(Double.parseDouble(temp)-273.15);
            String hum=  (((JSONObject) jsonObject.get("main")).get("humidity")).toString();
            String pre=  (((JSONObject) jsonObject.get("main")).get("pressure")).toString();
            String feel=  (((JSONObject) jsonObject.get("main")).get("feels_like")).toString();
            int celsiusTempFell= (int)(Double.parseDouble(feel)-273.15);
            temperature.setText("Temperature: "+celsiusTemp + "\u2103");
            humidity.setText("Humidity: "+hum);
            pressure.setText("Pressure: "+pre);
            feelsLike.setText("Feels Like "+celsiusTempFell+ "\u2103");
            DecimalFormat decimalFormat = new DecimalFormat("###.###");
            plnLabel.setText("PLN rate: " + decimalFormat.format(service.getNBPRate()));
            String currency = currencyField.getText();
            currencyRateLabel.setText("Currency rate: " + decimalFormat.format(service.getRateFor(currency)));
        });

        left.getChildren().addAll(temperature, humidity, pressure, feelsLike);
        right.getChildren().addAll(currencyRateLabel, plnLabel);
        top.getChildren().addAll(countryLabel, countryField,cityLabel, cityField, currencyLabel, currencyField, button);

        borderPane.setLeft(left);
        borderPane.setRight(right);
        borderPane.setTop(top);

        Scene scene = new Scene(borderPane,1300,700);
        stage.setScene(scene);
        stage.show();
    }
}
