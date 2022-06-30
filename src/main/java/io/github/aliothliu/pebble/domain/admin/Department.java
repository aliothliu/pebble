package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.Sort;
import io.github.aliothliu.marble.domain.ValidationHandler;
import io.github.aliothliu.pebble.PebbleRegistry;
import io.github.aliothliu.pebble.domain.ConcurrencySafeEntity;
import io.github.aliothliu.pebble.domain.PhoneticizeName;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Department
 *
 * @author liubin
 **/
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = {"parent", "children", "ancestors", "descendants"})
@Entity
@Table(name = "admin_department")
@Getter
@FieldNameConstants
public class Department extends ConcurrencySafeEntity {

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "name")),
            @AttributeOverride(name = "phoneticize", column = @Column(name = "phoneticize"))
    })
    @Setter(value = AccessLevel.PRIVATE)
    private PhoneticizeName name;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "sort", column = @Column(name = "sort"))
    })
    @Setter
    private Sort sort;

    @Column(name = "parent_id")
    private String parentId;

    // 描述
    @Setter
    private String description;

    /***********导航属性**************/
    //父分类
    @Setter(AccessLevel.PUBLIC)
    @Transient
    private transient Department parent;

    //所有直接孩子分类
    @Setter(AccessLevel.PUBLIC)
    @Transient
    private transient List<Department> children = new ArrayList<>();

    //所有祖先分类
    @Setter(AccessLevel.PUBLIC)
    @Transient
    private transient List<Department> ancestors = new ArrayList<>();

    //所有后代分类
    @Setter(AccessLevel.PUBLIC)
    @Transient
    private transient List<Department> descendants = new ArrayList<>();

    // for hibernate
    public Department() {

    }

    /**
     * 部门构造函数
     *
     * @param name     部门名称
     * @param parentId 父级部门
     */
    public Department(PhoneticizeName name, Sort sort, String parentId) {
        this(name, sort, parentId, null);
    }

    /**
     * 部门构造函数
     *
     * @param name     部门名称
     * @param parentId 父级部门
     */
    public Department(PhoneticizeName name, Sort sort, String parentId, String description) {
        this.name = name;
        this.sort = sort;
        this.parentId = parentId;
        this.description = description;
    }

    @Override
    public void validate(ValidationHandler handler) {

    }

    private DepartmentRepository repository() {
        return PebbleRegistry.departmentRepository();
    }

    /**
     * 是否为根部门
     *
     * @return true 表示当前部门是根部门，false反之
     */
    public boolean isRoot() {
        return Objects.isNull(parentId);
    }

    /**
     * 是否为叶子部门
     *
     * @return true 表示当前部门是否叶子部门
     */
    public boolean isLeaf() {
        return !this.repository().hasChildren(this.getId());
    }

    /**
     * 变更父级部门
     *
     * @param parentId 父部门ID
     */
    public void changeParent(String parentId) {
        this.assertArgumentNotEquals(this.getId(), parentId, "循环的部门关联");
        this.parentId = parentId;
        if (Objects.nonNull(this.getParent())) {
            this.loadParent();
        }
    }

    public void changeName(PhoneticizeName name) {
        this.assertArgumentNotNull(name, "部门名称不能为空");
        this.setName(name);
    }

    /**
     * 加载父节点
     *
     * @return 返回自身，非父级组织机构
     */
    public Department loadParent() {
        if (Objects.isNull(this.parentId)) {
            return this;
        } else {
            this.repository().ofId(this.getParentId()).ifPresent(this::setParent);
        }
        return this;
    }

    @Nullable
    public String parentName() {
        this.loadParent();
        if (Objects.isNull(this.parent)) {
            return null;
        }
        return this.parent.getName().getName();
    }

    /**
     * 加载直接孩子部门
     *
     * @return 返回自身
     */
    public Department loadChildren() {
        if (this.children.isEmpty()) {
            this.setChildren(this.repository().children(this.getId()));
        }
        return this;
    }
}
