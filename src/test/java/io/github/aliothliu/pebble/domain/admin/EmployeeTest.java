package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.pebble.domain.Cellphone;
import io.github.aliothliu.pebble.domain.PhoneticizeName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("h2")
class EmployeeTest {

    @Test
    void testLeave() {
        Employee employee = new Employee(
                new EmployeeId("9527"),
                new Username("abcd9527"),
                new PhoneticizeName("华安"),
                new IdCard("110101199003072711"),
                new Cellphone("13708854531"),
                WorkStatus.newEmploy());

        assertTrue(employee.isEmployed());

        employee.leave();

        assertFalse(employee.isEmployed());
    }

    @Test
    void newAccounts() {
        Employee employee = new Employee(
                new EmployeeId("9527"),
                new Username("abcd9527"),
                new PhoneticizeName("华安"),
                new IdCard("110101199003072711"),
                new Cellphone("13708854531"),
                WorkStatus.newEmploy());
        List<Account> accounts = employee.newAccounts("12345678");

        assertEquals(2, accounts.size());
        assertTrue(accounts.stream().anyMatch(account -> account.getIdentifier().equals("abcd9527")));
        assertTrue(accounts.stream().anyMatch(account -> account.getIdentifier().equals("13708854531")));
    }
}