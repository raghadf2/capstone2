package com.example.spaceflow.Service;

import com.example.spaceflow.Api.ApiException;
import com.example.spaceflow.DTO.PaymentResponse;
import com.example.spaceflow.Model.Booking;
import com.example.spaceflow.Repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BookingRepository bookingRepository;

    public PaymentResponse pay(Integer bookingId){

        Booking booking = bookingRepository.findBookingById(bookingId);

        if(booking == null){
            throw new ApiException("Booking not found");
        }

        if(booking.getStatus().equals("CANCELLED")){
            throw new ApiException("Cannot pay for cancelled booking");
        }

        if(booking.getStatus().equals("CONFIRMED")){
            throw new ApiException("Booking already paid");
        }

        Double amount = booking.getTotalPrice();

        if(amount == null || amount <= 0){
            throw new ApiException("Invalid payment amount");
        }

        boolean success = processPayment(amount);

        if(!success){
            throw new ApiException("Payment failed");
        }

        booking.setStatus("CONFIRMED");
        bookingRepository.save(booking);

        return new PaymentResponse(bookingId, amount, "CONFIRMED", "PAYMENT SUCCESS");
    }

    private boolean processPayment(Double amount){
        return amount > 0;
    }
}
