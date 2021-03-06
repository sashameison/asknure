package asknure.narozhnyi.core.repository;

import java.util.Optional;

import asknure.narozhnyi.core.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
  Optional<User> findUserById(String userId);

  Optional<User> findUserByEmail(String email);

  Optional<User> findUserByUsername(String username);

  Page<User> findAllBy(Pageable pageable);

  void deleteUsersById(String id);

}
