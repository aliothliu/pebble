package io.github.aliothliu.pebble.domain.admin;

import java.util.List;
import java.util.Optional;

public interface EmployeeTransferRepository {

    Optional<EmployeeTransfer> latest(EmployeeId employeeId);

    EmployeeTransfer save(EmployeeTransfer employeeTransfer);

    List<EmployeeTransfer> ofEmployeeId(EmployeeId employeeId);

    List<EmployeeTransfer> ofDepartmentId(String departmentId);

}