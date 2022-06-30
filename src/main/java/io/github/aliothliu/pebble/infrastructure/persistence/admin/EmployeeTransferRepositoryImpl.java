package io.github.aliothliu.pebble.infrastructure.persistence.admin;

import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import io.github.aliothliu.pebble.domain.admin.EmployeeTransfer;
import io.github.aliothliu.pebble.domain.admin.EmployeeTransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class EmployeeTransferRepositoryImpl implements EmployeeTransferRepository {

    private final JpaEmployeeTransferRepository transferRepository;

    @Override
    public Optional<EmployeeTransfer> latest(EmployeeId employeeId) {
        return transferRepository.findTopByEmployeeIdOrderByTransferDateDesc(employeeId);
    }

    @Override
    public EmployeeTransfer save(EmployeeTransfer employeeTransfer) {
        return this.transferRepository.save(employeeTransfer);
    }

    @Override
    public List<EmployeeTransfer> ofEmployeeId(EmployeeId employeeId) {
        return this.transferRepository.findByEmployeeId(employeeId);
    }

    @Override
    public List<EmployeeTransfer> ofDepartmentId(String departmentId) {
        return this.transferRepository.findByDepartmentId(departmentId);
    }
}
