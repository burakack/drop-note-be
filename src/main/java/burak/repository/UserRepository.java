package burak.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import burak.model.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    boolean existsByUsername(String username);

    AppUser findByUsername(String username);

    AppUser findByEmail(String username);

    boolean existsByEmail(String email);

    @Transactional
    void deleteByUsername(String username);

}
