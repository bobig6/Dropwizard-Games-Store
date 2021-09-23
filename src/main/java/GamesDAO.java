
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.type.descriptor.java.DataHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GamesDAO extends AbstractDAO<Games> {

    private static final String SELECT_ALL_QUERY = "SELECT * FROM GAMES";
    private static final String INSERT_QUERY = "insert into games(gameName, gameGenre) values(?, ?);";
    private static final String SELECT_BY_ID_QUERY = "SELECT * FROM GAMES WHERE gameID = ?";
    private static final String UPDATE_BY_ID_QUERY = "UPDATE Games SET gameName=?, gameGenre=? WHERE gameId=?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM Games WHERE gameId=?";




    DBManager dbManager;

    public GamesDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
        dbManager = new DBManager();
    }


    public List<Games> findAll() {
//        return list(namedTypedQuery("Games.findAll"));
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_QUERY)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Games> gamesList = new ArrayList<>();
                while (resultSet.next()){
                    gamesList.add(convertRSToObject(resultSet));
                }
                connection.close();
                return gamesList;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * Looks for employees whose first or last name contains the passed
     * parameter as a substring.
     *
     * @param name query parameter
     * @return list of employees whose first or last name contains the passed
     * parameter as a substring.
     */
    public List<Games> findByName(String name) {
        StringBuilder builder = new StringBuilder("%");
        builder.append(name).append("%");
        return list(
                namedTypedQuery("Games.findByName")
                        .setParameter("name", builder.toString())
        );
    }


    public List<Games> findById(Long id) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_QUERY)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                List<Games> gamesList = new ArrayList<>();
                while (resultSet.next()){
                    gamesList.add(convertRSToObject(resultSet));
                }
                connection.close();
                return gamesList;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public void addGame(Games games) {
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_QUERY)) {
            statement.setString(1, games.getGameName());
            statement.setString(2, games.getGameGenre());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void updateGame(Games games){
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID_QUERY)) {
            statement.setString(1, games.getGameName());
            statement.setString(2, games.getGameGenre());
            statement.setLong(3, games.getGameId());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void deleteGame(long id){
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_QUERY)){
             statement.setLong(1, id);
             statement.executeUpdate();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Games convertRSToObject(ResultSet resultSet) throws SQLException {
        Games games = new Games();

        games.setGameId(resultSet.getLong("gameId"));
        games.setGameName(resultSet.getString("gameName"));
        games.setGameGenre(resultSet.getString("gameGenre"));
        return games;
    }
}

