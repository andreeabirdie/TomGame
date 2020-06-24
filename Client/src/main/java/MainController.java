import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

import static java.lang.System.exit;

public class MainController extends UnicastRemoteObject implements IObserver, Serializable {
    private IService server;
    private String username;
    private Stage stage;
    @FXML Button startBtn;


    public MainController() throws RemoteException {
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
    public void initialize(){

    }

    @FXML
    public void logout() throws Exception {
        try{
            server.logout(username, this);
            exit(0);
        }
        catch (Exception e){
            alert(e.getMessage());
        }
    }

    @FXML
    public void startGame() throws IOException {
        server.startGame();
    }


    public void alert(String err){
        Alert message = new Alert(Alert.AlertType.ERROR);
        message.setTitle("Error message!");
        message.setContentText(err);
        message.showAndWait();
    }

    @Override
    public void enableStart() {
        startBtn.setDisable(false);
    }

    @Override
    public void disableStart() throws RemoteException {
        startBtn.setDisable(true);
    }

    @Override
    public void newRound(Integer id, String letter, Integer points) {
        Platform.runLater(new Runnable() {
            @Override public void run() {
                Stage primaryStage=new Stage();

                FXMLLoader loader=new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/round.fxml"));
                Parent root= null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RoundController roundController = loader.getController();
                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle("Player " + username);
                primaryStage.show();
                roundController.setService(server);
                roundController.setUsername(username);
                roundController.setStage(primaryStage);
                roundController.newRound(id, letter, points);
                server.changeClient(username, roundController);
                stage.getScene().getWindow().hide();
            }
        });

    }

    @Override
    public void finalClasament(Map<String, Integer> clasament) throws RemoteException {

    }
}
