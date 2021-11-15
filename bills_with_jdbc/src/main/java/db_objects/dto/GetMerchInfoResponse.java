package db_objects.dto;

import db_objects.dao.DAOItems;
import db_objects.entity.Merch;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@AllArgsConstructor
public class GetMerchInfoResponse implements Comparable<GetMerchInfoResponse> {
    @NotNull private Date date;
    @NotNull private Merch merch;
    private int quantity;
    private float price;

    public GetMerchInfoResponse(@NotNull ResultSet resultSet, @NotNull DAOItems dao) throws SQLException {
        int merchId = resultSet.getInt("id");
        this.setDate(resultSet.getDate("day"));
        this.setMerch(dao.getMerchDAO().select(merchId));
        this.setQuantity(resultSet.getInt("total_quantity"));
        this.setPrice(resultSet.getFloat("total_price"));
    }

    @Override
    public int compareTo(@NotNull GetMerchInfoResponse o) {
        if (date.equals(o.getDate()))
            return merch.getId() - o.getMerch().getId();
        return date.compareTo(o.getDate());
    }
}
