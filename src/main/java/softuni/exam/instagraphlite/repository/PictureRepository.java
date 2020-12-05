package softuni.exam.instagraphlite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.instagraphlite.models.entites.Picture;

import java.util.*;


@Repository
public interface PictureRepository extends JpaRepository<Picture,Integer> {

    Optional<Picture> findByPath(String path);

    @Query("select p from Picture  p where p.size > 30000 " +
            "order by p.size")
    List<Picture> exportPicturesWuthSizeBiggerThan30000();
}
