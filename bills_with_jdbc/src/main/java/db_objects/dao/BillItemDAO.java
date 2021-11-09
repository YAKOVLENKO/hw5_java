package db_objects.dao;

import db_objects.entity.BillItem;
import db_objects.entity.Merch;
import db_objects.entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BillItemDAO implements DAO<BillItem> {

    private static final String selectSingle = "SELECT * FROM bill_items WHERE id = ";
    private static final String selectAll = "SELECT * FROM bill_items";
    private static final String insertSingle = "INSERT INTO bill_items (id, price, bill_id, merch_id, quantity) VALUES (?,?,?,?,?)";
    private static final String updateSingle = "UPDATE bill_items SET price = ?, bill_id = ?, merch_id = ?, quantity = ? WHERE id = ?";
    public static final String deleteSingle = "DELETE FROM bill_items WHERE id = ?";

    private final Connection connection;

    public BillItemDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @NotNull
    @Override
    public List<BillItem> select() {
        List<BillItem> billItems = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(selectAll)) {
                while (resultSet.next()) {
                    billItems.add(new BillItem(resultSet.getInt("id"),
                            resultSet.getFloat("price"),
                            resultSet.getInt("bill_id"),
                            resultSet.getInt("merch_id"),
                            resultSet.getInt("quantity")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billItems;
    }

    @NotNull
    @Override
    public BillItem select(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(selectSingle + String.valueOf(id))) {
                if (resultSet.next()) {
                    return new BillItem(resultSet.getInt("id"),
                            resultSet.getFloat("price"),
                            resultSet.getInt("bill_id"),
                            resultSet.getInt("merch_id"),
                            resultSet.getInt("quantity"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Bill item record with id " + id + "not found");
    }

    @Override
    public void insert(@NotNull BillItem entity) {
        try (var statement = connection.prepareStatement(insertSingle)){
            statement.setInt(1, entity.getId());
            statement.setDouble(2, entity.getPrice());
            statement.setInt(3, entity.getBillId());
            statement.setInt(4, entity.getMerchId());
            statement.setInt(5, entity.getQuantity());
//            statement.executeUpdate();
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while inserting bill item with id " + entity.getId()); // todo
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(@NotNull BillItem entity) {
        try (var statement = connection.prepareStatement(updateSingle)) {
            statement.setFloat(1, entity.getPrice());
            statement.setInt(2, entity.getBillId());
            statement.setInt(3, entity.getMerchId());
            statement.setInt(4, entity.getQuantity());
            statement.setInt(5, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Something went wrong while updating bill item with id " + entity.getId());  // todo
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NotNull BillItem entity) {
        try(var statement = connection.prepareStatement(deleteSingle)) {
            statement.setInt(1, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Bill item record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
