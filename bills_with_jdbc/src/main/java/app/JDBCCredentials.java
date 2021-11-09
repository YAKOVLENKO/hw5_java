package app;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public final class JDBCCredentials {
    public static final @NotNull JDBCCredentials DEFAULT = new JDBCCredentials(
            "localhost",
            "5432",
            "bills_with_jdbc",
            "postgres",
            "newPassword"
    );

    private final @NotNull String prefix = "jdbc:postgresql";

    private final String host;
    private final String port;
    private final String dbName;

    @Getter
    private final String login;
    @Getter
    private final String passw;

    private JDBCCredentials(@NotNull String host,
                            @NotNull String port,
                            @NotNull String dbName,
                            @NotNull String login,
                            @NotNull String passw) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.login = login;
        this.passw = passw;
    }
    public @NotNull String url() {
        return prefix + "://" + host + ":" + port + "/" + dbName;
    }
}
