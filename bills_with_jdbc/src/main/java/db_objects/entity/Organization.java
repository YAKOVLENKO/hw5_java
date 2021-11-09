package db_objects.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class Organization {
    private int id;
    @NotNull private String name;
    @NotNull private String inn;
    @NotNull private String account;
}
