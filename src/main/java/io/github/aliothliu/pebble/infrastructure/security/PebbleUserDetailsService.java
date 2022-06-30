package io.github.aliothliu.pebble.infrastructure.security;

import io.github.aliothliu.marble.Subject;
import io.github.aliothliu.marble.application.RbacUserSubject;
import io.github.aliothliu.marble.domain.role.RoleCode;
import io.github.aliothliu.pebble.domain.admin.Account;
import io.github.aliothliu.pebble.domain.admin.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PebbleUserDetailsService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.accountRepository
                .ofIdentifier(username)
                .filter(Account::getEnabled)
                .map(auth -> {
                    Subject subject = new RbacUserSubject(auth.getEmployeeId().getId());
                    return new org.springframework.security.core.userdetails.User(
                            auth.getEmployeeId().getId(),
                            auth.getCredential().getPassword(),
                            subject.loadRoles().stream().map(RoleCode::getCode).map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                    );
                }).orElseThrow(() -> new BadCredentialsException("用户名或密码错误"));
    }
}
