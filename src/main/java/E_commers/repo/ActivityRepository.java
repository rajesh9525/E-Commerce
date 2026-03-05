package E_commers.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import E_commers.model.UserActivity;

@Repository
public interface ActivityRepository extends JpaRepository<UserActivity, Long> {
}

