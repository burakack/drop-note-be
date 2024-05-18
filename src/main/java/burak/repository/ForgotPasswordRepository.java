package burak.repository;

import burak.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepository extends JpaRepository<AppUser, Long> {
    AppUser findByEmail(String email);

    AppUser findByToken(String token);
}