package repository;

public interface IUserRepository {
    boolean findOne(String username, String password);
}
