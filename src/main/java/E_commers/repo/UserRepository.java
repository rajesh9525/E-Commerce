package E_commers.repo;

import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import E_commers.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByLogintype(String logintype);

	@Nullable
	Object findById(String string);

}

