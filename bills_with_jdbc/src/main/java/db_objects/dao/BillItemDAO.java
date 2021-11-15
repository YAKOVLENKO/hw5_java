package db_objects.dao;

import db_objects.entity.BillItem;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BillItemDAO extends BaseDAO<BillItem> {

    private static final String SELECT_SINGLE = "SELECT * FROM bill_items WHERE id = ";
    private static final String SELECT_ALL = "SELECT * FROM bill_items";
    private static final String INSERT_SINGLE = "INSERT INTO bill_items (id, price, bill_id, merch_id, quantity) VALUES (?,?,?,?,?)";
    private static final String UPDATE_SINGLE = "UPDATE bill_items SET price = ?, bill_id = ?, merch_id = ?, quantity = ? WHERE id = ?";
    public static final String DELETE_SINGLE = "DELETE FROM bill_items WHERE id = ?";

    public BillItemDAO(@NotNull Connection connection) {
        super(connection, BillItem.class,
                SELECT_SINGLE, SELECT_ALL, INSERT_SINGLE, UPDATE_SINGLE, DELETE_SINGLE);
    }

    @Override
    protected BillItem getFromResultSet(ResultSet resultSet) throws SQLException {
        return new BillItem(resultSet.getInt("id"),
                resultSet.getFloat("price"),
                resultSet.getInt("bill_id"),
                resultSet.getInt("merch_id"),
                resultSet.getInt("quantity"));
    }

    @Override
    protected void setupStatementForInsert(@NotNull PreparedStatement statement,
                                           @NotNull BillItem entity) throws SQLException {
        statement.setInt(1, entity.getId());
        statement.setDouble(2, entity.getPrice());
        statement.setInt(3, entity.getBillId());
        statement.setInt(4, entity.getMerchId());
        statement.setInt(5, entity.getQuantity());
    }

    @Override
    protected void setupStatementForUpdate(@NotNull PreparedStatement statement,
                                           @NotNull BillItem entity) throws SQLException {
        statement.setFloat(1, entity.getPrice());
        statement.setInt(2, entity.getBillId());
        statement.setInt(3, entity.getMerchId());
        statement.setInt(4, entity.getQuantity());
        statement.setInt(5, entity.getId());
    }

}
