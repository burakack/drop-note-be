package burak.controller;

import burak.dto.FriendshipRequestDto;
import burak.model.Friendship;
import burak.service.FriendshipService;
import burak.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

@RestController
@RequestMapping("/friendship")
@Api(tags = "friendship")
@RequiredArgsConstructor
public class FriendshipController {
    private final FriendshipService friendshipService;
    private final UserService userService;


    @PostMapping("new-friendship-request")
    @ApiOperation(value = "${FriendshipController.newFriendship}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void newFriendship(
            @ApiParam("FriendshipDto") @RequestBody FriendshipRequestDto friendshipDto, HttpServletRequest req) {
        friendshipService.newFriendshipRequest(friendshipDto.getOtherUserId(), req.getRemoteUser());
    }

    @PostMapping("accept-friendship-request")
    @ApiOperation(value = "${FriendshipController.acceptFriendship}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void acceptFriendship(
            @ApiParam("FriendshipDto") @RequestBody FriendshipRequestDto friendshipDto, HttpServletRequest req) {
        friendshipService.acceptFriendshipRequest(friendshipDto.getOtherUserId(), req.getRemoteUser());
    }

    @PostMapping("remove-friendship")
    @ApiOperation(value = "${FriendshipController.deleteFriendship}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void deleteFriendship(
            @ApiParam("FriendshipDto") @RequestBody FriendshipRequestDto friendshipDto, HttpServletRequest req) {
        friendshipService.deleteFriendship(friendshipDto.getOtherUserId(), req.getRemoteUser());
    }

    @GetMapping("my-friendships")
    @ApiOperation(value = "${FriendshipController.myFriendships}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public Set<Friendship> myFriendships(HttpServletRequest req) {

        return friendshipService.getFriendships(req.getRemoteUser());
    }

    @GetMapping("my-friend-requests")
    @ApiOperation(value = "${FriendshipController.myFriendRequests}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public Set<Friendship> myFriendRequests(HttpServletRequest req) {
        return friendshipService.getMyFriendRequests(req.getRemoteUser());
    }

    @PostMapping("remove-friend-request")
    @ApiOperation(value = "${FriendshipController.removeFriendRequest}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public void removeFriendRequest(
            @ApiParam("FriendshipDto") @RequestBody FriendshipRequestDto friendshipDto, HttpServletRequest req) {
        friendshipService.removeFriendRequest(friendshipDto.getOtherUserId(), req.getRemoteUser());
    }

    @GetMapping("user-friendships/{username}")
    @ApiOperation(value = "${FriendshipController.userFriendships}")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 403, message = "Access denied")})
    @PreAuthorize("hasRole('ROLE_USER')")
    public Set<Friendship> userFriendships(@PathVariable String username) {
        return friendshipService.getFriendships(username);
    }
}
