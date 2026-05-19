package com.example.spaceflow.Repository;

import com.example.spaceflow.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Integer> {
    Review findReviewById(Integer id);
    Review findReviewByUserIdAndSpaceId(Integer userId,Integer spaceId);
}
