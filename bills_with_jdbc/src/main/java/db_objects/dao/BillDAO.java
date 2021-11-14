package db_objects.dao;

import db_objects.entity.Bill;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillDAO implements DAO<Bill> {

    private static final String SELECT_SINGLE = "SELECT * FROM bills WHERE id = ";
    private static final String SELECT_ALL = "SELECT * FROM bills";
    private static final String INSERT_SINGLE = "INSERT INTO bills (number, date, sender_id) VALUES (?,?::date,?)";
    private static final String UPDATE_SINGLE = "UPDATE bills SET number = ?, date = ?, sender_id = ? WHERE id = ?";
    public static final String DELETE_DINGLE = "DELETE FROM bills WHERE id = ?";

    private final Connection connection;

    public BillDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @NotNull
    @Override
    public Bill select(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_SINGLE + id)) {
                if (resultSet.next()) {
                    return new Bill(resultSet.getInt("id"),
                            resultSet.getString("number"),
                            resultSet.getDate("date").toLocalDate(),
                            resultSet.getInt("sender_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Bill record with id " + id + "not found");
    }

    @NotNull
    @Override
    public List<Bill> select() {
        List<Bill> bills = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(SELECT_ALL)) {
                while (resultSet.next()) {
                    Bill bill = new Bill(resultSet.getInt("id"),
                            resultSet.getString("number"),
                            resultSet.getDate("date").toLocalDate(),
                            resultSet.getInt("sender_id"));
                    bills.add(bill);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @Override
    public void insert(@NotNull Bill entity) {
        try (var statement = connection.prepareStatement(INSERT_SINGLE)) {
            statement.setString(1, entity.getNumber());
            statement.setString(2, entity.getDate().toString());
            statement.setInt(3, entity.getSenderId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while inserting bill with id " + entity.getId()); // todo
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(@NotNull Bill entity) {
        try (var statement = connection.prepareStatement(UPDATE_SINGLE)) {
            statement.setString(1, entity.getNumber());
            statement.setDate(2, Date.valueOf(entity.getDate()));
            statement.setInt(3, entity.getSenderId());
            statement.setInt(4, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while updating bill with id " + entity.getId()); // todo
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NotNull Bill entity) {
        try(var statement = connection.prepareStatement(DELETE_DINGLE)) {
            statement.setInt(1, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Bill record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
