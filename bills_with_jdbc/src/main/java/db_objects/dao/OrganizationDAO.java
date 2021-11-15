package db_objects.dao;

import db_objects.entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrganizationDAO extends BaseDAO<Organization> {
    private static final String SELECT_SINGLE = "SELECT * FROM organizations WHERE id = ";
    private static final String SELECT_ALL = "SELECT * FROM organizations";
    private static final String INSERT_SINGLE = "INSERT INTO organizations (id, name, inn, account) VALUES (?,?,?,?)";
    private static final String UPDATE_SINGLE = "UPDATE organizations SET name = ?, inn = ?, account = ? WHERE id = ?";
    public static final String DELETE_SINGLE = "DELETE FROM organizations WHERE id = ?";


    public OrganizationDAO(@NotNull Connection connection) {
        super(connection, Organization.class,
                SELECT_SINGLE, SELECT_ALL, INSERT_SINGLE, UPDATE_SINGLE, DELETE_SINGLE);
    }

    @Override
    protected Organization getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Organization(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("inn"),
                resultSet.getString("account"));
    }

    @Override
    protected void setupStatementForUpdate(@NotNull PreparedStatement statement,
                                           @NotNull Organization entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getInn());
        statement.setString(3, entity.getAccount());
        statement.setInt(4, entity.getId());
    }

    @Override
    protected void setupStatementForInsert(@NotNull PreparedStatement statement,
                                           @NotNull Organization entity) throws SQLException {
        statement.setInt(1, entity.getId());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getInn());
        statement.setString(4, entity.getAccount());
    }
}
