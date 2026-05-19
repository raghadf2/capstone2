package com.example.spaceflow.Service;

import com.example.spaceflow.Api.ApiException;
import com.example.spaceflow.Model.Booking;
import com.example.spaceflow.Model.Review;
import com.example.spaceflow.Model.User;
import com.example.spaceflow.Repository.BookingRepository;
import com.example.spaceflow.Repository.ReviewRepository;
import com.example.spaceflow.Repository.SpaceRepository;
import com.example.spaceflow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final SpaceRepository spaceRepository;
    private final UserService userService;
    private final BookingRepository bookingRepository;

    //CRUD

    public void addReview(Review review){
        userService.checkUserRole(review.getUserId(), "USER");

        if(spaceRepository.findSpaceById(review.getSpaceId()) == null){
            throw new ApiException("Space Not Found");
        }

        if(reviewRepository.findReviewByUserIdAndSpaceId(review.getUserId(), review.getSpaceId()) != null){
            throw new ApiException("You already reviewed this space");
        }

        List<Booking> bookings = bookingRepository.findBookingByUserId(review.getUserId());

        boolean hasCompletedBooking = false;

        for (Booking b : bookings) {
            if (b.getSpaceId().equals(review.getSpaceId()) && b.getStatus().equals("CONFIRMED") && b.getBookingDate().isBefore(LocalDate.now())) {
                hasCompletedBooking = true;
                break;
            }
        }

        if (!hasCompletedBooking) {
            throw new ApiException("You can only review after completing a booking");
        }
        reviewRepository.save(review);
    }

    public List<Review> getReviews(){
        return reviewRepository.findAll();
    }

    public void updateReview(Integer id, Review review, Integer userId){

        userService.checkUserRole(userId, "USER");

        Review oldReview = reviewRepository.findReviewById(id);

        if(oldReview == null){
            throw new ApiException("Review Not found");
        }

        if(!oldReview.getUserId().equals(userId)){
            throw new ApiException("Not allowed to update this review");
        }

        if(!oldReview.getSpaceId().equals(review.getSpaceId())){

            if(reviewRepository.findReviewByUserIdAndSpaceId(
                    userId,review.getSpaceId()) != null){
                throw new ApiException("You already reviewed this space");
            }

            oldReview.setSpaceId(review.getSpaceId());
        }

        oldReview.setRating(review.getRating());
        oldReview.setComment(review.getComment());

        reviewRepository.save(oldReview);
    }

    public void deleteReview(Integer id,Integer userId){
        userService.checkUserRole(userId, "USER");

        Review review = reviewRepository.findReviewById(id);

        if(review == null){
            throw new ApiException("Review Not found");
        }

        if(!review.getUserId().equals(userId)){
            throw new ApiException("Not allowed");
        }

        reviewRepository.delete(review);
    }

}
