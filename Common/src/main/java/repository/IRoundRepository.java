package repository;

import domain.Round;

import java.util.List;

public interface IRoundRepository {
    Round findOne(Integer gameID, String player, Integer round);
    void update(Round r);
    void add(Round r);
    List<String> getPlayers(Integer gameID);
}
