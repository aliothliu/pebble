package io.github.aliothliu.pebble.domain.admin;

import io.github.aliothliu.marble.domain.AssertionException;
import io.github.aliothliu.marble.domain.FailFastValidationHandler;
import io.github.aliothliu.pebble.infrastructure.errors.NonUniquenessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("h2")
class AccountTest {

    @MockBean
    private AccountRepository repository;

    @Test
    void validate() {
        Mockito.when(repository.exists("13708854343")).thenReturn(true);

        Account fail = new Account("13708854343", new EmployeeId("9527"), new Password("123456"), AccountProvider.Local);

        Exception exception = assertThrows(NonUniquenessException.class, () -> fail.validate(new FailFastValidationHandler()));
        assertTrue(exception.getMessage().contains("该登录标识已被绑定[13708854343]"));

        Account success = new Account("13708854344", new EmployeeId("9527"), new Password("123456"), AccountProvider.Local);

        success.validate(new FailFastValidationHandler());
    }

    @Test
    void newIdentifier() {
        Account stub = new Account("13708854343", new EmployeeId("9527"), new Password("123456"), AccountProvider.Local);

        Account account = stub.newAccount("9527");

        assertEquals("9527", account.getIdentifier());
        assertEquals(AccountProvider.Local, account.getProvider());
        assertEquals(new EmployeeId("9527"), account.getEmployeeId());
        assertEquals("9527", account.getEmployeeId().getId());
    }

    @Test
    void changePassword() {
        Account account = new Account("13708854343", new EmployeeId("9527"), new Password("123456"), AccountProvider.Local);

        Exception exception = assertThrows(AssertionException.class, () -> account.changePassword(null, "1123"));
        assertTrue(exception.getMessage().contains("登录密码不能为空"));

        exception = assertThrows(AssertionException.class, () -> account.changePassword("123", null));
        assertTrue(exception.getMessage().contains("登录密码不能为空"));

        exception = assertThrows(AssertionException.class, () -> account.changePassword("123", "12345678"));
        assertTrue(exception.getMessage().contains("原密码不正确"));

        account.changePassword("123456", "12345678");

        assertTrue(account.getCredential().matches("12345678"));
    }

    @Test
    void resetPassword() {
        Account account = new Account("13708854343", new EmployeeId("9527"), new Password("123456"), AccountProvider.Local);

        account.resetPassword("12345678");

        assertTrue(account.getCredential().matches("12345678"));
    }
}