package io.github.aliothliu.pebble.domain.admin;

import java.util.List;
import java.util.Optional;

/**
 * DepartmentRepository
 *
 * @author liubin
 **/
public interface DepartmentRepository {

    Department save(Department department);

    void saveAll(Iterable<Department> departments);

    Optional<Department> ofId(String id);

    boolean hasChildren(String id);

    List<Department> roots();

    List<Department> children(String id);

    boolean nonexistence(String id);
}
