package games;

import domain.Round;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import repository.RoundRepository;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/games")
public class RoundController {
    private static final String template="TemplateGame";

    @Autowired
    private RoundRepository roundRepository;

    @RequestMapping(value = "/clasament/{gameID}", method = RequestMethod.GET)
    public Map<String, Integer> getClasament(@PathVariable Integer gameID){
        Map<String, Integer> clasament = new HashMap<>();
        List<String> players = roundRepository.getPlayers(gameID);

        for(String username : players){
            Integer points = 0;
            for (int i = 1; i < 4; i++) {
                Round r = roundRepository.findOne(gameID, username, i);
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

        return result;
    }

    @RequestMapping(value = "/rounds/{gameID}/{playerID}", method = RequestMethod.GET)
    public List<Round> getClasament(@PathVariable Integer gameID, @PathVariable String playerID) {
        List<Round> rounds = new ArrayList<>();
        for (int i = 1; i < 4; i++) {
            Round r = roundRepository.findOne(gameID, playerID, i);
            rounds.add(r);
        }
        return rounds;
    }

}
