package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.Sort;
import io.github.aliothliu.pebble.infrastructure.persistence.admin.JpaDepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门排序领域服务
 */
@RequiredArgsConstructor
@Component
public class DepartmentSortService {

    private final JpaDepartmentRepository repository;

    public Sort last(String position) {
        /*
         * 查询数据库同级别部门最大排序数字，不保证数据排序的唯一性，多线程情况，线程A读取到数据库最大值，线程B读取到数据库最大值，此时两个线程计算出的排序数字是相同的。
         * 由于部门的排序改动频率较低，几乎不可能出现并发问题，因此代码实现是可以接受的。在对排序一致性要求较高或者并发较高的情况下，推荐使用分布式中间件解决排序数字重复生成的问题。
         */

        return this.repository.findTopByParentIdOrderBySortDesc(position)
                .map(Department::getSort)
                .map(sort -> {
                    sort.increment();
                    return sort;
                })
                .orElse(Sort.one());
    }

    public Sort insert(String position) {
        /*
         * 具有副作用的获取排序数字的实现
         */
        if (Objects.isNull(position)) {
            return Sort.one();
        }
        return this.repository.findById(position)
                .map(department -> {
                    this.resortDepartment(department, true);
                    return department.getSort();
                }).orElse(Sort.one());
    }

    public Sort append(String position) {
        /*
         * 具有副作用的获取排序数字的实现
         */
        if (Objects.isNull(position)) {
            return Sort.one();
        }
        return this.repository.findById(position)
                .map(department -> {
                    this.resortDepartment(department, false);
                    return department.getSort();
                }).orElse(Sort.one());
    }

    private void resortDepartment(Department baselineDepartment, boolean forward) {
        this.repository.saveAll(this.repository
                .findAll(this.insertSortSpecification(baselineDepartment, forward))
                .stream()
                .peek(department -> department.getSort().increment())
                .collect(Collectors.toList())
        );
    }

    private Specification<Department> insertSortSpecification(Department position, boolean forward) {
        return ((root, criteriaQuery, criteriaBuilder) -> {
            Sort baseSort = position.getSort();
            if (!forward) {
                baseSort.increment();
            }
            List<Predicate> predicates = new ArrayList<>(Collections.singletonList(
                    criteriaBuilder.greaterThanOrEqualTo(root.get(Department.Fields.sort), baseSort)));
            if (StringUtils.hasText(position.getParentId())) {
                predicates.add(criteriaBuilder.equal(root.get(Department.Fields.parentId), position.getParentId()));
            }
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(Department.Fields.sort)));

            return criteriaQuery.where(predicates.toArray(new Predicate[0])).getRestriction();
        });
    }
}
