package burak.service;

import javax.servlet.http.HttpServletRequest;

import burak.dto.UserUpdateDto;
import burak.repository.ForgotPasswordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import burak.exception.CustomException;
import burak.model.AppUser;
import burak.repository.UserRepository;
import burak.security.JwtTokenProvider;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private static final long EXPIRE_TOKEN = 30;

    public String signin(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.BAD_REQUEST);
        }
    }

    public String signup(AppUser appUser) {
        if (!userRepository.existsByUsername(appUser.getUsername())) {
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            userRepository.save(appUser);
            return jwtTokenProvider.createToken(appUser.getUsername(), appUser.getAppUserRoles());
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }

    public AppUser search(String username) {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return appUser;
    }

    public AppUser whoami(HttpServletRequest req) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)));
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getAppUserRoles());
    }

    public AppUser editUserInformation(UserUpdateDto appUser) {
        if (userRepository.existsByUsername(appUser.getUsername())) {
            //token içerisindeki username ile gelen username aynı mı kontrol et

            AppUser appUserFromDatabase = userRepository.findByUsername(appUser.getUsername());

            appUserFromDatabase.setAge(appUser.getAge());
            appUserFromDatabase.setDescription(appUser.getDescription());

            userRepository.save(appUserFromDatabase);

            //return etmeden önce parola bilgisini gizle
            appUserFromDatabase.setPassword("********");
            return appUserFromDatabase;

        } else {
            throw new CustomException("Username not found in database", HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    public String forgotPassword(String email) {
        if (userRepository.existsByEmail(email)) {
            return "Password reset link sent to your email address";
        } else {
            throw new CustomException("Email not found in database", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public AppUser getUserInformationByUsername(String username) {
        if (userRepository.existsByUsername(username)) {
            return userRepository.findByUsername(username);

        } else {
            throw new CustomException("Username not found in database", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public AppUser getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    public AppUser findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public String forgotPass(String email) {
        Optional<AppUser> userOptional = Optional.ofNullable(forgotPasswordRepository.findByEmail(email));

        if (!userOptional.isPresent()) {
            return "Invalid email id.";
        }

        AppUser user = userOptional.get();
        user.setToken(generateToken());
        user.setTokenCreationDate(LocalDateTime.now());

        user = forgotPasswordRepository.save(user);
        return user.getToken();
    }

    public String resetPass(String token, String password) {
        Optional<AppUser> userOptional = Optional.ofNullable(forgotPasswordRepository.findByToken(token));

        if (!userOptional.isPresent()) {
            return "Invalid token";
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Token expired.";

        }

        AppUser user = userOptional.get();

        user.setPassword(password);
        user.setToken(null);
        user.setTokenCreationDate(null);

        forgotPasswordRepository.save(user);

        return "Your password successfully updated.";
    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN;
    }


    public void uploadUserImage(UserUpdateDto user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            //token içerisindeki username ile gelen username aynı mı kontrol et

            AppUser appUserFromDatabase = userRepository.findByUsername(user.getUsername());

            appUserFromDatabase.setUserImage(user.getUserImage());

            userRepository.save(appUserFromDatabase);


        } else {
            throw new CustomException("Username not found in database", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
