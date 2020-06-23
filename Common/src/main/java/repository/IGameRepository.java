package repository;

import domain.Game;

public interface IGameRepository {
    Game findOne(Integer gameID);
    void update(Game g);
    void add(Game g);
}
