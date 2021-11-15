package app;

import db_objects.dao.DAOItems;
import db_objects.dto.GetMerchInfoResponse;
import db_objects.dto.GetMerchOfOrgsResponse;
import db_objects.entity.Merch;
import db_objects.entity.Organization;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;


public class Report {
    private final Connection connection;
    private final DAOItems dao;

    public static final String FIRST_QUERY = SQLReader.readSQL(Path.of("sql/first.sql"));
    public static final String SECOND_QUERY = SQLReader.readSQL(Path.of("sql/second.sql"));
    public static final String THIRD_QUERY = SQLReader.readSQL(Path.of("sql/third.sql"));
    public static final String FOURTH_QUERY = SQLReader.readSQL(Path.of("sql/fourth.sql"));
    public static final String FIFTH_QUIERY = SQLReader.readSQL(Path.of("sql/fifth.sql"));

    public Report(@NotNull Connection connection, DAOItems dao) {
        this.connection = connection;
        this.dao = dao;
    }

    public @NotNull List<Organization> chooseFirstTenOrganizations(@NotNull Merch merch) {
        // Выбрать первые 10 поставщиков по количеству поставленного товара
        List<Organization> organizationsTop10 = new ArrayList<>();
        try (var statement = connection.prepareStatement(FIRST_QUERY)) {
            statement.setInt(1, merch.getId());
            try (var resultSet = statement.executeQuery()) {
                Organization organization;
                while (resultSet.next()) {
                    int orgId = resultSet.getInt("id");
                    organization = dao.getOrganizationDAO().select(orgId);
                    organizationsTop10.add(organization);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organizationsTop10;
    }

    public @NotNull List<Organization> organizationsWithMerchNumOver(@NotNull LinkedHashMap<Merch, Integer> cond) {
        // Выбрать поставщиков с суммой поставленного товара выше указанного количества
        // (товар и его количество должны допускать множественное указание).
        List<Organization> organizations = new ArrayList<>();
        Integer[] merchIds = cond.keySet().stream().map(Merch::getId).toArray(Integer[]::new);

        try(var statement = connection.prepareStatement(SECOND_QUERY)) {
            Array ids = connection.createArrayOf("INTEGER", merchIds);
            statement.setArray(1, ids);
            statement.setArray(2, ids);
            statement.setArray(3, ids);

            try (var resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    int orgId = resultSet.getInt("id");
                    organizations.add(dao.getOrganizationDAO().select(orgId));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return organizations;
    }

    public List<GetMerchInfoResponse> getMerchInfo(LocalDate from, LocalDate to) {
        // За каждый день для каждого товара рассчитать количество и сумму
        // полученного товара в указанном периоде
        List<GetMerchInfoResponse> responseList = new ArrayList<>();
        try (var statement = connection.prepareStatement(THIRD_QUERY)) {
            statement.setString(1, from.toString());
            statement.setString(2, to.toString());
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()){
                    responseList.add(new GetMerchInfoResponse(resultSet, dao));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responseList;
    }

    public double getMerchAvgPrice(Merch merch, LocalDate from, LocalDate to) {
        // Рассчитать среднюю цену полученного товара за период
        try (var statement = connection.prepareStatement(FOURTH_QUERY)) {
            statement.setString(1, from.toString());
            statement.setString(2, to.toString());
            statement.setInt(3, merch.getId());
            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("avg_price");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("Что-то не так!");
    }

    public List<GetMerchOfOrgsResponse> getMerchOfOrganizators(LocalDate from, LocalDate to) {
        // Вывести список товаров, поставленных организациями за период.
        // Если организация товары не поставляла, то она все равно должна быть отражена в списке.
        List<GetMerchOfOrgsResponse> responses = new ArrayList<>();
        try (var statement = connection.prepareStatement(FIFTH_QUIERY)){
            statement.setString(1, from.toString());
            statement.setString(2, to.toString());
            try (var resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    responses.add(new GetMerchOfOrgsResponse(resultSet, dao));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return responses;
    }



}
