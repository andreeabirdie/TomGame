import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IObserver extends Remote {
    void enableStart() throws RemoteException;
    void disableStart() throws RemoteException;
    void newRound(Integer id, String letter, Integer points) throws RemoteException;
    void finalClasament(Map<String, Integer> clasament) throws RemoteException;
}
