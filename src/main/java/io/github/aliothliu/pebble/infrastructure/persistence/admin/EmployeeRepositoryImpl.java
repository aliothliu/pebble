package io.github.aliothliu.pebble.infrastructure.persistence.admin;

import io.github.aliothliu.pebble.domain.admin.Employee;
import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import io.github.aliothliu.pebble.domain.admin.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.*;

@RequiredArgsConstructor
@Repository
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final JpaEmployeeRepository employeeRepository;

    @NonNull
    @Override
    public Employee save(Employee employee) {
        return this.employeeRepository.save(employee);
    }

    @NonNull
    @Override
    public List<Employee> allEmployees(Set<String> idList) {
        if (Objects.isNull(idList) || idList.isEmpty()) {
            return new ArrayList<>();
        }
        return this.employeeRepository.findAll((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.in(root.get(Employee.Fields.employeeId)).value(idList));
    }

    @Override
    public Optional<Employee> ofId(EmployeeId employeeId) {
        return this.employeeRepository.findOne(this.withEmployeeId(employeeId));
    }

    @Override
    public boolean nonexistence(EmployeeId employeeId) {
        return this.employeeRepository.count(this.withEmployeeId(employeeId)) == 0;
    }

    private Specification<Employee> withEmployeeId(EmployeeId employeeId) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(Employee.Fields.employeeId), employeeId);
    }

    @Override
    public void changePassword(Employee employee) {
        this.employeeRepository.save(employee);
    }

    @NonNull
    @Override
    public String nextEmployeeId() {
        return UUID.randomUUID().toString();
    }
}
