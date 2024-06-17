package burak.service;

import burak.dto.TicketDto;
import burak.model.AppUser;
import burak.model.Ticket;
import burak.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    public void createTicket(TicketDto ticketDto, AppUser user) {
        Ticket ticket = new Ticket();
        ticket.setContent(ticketDto.getContent());
        ticket.setTitle(ticketDto.getTitle());
        ticket.setUser(user);
        ticket.setStatus(false);
        ticket.setEmail(ticketDto.getEmail());
        ticketRepository.save(ticket);
    }

    public Set<Ticket> getTicketsByUser (AppUser user) {
        return ticketRepository.findAllByUser(user);
    }

}
