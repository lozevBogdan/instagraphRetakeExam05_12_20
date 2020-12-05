package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entites.Picture;
import softuni.exam.instagraphlite.models.entites.User;

import java.util.*;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);

    @Query("select u from User u order by u.posts.size desc,u.id")
    List<User> getUsersWithTheirPosts();
}
