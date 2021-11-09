import app.JDBCCredentials;
import app.Report;
import db_objects.dao.*;
import db_objects.entity.Bill;
import db_objects.entity.BillItem;
import db_objects.entity.Merch;
import db_objects.entity.Organization;
import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Application {

    public static final JDBCCredentials CRED = JDBCCredentials.DEFAULT;

    public static void main(String[] args) {
        final Flyway flyway = Flyway
                .configure()
                .dataSource(CRED.url(), CRED.getLogin(), CRED.getPassw())
                .locations("java_migrations")
                .load();
        flyway.clean();
        flyway.migrate();
        System.out.println("Successful");

        try (Connection connection = DriverManager.getConnection(CRED.url(), CRED.getLogin(), CRED.getPassw())) {

            DAOItems dao = new DAOItems(connection);
            fillScheme(dao);

            Report report = new Report(connection, dao);
            LocalDate from = LocalDate.of(2004, 10, 17);
            LocalDate to = LocalDate.of(2004, 10, 20);

            Merch merch1 = dao.getMerchDAO().select(1);
            Merch merch2 = dao.getMerchDAO().select(2);


            System.out.println(report.getMerchInfo(from, to));
            System.out.println(report.chooseFirstTenOrganizations(merch2));
            System.out.println(report.getMerchAvgPrice(merch2, from, to));
            System.out.println(report.getMerchOfOrganizators(from, to));;

            LinkedHashMap<Merch, Integer> conds = new LinkedHashMap<>();
            conds.put(merch1, 2);
            conds.put(merch2, 3);
            System.out.println(report.organizationsWithMerchNumOver(conds));


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void fillScheme(DAOItems dao) {
        Organization org1 = new Organization(1, "Universitet", "123", "8800");
        Organization org2 = new Organization(2, "Cafe", "345", "8900");
        dao.getOrganizationDAO().insert(org1);
        dao.getOrganizationDAO().insert(org2);

        Bill bill1 = new Bill(1, "440A", LocalDate.of(2004, 10, 19), 1);
        dao.getBillDAO().insert(bill1);

        Merch merch1 = new Merch(1, "tea", "7600N");
        Merch merch2 = new Merch(2, "pirozhok", "7500K");
        dao.getMerchDAO().insert(merch1);
        dao.getMerchDAO().insert(merch2);

        BillItem billItem1 = new BillItem(1, 100, 1, 2, 20);
        BillItem billItem2 = new BillItem(2, 120, 1, 2, 100);
        BillItem billItem3 = new BillItem(3, 10, 1, 1, 100);
        dao.getBillItemDAO().insert(billItem1);
        dao.getBillItemDAO().insert(billItem2);
        dao.getBillItemDAO().insert(billItem3);


    }
}
