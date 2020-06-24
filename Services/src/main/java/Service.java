import domain.Game;
import domain.Round;
import org.springframework.jmx.export.assembler.AbstractReflectiveMBeanInfoAssembler;
import repository.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.toMap;

public class Service implements IService {
    private ICountryRepository countryRepository;
    private ICityRepository cityRepository;
    private ISeaRepository seaRepository;
    private IUserRepository userRepository;
    private IGameRepository gameRepository;
    private IRoundRepository roundRepository;
    private Map<String,IObserver> loggedClients;
    private List<String> ltrs = new ArrayList<>(Arrays.asList("b", "c", "l", "s"));

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
            if(loggedClients.size() == 3){
                notifyStart();
            }
        }
        return valid;
    }

    @Override
    public void logout(String username, IObserver client) throws Exception {
        IObserver localClient = loggedClients.remove(username);
        if (localClient == null)
            throw new Exception("User "+ username + " is not logged in");
        System.out.println("Client " + username + " disconnected");
        if(loggedClients.size() < 3){
            notifyStart();
        }
    }

    @Override
    public void startGame() throws IOException {
        String fileName = "D:\\MPP\\practice\\TOM\\Services\\src\\main\\resources\\gameID";
        FileInputStream fis=new FileInputStream(fileName);
        Scanner sc=new Scanner(fis);
        Integer id = Integer.parseInt(sc.nextLine()) +1;
        sc.close();
        System.out.println(id);
        FileOutputStream outputStream = new FileOutputStream(fileName);
        byte[] strToBytes = id.toString().getBytes();
        outputStream.write(strToBytes);
        outputStream.close();

        Collections.shuffle(ltrs);
        String letters = ltrs.get(0);
        for (int i=1; i<3; i++) {
            letters += "," + ltrs.get(i);
        }

        Game g = new Game(id,0,0,letters);
        gameRepository.add(g);
        startRound(id);
    }

    @Override
    public void sendResponse(String username, Integer gameID, String country, String city, String sea) {
        Game g = gameRepository.findOne(gameID);
        Round r = roundRepository.findOne(gameID, username, g.getCurrentRound());

        r.setCountry(country);
        r.setCity(city);
        r.setSea(sea);
        roundRepository.update(r);

        g.setSentResponses(g.getSentResponses() + 1);
        gameRepository.update(g);
        if(g.getSentResponses() == 3){
            finishRound(g);
        }

    }

    @Override
    public void changeClient(String username, IObserver client) {
        loggedClients.replace(username, client);
    }

    private void finishRound(Game g) {
        String letter = g.getLetters().split(",")[g.getCurrentRound()-1];
        List<Round> rounds = new ArrayList<>();
        List<String> countries = new ArrayList<>();
        List<String> cities = new ArrayList<>();
        List<String> seas = new ArrayList<>();

        for(String username : loggedClients.keySet()) {
            Round r = roundRepository.findOne(g.getId(), username, g.getCurrentRound());
            rounds.add(r);
            countries.add(r.getCountry());
            cities.add(r.getCity());
            seas.add(r.getSea());
        }

        for (int i = 0; i < 3; i++) {
            if(cities.get(i).startsWith(letter) && cityRepository.findOne(cities.get(i))){
                if(appearences(cities.get(i), cities) == 1)
                    rounds.get(i).setPoints(rounds.get(i).getPoints() + 10);
                else rounds.get(i).setPoints(rounds.get(i).getPoints() + 3);
            }
            if(countries.get(i).startsWith(letter) && countryRepository.findOne(countries.get(i))){
                if(appearences(countries.get(i), countries) == 1)
                    rounds.get(i).setPoints(rounds.get(i).getPoints() + 10);
                else rounds.get(i).setPoints(rounds.get(i).getPoints() + 3);
            }
            if(seas.get(i).startsWith(letter) && seaRepository.findOne(seas.get(i))){
                if(appearences(seas.get(i), seas) == 1)
                    rounds.get(i).setPoints(rounds.get(i).getPoints() + 10);
                else rounds.get(i).setPoints(rounds.get(i).getPoints() + 3);
            }
            roundRepository.update(rounds.get(i));
        }
        if(g.getCurrentRound() != 3)
            startRound(g.getId());
        else finishGame(g.getId());
    }

    private void finishGame(Integer id) {
        Map<String, Integer> clasament = new HashMap<>();
        for(String username : loggedClients.keySet()){
            Integer points = 0;
            for (int i = 1; i < 4; i++) {
                Round r = roundRepository.findOne(id, username, i);
                points += r.getPoints();
            }
            clasament.put(username, points);
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(clasament.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<String, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        showClasament(result);
    }

    private void showClasament(Map<String, Integer> clasament) {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);

        executor.execute(()->{
            try{
                System.out.println("notifying clasament...");
                for(String username : loggedClients.keySet())
                    loggedClients.get(username).finalClasament(clasament);
            }catch (Exception e){
                System.out.println("error notifying player...");
            }
        });

        executor.shutdown();
    }

    private Integer appearences(String element, List<String> list){
        Integer nr=0;
        for(String s: list){
            if(s.equals(element)) nr++;
        }
        return nr;
    }

    private void startRound(Integer id) {
        Game g = gameRepository.findOne(id);
        g.setCurrentRound(g.getCurrentRound() + 1);
        g.setSentResponses(0);
        gameRepository.update(g);

        for(String username : loggedClients.keySet()){
            Round r = new Round(id, g.getCurrentRound(), username, "", "", "", 0);
            roundRepository.add(r);
        }

        sendLetter(g);
    }


    private final int defaultThreadsNo=5;
    private void notifyStart() {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);

        executor.execute(()->{
            try{
                System.out.println("notifying start...");
                for(String username : loggedClients.keySet())
                    if(loggedClients.size() == 3)
                        loggedClients.get(username).enableStart();
                    else if(loggedClients.size() < 3)
                        loggedClients.get(username).disableStart();
            }catch (Exception e){
                System.out.println("error notifying player...");
            }
        });

        executor.shutdown();
    }

    private void sendLetter(Game g) {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);

        executor.execute(()->{
            try{
                String letter = g.getLetters().split(",")[g.getCurrentRound() -1];

                for(String username : loggedClients.keySet()) {
                    Integer points = 0;
                    if (g.getCurrentRound() > 1) {
                        for (int i = 1; i <= g.getCurrentRound(); i++) {
                            Round rnd = roundRepository.findOne(g.getId(), username, i);
                            points += rnd.getPoints();
                        }
                    }
                    System.out.println("sending letter to " + username);
                    loggedClients.get(username).newRound(g.getId(), letter, points);
                }
            }catch (Exception e){
                System.out.println("error notifying player..." + e.getMessage());
            }
        });

        executor.shutdown();
    }
}
