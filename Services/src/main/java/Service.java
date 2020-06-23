import repository.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IService {
    private ICountryRepository countryRepository;
    private ICityRepository cityRepository;
    private ISeaRepository seaRepository;
    private IUserRepository userRepository;
    private IGameRepository gameRepository;
    private IRoundRepository roundRepository;
    private Map<String,IObserver> loggedClients;

    public Service(ICountryRepository countryRepository, ICityRepository cityRepository, ISeaRepository seaRepository, IUserRepository userRepository, IGameRepository gameRepository, IRoundRepository roundRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.seaRepository = seaRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.roundRepository = roundRepository;
        this.loggedClients = new ConcurrentHashMap<>();
    }

    @Override
    public boolean login(String username, String password, IObserver client) throws Exception {
        boolean valid = userRepository.findOne(username, password);
        if(valid) {
            if (loggedClients.get(username) != null) {
                throw new Exception("User is already Logged in!");
            }
            loggedClients.put(username, client);
            System.out.println("Client " + username + " connected");
        }
        return valid;
    }
}
