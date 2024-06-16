package burak.repository;

import burak.model.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {


    Friendship findByFriendRequesterIdAndFriendReceiverId(Long requesterId, Long requestedId);

    Friendship findByFriendRequesterIdAndFriendReceiverIdAndAcceptedIsFalse(Long requesterId, Long requestedId);


    Set<Friendship> findAllByFriendRequesterIdAndAcceptedIsTrueOrFriendReceiverIdAndAcceptedIsTrue(Long id, Long id2);


    Set<Friendship> findAllByFriendReceiverId(Long id);
}
