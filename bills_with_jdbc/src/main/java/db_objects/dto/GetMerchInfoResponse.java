package db_objects.dto;

import db_objects.entity.Merch;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

@Data
@AllArgsConstructor
public class GetMerchInfoResponse implements Comparable<GetMerchInfoResponse> {
    @NotNull private Date date;
    @NotNull private Merch merch;
    private int quantity;
    private float price;

    @Override
    public int compareTo(@NotNull GetMerchInfoResponse o) {
        if (date.equals(o.getDate()))
            return merch.getId() - o.getMerch().getId();
        return date.compareTo(o.getDate());
    }
}
