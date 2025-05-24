package com.mk.usermanagement.service;

import com.mk.usermanagement.dto.*;
import com.mk.usermanagement.entity.Role;
import com.mk.usermanagement.entity.RoleName;
import com.mk.usermanagement.entity.User;
import com.mk.usermanagement.exception.UsernameAlreadyExistsException;
import com.mk.usermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void register(UserRegisterRequest registerRequest) {

        // sprawdź czy uzytkownik z takim username albo email już istnieje,
        // jeśli tak - wyrzuć wyjatęk
        if (userRepository.existsByUsername(registerRequest.username()) || userRepository.existsByEmail(registerRequest.email())) {
            throw new UsernameAlreadyExistsException("User already exists");
        }

        // przypisz nowemu użytkownikowi domyślną rolę - ROLE_USER
        Role role = roleService.findByName(String.valueOf(RoleName.ROLE_USER));

        // stwórz nowy obiekt użytkownika
        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        // BCrypt przekazanego hasła
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setEnabled(true);
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }

    @Override
    public AuthResponseDto login(UserLoginRequest loginRequest) {

        // tworzenie obiektu uwierzytelniającego na podstawie przekazanego username oraz password na endpoincie /login
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()));

        // zapisz uwierzytelnionego usera do kontekstu Springa
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // wygeneruj token na podstawie usera w authentication
        String token = jwtService.generateToken(authentication);
        // stwórz usera na podstawie głownego uzytkownika
        var user = (UserDetails) authentication.getPrincipal();

        // wyciągnij przypisane role do użytkownika
        Set<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // zwróć DTO z tokenem, username oraz rolami
        return new AuthResponseDto(token, user.getUsername(), roles);
    }

    @Override
    @Transactional
    public void createUserByAdmin(CreateUserRequest request) {
        // sprawdź czy w bazie istnieje już user z przekazanym username lub email
        if (userRepository.existsByUsername(request.username()) || userRepository.existsByEmail(request.email())) {
            throw new UsernameAlreadyExistsException("Username or email is already taken");
        }

        // wyciągnij przekazane role (upewniając się że takie role istnieją w bazie) i zapisz je do zbioru roles
        Set<Role> roles = request.roles().stream()
                .map(roleName -> roleService.findByName(roleName.toUpperCase()))
                .collect(Collectors.toSet());

        // stwórz nowego użytkownika na podstawie przekazanych danych z requestu
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRoles(roles);
        user.setEnabled(true);

        // zapisz nowego użytkownika
        userRepository.save(user);
    }

    @Override
    public UserDto findById(Long id) {

        // wyszukaj usera w bazie na podstawie przekazanego id
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        // wyciągnij jego role
        Set<String> roles = dbUser.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        // zwróć tylko te dane które naprawdę potrzeba w formie Dto
        return new UserDto(
                dbUser.getId(),
                dbUser.getUsername(),
                dbUser.getEmail(),
                roles
        );
    }

    @Override
    public List<UserDto> findAll() {

        // ponieważ nie chcę zwracać całej encji User, wyciągam z niej tylko id, username, email oraz role i zwracam jako listę UserDto
        return userRepository.findAll().stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRoles().stream()
                                .map(Role::getName)
                                .collect(Collectors.toSet())
                ))
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        // sprawdź czy user o zadanym id istnieje w bazie
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        // sprawdź czy przypadkiem zalogowany użytkownik nie chce usunąć samego siebie - nie pozwalam na to
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        if (loggedUser.equals(dbUser.getUsername())) {
            throw new IllegalArgumentException("Cannot delete your own account");
        }

        // usuń użytkownika z bazy
        userRepository.delete(dbUser);
    }

    @Override
    @Transactional
    public void update(Long id, UpdateUserRequest request) {

        // sprawdź czy user o zadanym id istnieje w bazie
        User dbUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + request.username()));

        // przypisz znalezionemu uzytkownikowi nowe dane
        dbUser.setUsername(request.username());
        dbUser.setEmail(request.email());

        if (request.roles() != null && !request.roles().isEmpty()) {
            Set<Role> roles = request.roles().stream()
                    .map(roleService::findByName)
                    .collect(Collectors.toSet());
            dbUser.setRoles(roles);
        }

        // zapisz zaktualizwanego użytkownika do bazy
        userRepository.save(dbUser);
    }

    @Override
    public UserDto findByUsername(String username) {

        // sprawdź czy user o zadanym username istnieje w bazie
        User dbUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " was not found."));

        // zwróć tylko UserDto
        return new UserDto(
                dbUser.getId(),
                dbUser.getUsername(),
                dbUser.getEmail(),
                dbUser.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet())
        );
    }
}
