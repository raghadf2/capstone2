package com.example.spaceflow.Controller;

import com.example.spaceflow.Api.ApiResponse;
import com.example.spaceflow.Model.Booking;
import com.example.spaceflow.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/add")
    public ResponseEntity<?> addBooking(@RequestBody @Valid Booking booking) {
        bookingService.addBooking(booking);
        return ResponseEntity.status(200).body(new ApiResponse("Booking Added"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getBooking() {
        return ResponseEntity.status(200).body(bookingService.getBooking());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Integer id, @RequestBody @Valid Booking booking) {
        bookingService.updateBooking(id, booking);
        return ResponseEntity.status(200).body(new ApiResponse("Booking Updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Integer id, @RequestParam Integer userId) {
        bookingService.deleteBooking(id, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Booking Deleted"));
    }

    @PostMapping("/check-conflict")
    public ResponseEntity<?> preventDoubleBooking(@RequestBody @Valid Booking booking, @RequestParam(required = false) Integer bookingId) {
        bookingService.preventDoubleBooking(booking, bookingId);
        return ResponseEntity.status(200).body(new ApiResponse("No conflict detected"));
    }

    @PostMapping("/recurring")
    public ResponseEntity<?> createRecurringBooking(@RequestBody @Valid Booking booking, @RequestParam int repeatCount) {
        bookingService.createRecurringBooking(booking, repeatCount);
        return ResponseEntity.status(200).body(new ApiResponse("Recurring bookings created"));
    }

    @PutMapping("/reschedule/{id}")
    public ResponseEntity<?> rescheduleBooking(@PathVariable Integer id, @RequestParam LocalDate date, @RequestParam LocalTime start, @RequestParam LocalTime end) {
        bookingService.rescheduleBooking(id, date, start, end);
        return ResponseEntity.status(200).body(new ApiResponse("Booking rescheduled"));
    }

    @DeleteMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.status(200).body(new ApiResponse("Booking cancelled"));
    }

    @GetMapping("/user-booking/{userId}")
    public ResponseEntity<?> getUserBookings(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(bookingService.getUserBookings(userId));
    }

    @GetMapping("/estimate-price")
    public ResponseEntity<?> estimatePrice(@RequestParam Integer spaceId, @RequestParam LocalTime start, @RequestParam LocalTime end) {
        return ResponseEntity.ok(bookingService.estimatePrice(spaceId, start, end));
    }

    @PostMapping("/notify-booking/{bookingId}")
    public ResponseEntity<?> notifyBooking(@PathVariable Integer bookingId) {
        bookingService.notifyBooking(bookingId);
        return ResponseEntity.ok(new ApiResponse("Notification sent"));
    }

    @PostMapping("/reminder/{bookingId}")
    public ResponseEntity<?> reminder(@PathVariable Integer bookingId) {
        bookingService.sendReminder(bookingId);
        return ResponseEntity.ok(new ApiResponse("Reminder checked"));
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getBookingSummary(@RequestParam Integer spaceId, @RequestParam LocalTime start, @RequestParam LocalTime end) {
        return ResponseEntity.ok(bookingService.getBookingSummary(spaceId, start, end));
    }

    @PostMapping("/payment/{bookingId}")
    public ResponseEntity<?> confirmBookingPayment(@PathVariable Integer bookingId) {
        return ResponseEntity.ok(bookingService.confirmBookingPayment(bookingId));
    }
}

