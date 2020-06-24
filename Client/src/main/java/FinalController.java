import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import static java.lang.System.exit;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class FinalController extends UnicastRemoteObject implements IObserver, Serializable {
    private IService server;
    private String username;
    private Stage stage;
    @FXML
    Text firstPlace;
    @FXML
    Text secondPlace;
    @FXML
    Text thirdPlace;

    public FinalController() throws RemoteException {
    }


    public void setService(IService server) {
        this.server = server;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {

    }

    @FXML
    public void logout() throws Exception {
        try {
            server.logout(username, this);
            exit(0);
        } catch (Exception e) {
            alert(e.getMessage());
        }
    }


    public void alert(String err) {
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error message!");
        message.setContentText(err);
        message.showAndWait();
    }

    @Override
    public void enableStart() {

    }

    @Override
    public void disableStart() throws RemoteException {

    }

    @Override
    public void newRound(Integer id, String letter, Integer points) {

    }

    @Override
    public void finalClasament(Map<String, Integer> clasament) {
        clasament = clasament.entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                        HashMap::new));
        Map<String, Integer> finalClasament = clasament;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String first = "1. ";
                String second = "2. ";
                String third = "3. ";
                int i = 1;
                for (String username : finalClasament.keySet()) {
                    if (i == 1) {
                        first += username + ": " + finalClasament.get(username) + " pct";
                    }
                    if (i == 2) {
                        second += username + ": " + finalClasament.get(username) + " pct";
                    }
                    if (i == 3) {
                        third += username + ": " + finalClasament.get(username) + " pct";
                    }
                    i++;
                }
                firstPlace.setText(first);
                secondPlace.setText(second);
                thirdPlace.setText(third);
            }

        });
    }
}