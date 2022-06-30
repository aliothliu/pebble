package io.github.aliothliu.pebble.infrastructure.persistence.admin;

import io.github.aliothliu.pebble.domain.admin.Account;
import io.github.aliothliu.pebble.domain.admin.AccountProvider;
import io.github.aliothliu.pebble.domain.admin.AccountRepository;
import io.github.aliothliu.pebble.domain.admin.EmployeeId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaAccountRepository authenticationRepository;

    @Override
    public boolean exists(String identifier) {
        return this.authenticationRepository.existsById(identifier);
    }

    @Override
    public Optional<Account> ofIdentifier(String identifier) {
        return this.authenticationRepository.findById(identifier);
    }

    @Override
    public void saveAll(List<Account> accounts) {
        this.authenticationRepository.saveAll(accounts);
    }

    @Override
    public void save(Account account) {
        this.authenticationRepository.save(account);
    }

    @Override
    public List<Account> ofEmployeeId(EmployeeId employeeId, AccountProvider provider) {
        return this.authenticationRepository.findByEmployeeIdAndProvider(employeeId, provider);
    }

    @Override
    public void deleteByIdentifier(String identifier) {
        this.authenticationRepository.deleteById(identifier);
    }
}
