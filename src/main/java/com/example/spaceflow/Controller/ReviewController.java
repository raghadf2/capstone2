package com.example.spaceflow.Controller;

import com.example.spaceflow.Api.ApiResponse;
import com.example.spaceflow.Model.Review;
import com.example.spaceflow.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody @Valid Review review){
        reviewService.addReview(review);
        return ResponseEntity.status(200).body(new ApiResponse("Review Added"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getReviews(){
        return ResponseEntity.status(200).body(reviewService.getReviews());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id,@RequestBody @Valid Review review, @RequestParam Integer userId){
        reviewService.updateReview(id,review,userId);
        return ResponseEntity.status(200).body(new ApiResponse("Review Updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id, @RequestParam Integer userId){
        reviewService.deleteReview(id,userId);
        return ResponseEntity.status(200).body(new ApiResponse("Review Deleted"));
    }
}
