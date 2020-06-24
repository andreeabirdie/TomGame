package repository;

import domain.Round;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
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
                System.out.println(round);
                round.setPoints(r.getPoints());
                round.setCountry(r.getCountry());
                round.setCity(r.getCity());
                round.setSea(r.getSea());
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

    @Override
    public List<String> getPlayers(Integer gameID) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                String queryString = "from Round as r where r.gameID = ? and r.round = 1";
                List<Round> rounds = session.createQuery(queryString, Round.class)
                        .setParameter(0, gameID)
                        .list();
                tx.commit();
                List<String> players = rounds.stream().map(r->r.getPlayer()).collect(Collectors.toList());
                return players;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
