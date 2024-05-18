package burak.service;

import burak.model.AppUser;
import burak.model.Friendship;
import burak.repository.FriendshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserService userService;

    public void newFriendshipRequest(Long otherUserId, String userName) {


        if (otherUserId == null) {
            throw new RuntimeException("Something went wrong!");
        } else if (otherUserId == userService.getUserInformationByUsername(userName).getId()) {
            throw new RuntimeException("You can't send a friend request to yourself!");
        }

        AppUser recieverUser = userService.getUserById(otherUserId);
        AppUser user = userService.getUserInformationByUsername(userName);

        if (recieverUser == null) {
            throw new RuntimeException("Something went wrong!");
        } else {

            Friendship friendship = new Friendship();
            friendship.setFriendRequester(user);
            friendship.setFriendReceiver(recieverUser);
            friendship.setAccepted(false);
            friendshipRepository.save(friendship);
        }
    }

    public void acceptFriendshipRequest(Long otherUserId, String userName) {

        AppUser user = userService.getUserInformationByUsername(userName);
        Friendship friendship = friendshipRepository.findByFriendRequesterIdAndFriendReceiverId(otherUserId, user.getId());

        if (friendship != null) {
            throw new RuntimeException("Something went wrong!");
        } else {
            friendship.setAccepted(true);
            //set date now
            friendship.setDate(new Date());
            friendshipRepository.save(friendship);
        }
    }

    public void deleteFriendship(Long otherUserId, String userName) {

        AppUser user = userService.getUserInformationByUsername(userName);

        Friendship friendship = friendshipRepository.findByFriendRequesterIdAndFriendReceiverId(user.getId(), otherUserId);

        if (friendship == null) {
            friendship = friendshipRepository.findByFriendRequesterIdAndFriendReceiverId(otherUserId, user.getId());
        }

        if (friendship != null) {
            throw new RuntimeException("Something went wrong!");
        } else {
            friendshipRepository.delete(friendship);
        }
    }

    public Set<Friendship> getFriendships(String userName) {

        AppUser user = userService.getUserInformationByUsername(userName);

        return friendshipRepository.findAllByFriendRequesterIdAndAcceptedIsTrueOrFriendReceiverIdAndAcceptedIsTrue(user.getId(), user.getId());
    }

    public Set<Friendship> getMyFriendRequests(String userName) {

        AppUser user = userService.getUserInformationByUsername(userName);

        return friendshipRepository.findAllByFriendReceiverId(user.getId());
    }


    public void removeFriendRequest(Long otherUserId, String userName) {

        AppUser user = userService.getUserInformationByUsername(userName);

        Friendship friendship = friendshipRepository.findByFriendRequesterIdAndFriendReceiverIdAndAcceptedIsFalse(otherUserId, user.getId());

        if (friendship == null ) {
            friendship = friendshipRepository.findByFriendRequesterIdAndFriendReceiverIdAndAcceptedIsFalse(user.getId(), otherUserId );
        }

        if (friendship == null || friendship.isAccepted()) {
            throw new RuntimeException("You don't have a friend request from this user!");
        } else {
            friendshipRepository.delete(friendship);
        }
    }


}
