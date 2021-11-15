package db_objects.dao;

import db_objects.entity.Merch;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MerchDAO extends BaseDAO<Merch> {

    private static final String SELECT_SINGLE = "SELECT * FROM merch WHERE id = ";
    private static final String SELECT_ALL = "SELECT * FROM merch";
    private static final String INSERT_SINGLE = "INSERT INTO merch (id, name, code) VALUES (?,?,?)";
    private static final String UPDATE_SINGLE = "UPDATE merch SET name = ?, code = ? WHERE id = ?";
    public static final String DELETE_SINGLE = "DELETE FROM merch WHERE id = ?";


    public MerchDAO(@NotNull Connection connection) {
        super(connection, Merch.class,
                SELECT_SINGLE, SELECT_ALL, INSERT_SINGLE, UPDATE_SINGLE, DELETE_SINGLE);
    }

    @Override
    protected Merch getFromResultSet(ResultSet resultSet) throws SQLException {
        return new Merch(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("code"));
    }

    @Override
    protected void setupStatementForUpdate(@NotNull PreparedStatement statement,
                                           @NotNull Merch entity) throws SQLException {
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getCode());
        statement.setInt(3, entity.getId());
    }

    @Override
    protected void setupStatementForInsert(@NotNull PreparedStatement statement,
                                           @NotNull Merch entity) throws SQLException {
        statement.setInt(1, entity.getId());
        statement.setString(2, entity.getName());
        statement.setString(3, entity.getCode());
    }

}
