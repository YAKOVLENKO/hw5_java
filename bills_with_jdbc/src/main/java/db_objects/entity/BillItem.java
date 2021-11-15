package db_objects.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BillItem implements Entity {
    private int id;
    private float price;
    private int billId;
    private int merchId;
    private int quantity;
}
