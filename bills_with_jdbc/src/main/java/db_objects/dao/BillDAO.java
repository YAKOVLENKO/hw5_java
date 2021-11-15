package db_objects.dao;

import db_objects.entity.Bill;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

public class BillDAO extends BaseDAO<Bill>{
    private static final String SELECT_SINGLE = "SELECT * FROM bills WHERE id = ";
    private static final String SELECT_ALL = "SELECT * FROM bills";
    private static final String INSERT_SINGLE = "INSERT INTO bills (number, date, sender_id) VALUES (?,?::date,?)";
    private static final String UPDATE_SINGLE = "UPDATE bills SET number = ?, date = ?, sender_id = ? WHERE id = ?";
    public static final String DELETE_SINGLE = "DELETE FROM bills WHERE id = ?";


    public BillDAO(@NotNull Connection connection) {
        super(connection, Bill.class,
                SELECT_SINGLE, SELECT_ALL, INSERT_SINGLE, UPDATE_SINGLE, DELETE_SINGLE);

    }

    @Override
    protected Bill getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Bill(resultSet.getInt("id"),
                resultSet.getString("number"),
                resultSet.getDate("date").toLocalDate(),
                resultSet.getInt("sender_id"));
    }

    @Override
    protected void setupStatementForInsert(@NotNull PreparedStatement statement,
                                           @NotNull Bill entity) throws SQLException {
        statement.setString(1, entity.getNumber());
        statement.setString(2, entity.getDate().toString());
        statement.setInt(3, entity.getSenderId());
    }

    @Override
    protected void  setupStatementForUpdate(@NotNull PreparedStatement statement,
                                            @NotNull Bill entity) throws SQLException {
        statement.setString(1, entity.getNumber());
        statement.setDate(2, Date.valueOf(entity.getDate()));
        statement.setInt(3, entity.getSenderId());
        statement.setInt(4, entity.getId());
    }
}
