package in.mpapp.springsecurityjwtdemo.repositories;

import in.mpapp.springsecurityjwtdemo.entities.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends IDedRepository<UserEntity> {

    Optional<UserEntity> findByUsername(final String username);
}
