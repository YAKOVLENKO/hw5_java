package db_objects.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Merch {
    private int id;
    @NotNull private String name;
    @NotNull private String code;
}
