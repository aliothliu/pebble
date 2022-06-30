package io.github.aliothliu.pebble.application.admin;

import io.github.aliothliu.marble.application.RbacUserSubject;
import io.github.aliothliu.marble.domain.role.RoleCode;
import io.github.aliothliu.pebble.application.admin.query.EmployeeQuery;
import io.github.aliothliu.pebble.application.admin.representation.EmployeeDetailRepresentation;
import io.github.aliothliu.pebble.application.admin.representation.EmployeeRepresentation;
import io.github.aliothliu.pebble.domain.Cellphone;
import io.github.aliothliu.pebble.domain.PhoneticizeName;
import io.github.aliothliu.pebble.domain.admin.*;
import io.github.aliothliu.pebble.infrastructure.persistence.admin.JpaEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.persistence.criteria.Predicate;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class EmployeeQueryService {

    private final JpaEmployeeRepository employeeRepository;

    private final EmployeeTransferRepository transferRepository;

    public Page<EmployeeRepresentation> paging(@NotNull EmployeeQuery query, @NotNull Pageable pageable) {
        return this.employeeRepository.findAll(this.buildEmployeeSpec(query), pageable).map(this::from);
    }

    public Optional<EmployeeDetailRepresentation> getOne(@NotNull EmployeeId id) {
        return this.employeeRepository.findByEmployeeId(id).map(this::detailFrom);
    }

    private Specification<Employee> buildEmployeeSpec(EmployeeQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(query.getName())) {
                predicates.add(criteriaBuilder.like(root.get(Employee.Fields.name).get(PhoneticizeName.Fields.name), "%" + query.getName() + "%"));
            }
            if (StringUtils.hasText(query.getCellphone())) {
                predicates.add(criteriaBuilder.like(root.get(Employee.Fields.cellphone).get(Cellphone.Fields.number), "%" + query.getCellphone() + "%"));
            }
            if (StringUtils.hasText(query.getDepartmentId())) {
                // FIXME: redundancy departmentId in employee entity
                Set<EmployeeId> employeeIds = this.transferRepository.ofDepartmentId(query.getDepartmentId())
                        .stream()
                        .map(EmployeeTransfer::getEmployeeId)
                        .collect(Collectors.toSet());
                predicates.add(criteriaBuilder.in(root.get(Employee.Fields.employeeId)).value(employeeIds));
            }
            if (Objects.nonNull(query.getStatus())) {
                predicates.add(criteriaBuilder.equal(root.get(Employee.Fields.workStatus).get(WorkStatus.Fields.status), query.getStatus()));
            }
            return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    private EmployeeRepresentation from(Employee employee) {
        EmployeeRepresentation representation = new EmployeeRepresentation();
        this.setRepresentation(representation, employee);
        return representation;
    }

    private void setRepresentation(EmployeeRepresentation representation, Employee employee) {
        representation.setId(employee.getEmployeeId().getId());
        representation.setUsername(employee.getUsername().getValue());
        representation.setName(employee.getName().getName());
        representation.setCellphone(employee.getCellphone().getNumber());
        representation.setWorkStatus(employee.getWorkStatus().getStatus().name());
        representation.setWorkStatusDesc(employee.getWorkStatus().getStatus().getDesc());
        if (Objects.nonNull(employee.getGender())) {
            representation.setGender(employee.getGender().name());
            representation.setGenderDesc(employee.getGender().getDesc());
        }
        representation.setBirthDay(employee.getBirthday());
        if (Objects.nonNull(employee.getIdCard())) {
            representation.setIdCardNo(employee.getIdCard().getIdCardNo());
        }

        employee.department().ifPresent(department -> {
            representation.setDepartmentId(department.getId());
            representation.setDepartmentName(department.getName().getName());
        });
    }

    private EmployeeDetailRepresentation detailFrom(Employee employee) {
        EmployeeDetailRepresentation representation = new EmployeeDetailRepresentation();
        this.setRepresentation(representation, employee);

        representation.setTransfers(
                transferRepository.ofEmployeeId(employee.getEmployeeId())
                        .stream()
                        .map(t -> {
                            EmployeeDetailRepresentation.Transfer transfer = new EmployeeDetailRepresentation.Transfer();
                            transfer.setDepartmentId(t.getDepartmentId());
                            transfer.setDepartmentName(t.departmentName());
                            transfer.setTransferDate(t.getTransferDate());

                            return transfer;
                        })
                        .collect(Collectors.toList())
        );

        representation.setRoles(new RbacUserSubject(employee.getEmployeeId().getId()).loadRoles().stream().map(RoleCode::getCode).collect(Collectors.toList()));

        return representation;
    }
}
