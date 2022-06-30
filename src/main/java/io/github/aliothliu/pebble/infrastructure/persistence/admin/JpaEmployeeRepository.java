package io.github.aliothliu.pebble.infrastructure.persistence.admin;

import io.github.aliothliu.pebble.domain.admin.Employee;
import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaEmployeeRepository extends PagingAndSortingRepository<Employee, String>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeId(EmployeeId id);
}
