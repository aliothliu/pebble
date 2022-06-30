package io.github.aliothliu.pebble.domain.admin;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    boolean exists(String identifier);

    Optional<Account> ofIdentifier(String identifier);

    void saveAll(List<Account> accounts);

    void save(Account account);

    List<Account> ofEmployeeId(EmployeeId employeeId, AccountProvider provider);

    void deleteByIdentifier(String identifier);
}
