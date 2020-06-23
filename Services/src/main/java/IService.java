public interface IService {
    boolean login(String username, String password, IObserver client) throws Exception;

}
