package com.example.spaceflow.Repository;

import com.example.spaceflow.Model.Favorite;
import com.example.spaceflow.Model.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Integer> {
    Favorite findFavoriteById(Integer id);
    Favorite findFavoriteByUserIdAndSpaceId(Integer userId,Integer spaceId);

    @Query("select s from Space s where s.id in (select f.spaceId from Favorite f where f.userId = :userId)")
    List<Space> getUserFavorites(@Param("userId") Integer userId);
}
