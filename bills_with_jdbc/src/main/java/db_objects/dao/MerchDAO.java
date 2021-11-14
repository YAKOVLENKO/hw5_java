package db_objects.dao;

import db_objects.entity.Merch;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MerchDAO implements DAO<Merch> {

    private static final String SELECT_SINGLE = "SELECT * FROM merch WHERE id = ";
    private static final String SELECT_ALL = "SELECT * FROM merch";
    private static final String INSERT_SINGLE = "INSERT INTO merch (id, name, code) VALUES (?,?,?)";
    private static final String UPDATE_SINGLE = "UPDATE merch SET name = ?, code = ? WHERE id = ?";
    public static final String DELETE_SINGLE = "DELETE FROM merch WHERE id = ?";

    private final Connection connection;

    public MerchDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @NotNull
    @Override
    public Merch select(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_SINGLE + id)) {
                if (resultSet.next()) {
                    return new Merch(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("code"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Merch record with id " + id + "not found");
    }

    @NotNull
    @Override
    public List<Merch> select() {
        List<Merch> merchList = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL)) {
                while (resultSet.next()) {
                    merchList.add(new Merch(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("code")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return merchList;
    }

    @Override
    public void update(@NotNull Merch entity) {
        try (var statement = connection.prepareStatement(UPDATE_SINGLE)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getCode());
            statement.setInt(3, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while updating merch with id " + entity.getId());  // todo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void insert(@NotNull Merch entity) {
        try (var statement = connection.prepareStatement(INSERT_SINGLE)){
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getCode());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while inserting merch with id " + entity.getId()); // todo
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NotNull Merch entity) {
        try (var statement = connection.prepareStatement(DELETE_SINGLE)) {
            statement.setInt(1, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while deleting merch with id " + entity.getId());
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
