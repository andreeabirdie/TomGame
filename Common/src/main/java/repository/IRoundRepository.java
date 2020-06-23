package repository;

import domain.Round;

public interface IRoundRepository {
    Round findOne(Integer gameID, String player, Integer round);
    void update(Round r);
    void add(Round r);
}
