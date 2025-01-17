package renatoguii.imageliteapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import renatoguii.imageliteapi.entities.user.UserEntity;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByEmail(String email);
}
