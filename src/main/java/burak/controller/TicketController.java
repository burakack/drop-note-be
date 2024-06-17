package burak.controller;

import burak.dto.FriendshipRequestDto;
import burak.dto.TicketDto;
import burak.model.AppUser;
import burak.model.Friendship;
import burak.model.Ticket;
import burak.service.FriendshipService;
import burak.service.TicketService;
import burak.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping("/ticket")
@Api(tags = "ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final UserService userService;


    @PostMapping("new-ticket")
    @ApiOperation(value = "${TicketController.newFriendship}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void newTicket(
            @ApiParam("TicketDto") @RequestBody TicketDto ticketDto, HttpServletRequest req) {

        AppUser user = userService.search(req.getRemoteUser());
        ticketService.createTicket(ticketDto,user);
    }

    @GetMapping("get-my-tickets")
    @ApiOperation(value = "${TicketController.getMyTickets}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public Set<Ticket> getMyTickets(HttpServletRequest req) {
        AppUser user = userService.search(req.getRemoteUser());
        return ticketService.getTicketsByUser(user);

    }

}
