package com.TicketXChange.TicketXChange.auth.repository;

import com.TicketXChange.TicketXChange.auth.model.DeactivateAndDelete;
import com.TicketXChange.TicketXChange.auth.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeactivateAndDeleteRepository extends JpaRepository<DeactivateAndDelete, Integer> {

    Optional<DeactivateAndDelete> findByUser(UserProfile user);

    List<DeactivateAndDelete> findByDeletionPending(Boolean deletionPending);

    List<UserProfile> findByIsDeactivated(boolean b);
}
