package repository;

import domain.Sea;
import domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class SeaRepository implements ISeaRepository{

    public SeaRepository() {
        System.out.println("Creating sea repo");
    }

    @Override
    public boolean findOne(String name) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                String queryString = "from Sea as s where s.name = ?";
                List<Sea> sea = session.createQuery(queryString, Sea.class)
                        .setParameter(0, name)
                        .list();
                tx.commit();
                if (sea.size() == 1)
                    return true;
                else return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
