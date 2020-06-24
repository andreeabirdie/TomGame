import java.io.IOException;

public interface IService {
    boolean login(String username, String password, IObserver client) throws Exception;
    void logout(String username, IObserver client) throws Exception;
    void startGame() throws IOException;
    void sendResponse(String username, Integer gameID, String country, String city, String sea);
    void changeClient(String username,IObserver client);
}
