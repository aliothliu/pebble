package io.github.aliothliu.pebble.infrastructure.persistence.admin;

import io.github.aliothliu.pebble.domain.admin.Account;
import io.github.aliothliu.pebble.domain.admin.AccountProvider;
import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaAccountRepository extends CrudRepository<Account, String> {

    List<Account> findByEmployeeIdAndProvider(EmployeeId employeeId, AccountProvider provider);
}
