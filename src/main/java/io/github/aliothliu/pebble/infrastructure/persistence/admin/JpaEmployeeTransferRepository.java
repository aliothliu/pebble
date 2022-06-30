package io.github.aliothliu.pebble.infrastructure.persistence.admin;

import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import io.github.aliothliu.pebble.domain.admin.EmployeeTransfer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaEmployeeTransferRepository extends PagingAndSortingRepository<EmployeeTransfer, String>, JpaSpecificationExecutor<EmployeeTransfer> {

    Optional<EmployeeTransfer> findTopByEmployeeIdOrderByTransferDateDesc(EmployeeId employeeId);

    List<EmployeeTransfer> findByDepartmentId(String departmentId);

    List<EmployeeTransfer> findByEmployeeId(EmployeeId employeeId);
}
