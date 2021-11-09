package db_objects.dao;

import db_objects.entity.Bill;
import db_objects.entity.BillItem;
import db_objects.entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO implements DAO<Organization> {
    private static final String selectSingle = "SELECT * FROM organizations WHERE id = ";
    private static final String selectAll = "SELECT * FROM organizations";
    private static final String insertSingle = "INSERT INTO organizations (id, name, inn, account) VALUES (?,?,?,?)";
    private static final String updateSingle = "UPDATE organizations SET name = ?, inn = ?, account = ? WHERE id = ?";
    public static final String deleteSingle = "DELETE FROM organizations WHERE id = ?";

    private final Connection connection;

    public OrganizationDAO(@NotNull Connection connection) {
        this.connection = connection;
    }

    @NotNull
    @Override
    public List<Organization> select() {
        List<Organization> bills = new ArrayList<>();
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(selectAll)) {
                while (resultSet.next()) {
                    Organization organization = new Organization(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("inn"),
                            resultSet.getString("account"));
                    bills.add(organization);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }

    @NotNull
    @Override
    public Organization select(int id) {
        try (var statement = connection.createStatement()) {
            try (var resultSet = statement.executeQuery(selectSingle + String.valueOf(id))) {
                if (resultSet.next()) {
                    return new Organization(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("inn"),
                            resultSet.getString("account"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Organization record with id " + id + "not found");
    }

    @Override
    public void update(@NotNull Organization entity) {
        try (var statement = connection.prepareStatement(updateSingle)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getInn());
            statement.setString(3, entity.getAccount());
            statement.setInt(4, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Organization record with id = " + entity.getId() + " not found");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(@NotNull Organization entity) {
        try(var statement = connection.prepareStatement(deleteSingle)) {
            statement.setInt(1, entity.getId());
            if (statement.executeUpdate() == 0) {
                throw new IllegalStateException("Organization record with id = " + entity.getId() + " not found");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void insert(@NotNull Organization entity) {
        try (var statement = connection.prepareStatement(insertSingle)) {
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getInn());
            statement.setString(4, entity.getAccount());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
