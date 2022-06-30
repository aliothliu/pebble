package io.github.aliothliu.pebble.application.admin;

import io.github.aliothliu.marble.domain.FailFastValidationHandler;
import io.github.aliothliu.marble.infrastructure.errors.NonMatchEntityException;
import io.github.aliothliu.pebble.application.admin.command.ChangeEmployeeCommand;
import io.github.aliothliu.pebble.application.admin.command.ChangePasswordCommand;
import io.github.aliothliu.pebble.application.admin.command.NewEmployeeCommand;
import io.github.aliothliu.pebble.application.admin.command.TransferEmployeeCommand;
import io.github.aliothliu.pebble.domain.Cellphone;
import io.github.aliothliu.pebble.domain.PhoneticizeName;
import io.github.aliothliu.pebble.domain.admin.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class EmployeeApplicationService {

    private final EmployeeRepository repository;

    private final EmployeeTransferRepository transferRepository;

    private final AccountRepository accountRepository;

    @Transactional
    public String newEmployee(@Valid NewEmployeeCommand command) {
        Employee employee = new Employee(
                new EmployeeId(repository.nextEmployeeId()),
                new Username(command.getUsername()),
                new PhoneticizeName(command.getName()),
                new IdCard(command.getIdCard()),
                new Cellphone(command.getCellphone()),
                WorkStatus.newEmploy()
        );

        EmployeeTransfer transfer = employee.joinIn(command.getDepartmentId());
        List<Account> accounts = employee.newAccounts(command.getPassword());

        employee.validate(new FailFastValidationHandler());
        transfer.validate(new FailFastValidationHandler());
        accounts.forEach(a -> a.validate(new FailFastValidationHandler()));

        this.transferRepository.save(transfer);
        this.accountRepository.saveAll(accounts);
        this.repository.save(employee);
        return employee.getEmployeeId().getId();
    }

    public void changePassword(@NotNull EmployeeId employeeId, ChangePasswordCommand command) {
        this.accountRepository.saveAll(
                this.accountRepository
                        .ofEmployeeId(employeeId, AccountProvider.Local)
                        .stream()
                        .peek(a -> a.changePassword(command.getCurrentPassword(), command.getChangedPassword()))
                        .collect(Collectors.toList())
        );
    }

    public void transfer(@NotNull EmployeeId employeeId, @Valid TransferEmployeeCommand command) {
        Employee employee = this.getOne(employeeId);
        EmployeeTransfer transfer = employee.transfer(command.getDepartmentId());
        transfer.validate(new FailFastValidationHandler());

        this.transferRepository.save(transfer);
    }

    @Transactional(rollbackFor = Exception.class)
    public void leave(@NotNull EmployeeId id) {
        Employee employee = this.getOne(id);
        employee.leave();

        List<Account> disabled = this.accountRepository
                .ofEmployeeId(id, AccountProvider.Local)
                .stream()
                .peek(Account::disabled)
                .collect(Collectors.toList());

        this.accountRepository.saveAll(disabled);
        this.repository.save(employee);
    }

    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(@NotNull EmployeeId id) {
        Employee employee = this.getOne(id);

        List<Account> resetPasswordAuthList = this.accountRepository
                .ofEmployeeId(id, AccountProvider.Local)
                .stream()
                .peek(auth -> auth.resetPassword(employee.getCellphone().getNumber()))
                .collect(Collectors.toList());

        this.accountRepository.saveAll(resetPasswordAuthList);
    }

    @Transactional(rollbackFor = Exception.class)
    public void changeEmployee(@NotNull EmployeeId employeeId, @Valid ChangeEmployeeCommand command) {
        Employee employee = this.getOne(employeeId);
        Cellphone expiredCellphone = employee.getCellphone();

        employee.changeName(new PhoneticizeName(command.getName()));
        employee.changePhone(new Cellphone(command.getCellphone()));
        employee.changeIdCard(command.getIdCard());

        employee.validate(new FailFastValidationHandler());

        Account expiredAccount = this.accountRepository.ofIdentifier(expiredCellphone.getNumber()).orElseThrow(() -> new NonMatchEntityException("找到符合条件的账户数据"));
        Account newAccount = expiredAccount.newAccount(command.getCellphone());

        this.accountRepository.deleteByIdentifier(expiredCellphone.getNumber());
        this.accountRepository.save(newAccount);
        this.repository.save(employee);
    }

    private Employee getOne(EmployeeId id) {
        return this.repository.ofId(id).orElseThrow(() -> new NonMatchEntityException("查询失败：未找到符合条件的用户数据"));
    }
}