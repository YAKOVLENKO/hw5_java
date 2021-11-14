package java_migrations;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.nio.file.Path;
import java.sql.SQLException;
import java.sql.Statement;

import app.SQLReader;

public class V1__Create_tables extends BaseJavaMigration {
    private final String sql = SQLReader.readSQL(Path.of("sql/table_creation.sql"));

    @Override
    public void migrate(Context context) {
        try (Statement statement = context
                .getConnection()
                .createStatement()) {
            int status = statement.executeUpdate(sql);
            System.out.println(status);
            System.out.println("Tables created...");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
