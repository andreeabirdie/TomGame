package repository;

import domain.City;
import domain.Country;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CountryRepository implements ICountryRepository {

    public CountryRepository() {
        System.out.println("Creating country repo");
    }

    @Override
    public boolean findOne(String name) {
        try (Session session = JdbcUtils.getSessionFactory().openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                String queryString = "from Country as c where c.name = ?";
                List<Country> c = session.createQuery(queryString, Country.class)
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
