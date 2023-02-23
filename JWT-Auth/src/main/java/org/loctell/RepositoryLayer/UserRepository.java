package org.loctell.RepositoryLayer;

import java.util.Optional;

import org.loctell.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);


  @Query("SELECT u FROM User u WHERE u.email = :email AND u.password = :password")
  public User findbyUsernameandPassword(@RequestParam("email") String email, @RequestParam("password") String password);

}
