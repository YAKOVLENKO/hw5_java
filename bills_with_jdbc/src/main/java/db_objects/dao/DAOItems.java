package db_objects.dao;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;


public class DAOItems {
    @Getter @NotNull private final BillDAO billDAO;
    @Getter @NotNull private final BillItemDAO billItemDAO;
    @Getter @NotNull private final MerchDAO merchDAO;
    @Getter @NotNull private final OrganizationDAO organizationDAO;

    public DAOItems(@NotNull Connection connection) {
        billDAO = new BillDAO(connection);
        billItemDAO = new BillItemDAO(connection);
        merchDAO = new MerchDAO(connection);
        organizationDAO = new OrganizationDAO(connection);
    }
}
