package repository;

import domain.City;
import domain.Sea;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CityRepository implements ICityRepository {

    public CityRepository() {
        System.out.println("creating city repo");
    }

    @Override
    public boolean findOne(String name) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                String queryString = "from City as c where c.name = ?";
                List<City> c = session.createQuery(queryString, City.class)
                        .setParameter(0, name)
                        .list();
                tx.commit();
                if (c.size() == 1)
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
