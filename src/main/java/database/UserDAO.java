package database;

import io.dropwizard.hibernate.AbstractDAO;
import model.Games;
import model.User;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDAO{

    private static final String SELECT_ALL_QUERY = "SELECT * FROM USERS";
    private static final String INSERT_QUERY = "insert into users(id, username, password_hash) values(?, ?, ?);";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String SELECT_BY_NAME_QUERY = "SELECT * FROM users WHERE username = ?";
    private static final String UPDATE_BY_ID_QUERY = "UPDATE Users SET username=?, password_hash=? WHERE id=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM users WHERE id=?";

    DBManager dbManager;

    public UserDAO() {
        this.dbManager = new DBManager();
    }

    public List<User> findAll() {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> userList = new ArrayList<>();
                while (resultSet.next()){
                    userList.add(convertRSToObject(resultSet));
                }
                connection.close();
                return userList;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<User> findByUsername(String username) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME_QUERY)) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> userList = new ArrayList<>();
                while (resultSet.next()){
                    userList.add(convertRSToObject(resultSet));
                }
                connection.close();
                return userList;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<User> findById(String id) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
            statement.setString(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<User> userList = new ArrayList<>();
                while (resultSet.next()){
                    userList.add(convertRSToObject(resultSet));
                }
                connection.close();
                return userList;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void addUser(User user) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, user.getId().toString());
            statement.setString(2, user.getName());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void updateUser(User user){
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID_QUERY)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getId().toString());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteUser(String id){
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_QUERY)){
            statement.setString(1, id);
            statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean checkUserCredentials(User user){
        return !findByUsername(user.getName()).isEmpty();
    }

    public User convertRSToObject(ResultSet resultSet) throws SQLException {
        User user = new User();

        user.setId(UUID.fromString(resultSet.getString("id")));
        user.setName(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password_hash"));
        return user;
    }

}
