package burak.repository;


import burak.model.AppUser;
import burak.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Set<Ticket> findAllByUser(AppUser user);

}
