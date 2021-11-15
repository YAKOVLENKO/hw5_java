package db_objects.dao;

import db_objects.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDAO<T extends Entity> implements DAO<T>{
    private final String selectSingle;
    private final String selectAll;
    private final String insertSingle;
    private final String updateSingle;
    private final String deleteSingle;

    private final Class<T> typeParameterClass;

    private final Connection connection;

    BaseDAO(Connection connection, @NotNull Class<T> typeParameterClass, @NotNull String selectSingle,
            @NotNull String selectAll, @NotNull String insertSingle, @NotNull String updateSingle,
            @NotNull String deleteSingle) {
        this.connection = connection;
        this.selectSingle = selectSingle;
        this.selectAll = selectAll;
        this.insertSingle = insertSingle;
        this.updateSingle = updateSingle;
        this.deleteSingle = deleteSingle;

        this.typeParameterClass = typeParameterClass;
    }

    @NotNull
    @Override
    public T select(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(selectSingle + id)) {
                if (resultSet.next()) {
                    return getFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException(typeParameterClass.getSimpleName() + " record with id " + id + " not found");
    }

    protected abstract T getFromResultSet(ResultSet resultSet) throws SQLException ;

    @NotNull
    @Override
    public List<T> select() {
        List<T> values = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(selectAll)) {
                while (resultSet.next()) {
                    T value = getFromResultSet(resultSet);
                    values.add(value);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return values;
    }

    protected abstract void setupStatementForInsert(@NotNull PreparedStatement statement,
                                           @NotNull T entity) throws SQLException;

    @Override
    public void insert(@NotNull T entity) {
        try (var statement = connection.prepareStatement(insertSingle)) {
            setupStatementForInsert(statement, entity);
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while inserting " +
                        typeParameterClass.getSimpleName() +
                         ". Wrong record: " + entity);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected abstract void  setupStatementForUpdate(@NotNull PreparedStatement statement,
                                            @NotNull T entity) throws SQLException;

    @Override
    public void update(@NotNull T entity) {
        try (var statement = connection.prepareStatement(updateSingle)) {
            setupStatementForUpdate(statement, entity);
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while updating " +
                        typeParameterClass.getSimpleName() +
                        ". Wrong record: " + entity);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NotNull T entity) {
        try(var statement = connection.prepareStatement(deleteSingle)) {
            statement.setInt(1, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException(typeParameterClass.getSimpleName() +
                        " record with id = " +
                        entity.getId() +
                        " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
