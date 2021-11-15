package db_objects.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Bill implements Entity {
    private int id;
    @NotNull private String number;
    @NotNull private LocalDate date;
    private int senderId;
}


