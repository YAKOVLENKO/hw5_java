package db_objects.dao;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DAO<T> {
    @NotNull T select(int id);

    @NotNull List<T> select();

    void insert(@NotNull T entity);
    void update(@NotNull T entity);
    void delete(@NotNull T entity);
}
