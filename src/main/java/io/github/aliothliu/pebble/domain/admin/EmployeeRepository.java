package io.github.aliothliu.pebble.domain.admin;

import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeRepository {

    @NonNull
    Employee save(Employee employee);

    @NonNull
    List<Employee> allEmployees(Set<String> idList);

    Optional<Employee> ofId(EmployeeId employeeId);

    boolean nonexistence(EmployeeId employeeId);

    void changePassword(Employee employee);

    @NonNull
    String nextEmployeeId();
}
