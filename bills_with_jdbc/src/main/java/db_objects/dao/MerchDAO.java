package db_objects.dao;

import db_objects.entity.Merch;
import db_objects.entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MerchDAO implements DAO<Merch> {

    private static final String selectSingle = "SELECT * FROM merch WHERE id = ";
    private static final String selectAll = "SELECT * FROM merch";
    private static final String insertSingle = "INSERT INTO merch (id, name, code) VALUES (?,?,?)";
    private static final String updateSingle = "UPDATE merch SET name = ?, code = ? WHERE id = ?";
    public static final String deleteSingle = "DELETE FROM merch WHERE id = ?";

    private final Connection connection;

    public MerchDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @NotNull
    @Override
    public Merch select(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(selectSingle + String.valueOf(id))) {
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
            try (var resultSet = statement.executeQuery(selectAll)) {
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
        try (var statement = connection.prepareStatement(updateSingle)) {
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
        try (var statement = connection.prepareStatement(insertSingle)){
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getCode());
//            statement.executeUpdate();
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
        try (var statement = connection.prepareStatement(deleteSingle)) {
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
