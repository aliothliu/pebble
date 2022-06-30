package io.github.aliothliu.pebble.infrastructure.persistence.admin;

import io.github.aliothliu.pebble.domain.admin.Department;
import io.github.aliothliu.pebble.domain.admin.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final JpaDepartmentRepository departmentRepository;

    @Override
    public Department save(Department department) {
        return this.departmentRepository.save(department);
    }

    @Override
    public void saveAll(Iterable<Department> departments) {
        this.departmentRepository.saveAll(departments);
    }

    @Override
    public Optional<Department> ofId(String id) {
        if (!StringUtils.hasText(id)) {
            return Optional.empty();
        }
        return this.departmentRepository.findById(id);
    }

    @Override
    public boolean hasChildren(String id) {
        if (!StringUtils.hasText(id)) {
            return false;
        }
        return this.departmentRepository.count(this.withParentId(id)) > 0;
    }

    @Override
    public List<Department> roots() {
        return this.departmentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.isNull(root.get(Department.Fields.parentId)));
    }

    @Override
    public List<Department> children(String id) {
        return this.departmentRepository.findAll(this.withParentId(id));
    }

    private Specification<Department> withParentId(String id) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(Department.Fields.parentId), id);
    }

    @Override
    public boolean nonexistence(String id) {
        return !this.departmentRepository.existsById(id);
    }
}
