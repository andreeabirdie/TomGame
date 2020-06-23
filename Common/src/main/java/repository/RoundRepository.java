package repository;

import domain.Game;
import domain.Round;
import domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class RoundRepository implements IRoundRepository {

    public RoundRepository() {
        System.out.println("Creating round repository");
    }

    @Override
    public Round findOne(Integer gameID, String player, Integer round) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                String queryString = "from Round as r where r.gameID = ? and r.player = ? and r.round = ?";
                List<Round> r = session.createQuery(queryString, Round.class)
                        .setParameter(0, gameID)
                        .setParameter(1, player)
                        .setParameter(2, round)
                        .list();
                tx.commit();
                if (r.size() == 1)
                    return r.get(0);
                else return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(Round r) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                Round round = (Round) session.load(Round.class, new Round(r.getGameID(), r.getPlayer(), r.getRound()));
                round.setPoints(r.getPoints());
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Round r) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.save(r);
                tx.commit();
            } catch (RuntimeException ex) {
                if (tx != null)
                    tx.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
