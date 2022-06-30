package io.github.aliothliu.pebble.infrastructure.persistence.admin;

import io.github.aliothliu.pebble.domain.admin.Department;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaDepartmentRepository extends CrudRepository<Department, String>, JpaSpecificationExecutor<Department> {

    Optional<Department> findTopByParentIdOrderBySortDesc(String parentId);
}
