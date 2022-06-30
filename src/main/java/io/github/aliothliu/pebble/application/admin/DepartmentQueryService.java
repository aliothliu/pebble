package io.github.aliothliu.pebble.application.admin;

import io.github.aliothliu.pebble.application.admin.representation.DepartmentDetailRepresentation;
import io.github.aliothliu.pebble.application.admin.representation.DepartmentSimplifyRepresentation;
import io.github.aliothliu.pebble.domain.admin.Department;
import io.github.aliothliu.pebble.domain.admin.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentQueryService {

    private final DepartmentRepository repository;

    public Optional<DepartmentDetailRepresentation> getOne(@NotBlank String id) {
        return this.repository
                .ofId(id)
                .map(Department::loadParent)
                .map(this::detail);
    }

    public List<DepartmentSimplifyRepresentation> forest() {
        return this.repository
                .roots()
                .stream()
                .map(this::recursionDepartment)
                .collect(Collectors.toList());
    }

    private DepartmentSimplifyRepresentation recursionDepartment(Department department) {
        DepartmentSimplifyRepresentation treeVo = this.simplify(department);
        if (!department.isLeaf()) {
            List<Department> children = department.loadChildren().getChildren();
            List<DepartmentSimplifyRepresentation> voChildren = new ArrayList<>();
            for (Department child : children) {
                voChildren.add(this.simplify(child));
            }
            treeVo.setChildren(voChildren);
        }
        return treeVo;
    }

    private DepartmentSimplifyRepresentation simplify(Department department) {
        return DepartmentSimplifyRepresentation
                .builder()
                .id(department.getId())
                .name(department.getName().getName())
                .description(department.getDescription())
                .children(new ArrayList<>())
                .build();
    }

    private DepartmentDetailRepresentation detail(Department department) {
        return DepartmentDetailRepresentation
                .builder()
                .id(department.getId())
                .name(department.getName().getName())
                .parentId(department.getParentId())
                .parentName(department.parentName())
                .description(department.getDescription())
                .build();
    }
}
