package io.github.aliothliu.pebble;

import io.github.aliothliu.pebble.domain.admin.*;
import io.github.aliothliu.pebble.infrastructure.fm.FileManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class PebbleRegistry implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static DepartmentRepository departmentRepository() {
        return applicationContext.getBean(DepartmentRepository.class);
    }

    public static EmployeeRepository employeeRepository() {
        return applicationContext.getBean(EmployeeRepository.class);
    }

    public static AccountRepository accountRepository() {
        return applicationContext.getBean(AccountRepository.class);
    }

    public static EmployeeTransferRepository transferRepository() {
        return applicationContext.getBean(EmployeeTransferRepository.class);
    }

    public static PasswordService passwordService() {
        return applicationContext.getBean(PasswordService.class);
    }

    public static FileManager fileManager() {
        return applicationContext.getBean(FileManager.class);
    }

    public static PebbleProperties properties() {
        return applicationContext.getBean(PebbleProperties.class);
    }

    /**
     * 通过class获取Repository
     *
     * @param clazz Repository约束的Class
     * @return Repository实例
     */
    public static <T> T repository(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        PebbleRegistry.applicationContext = applicationContext;
    }
}
