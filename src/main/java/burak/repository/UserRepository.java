package burak.repository;

import burak.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsername(String username);

    AppUser findByUsername(String username);

    AppUser findByEmail(String username);

    boolean existsByEmail(String email);

    @Transactional
    void deleteByUsername(String username);

}
