import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import repository.ISeaRepository;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class RoundController extends UnicastRemoteObject implements IObserver, Serializable {
    private IService server;
    private String username;
    private Stage stage;
    private Integer gameID;
    @FXML Text ltrTxt;
    @FXML Text pointsTxt;
    @FXML TextField countryField;
    @FXML TextField cityField;
    @FXML TextField seaField;

    public RoundController() throws RemoteException {
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

    @Override
    public void enableStart() throws RemoteException {

    }

    @Override
    public void disableStart() throws RemoteException {

    }

    @Override
    public void newRound(Integer id, String letter, Integer points) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                ltrTxt.setText(letter);
                pointsTxt.setText(points.toString() + " pct");

                cityField.setText("");
                countryField.setText("");
                seaField.setText("");
            }
        });
        this.gameID = id;
    }

    @Override
    public void finalClasament(Map<String, Integer> clasament) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                Stage primaryStage=new Stage();

                FXMLLoader loader=new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/final.fxml"));
                Parent root= null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FinalController finalController = loader.getController();
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Player " + username);
                primaryStage.show();
                finalController.setService(server);
                finalController.setUsername(username);
                finalController.setStage(primaryStage);
                finalController.finalClasament(clasament);
                server.changeClient(username, finalController);
                stage.getScene().getWindow().hide();
            }
        });
    }

    public void sendResponse(ActionEvent ae){
        String country = countryField.getText();
        String city = cityField.getText();
        String sea = seaField.getText();

        server.sendResponse(username, gameID, country, city, sea);
    }
}
