package com.example.spaceflow.Service;

import com.example.spaceflow.Api.ApiException;
import com.example.spaceflow.DTO.BookingSummary;
import com.example.spaceflow.DTO.PaymentResponse;
import com.example.spaceflow.Model.Booking;
import com.example.spaceflow.Model.Space;
import com.example.spaceflow.Model.User;
import com.example.spaceflow.Repository.BookingRepository;
import com.example.spaceflow.Repository.SpaceRepository;
import com.example.spaceflow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final SpaceRepository spaceRepository;
    private final EmailService emailService;
    private final WhatsAppService whatsAppService;
    private final UserService userService;
    private final PaymentService paymentService;


    //CRUD
    public void addBooking(Booking booking){
        User user = userService.checkUserRole(booking.getUserId(), "USER");

        Space space = spaceRepository.findSpaceById(booking.getSpaceId());

        if(space == null){
            throw new ApiException("Space Not Found");
        }

        if(booking.getEndTime().isBefore(booking.getStartTime())){
            throw new ApiException("End time must be after start time");
        }

        Double price = estimatePrice(booking.getSpaceId(), booking.getStartTime(), booking.getEndTime());

        booking.setTotalPrice(price);

        preventDoubleBooking(booking, null);

        if(booking.getBookingDate().isBefore(LocalDate.now())){
            throw new ApiException("Cannot book in the past");
        }

        bookingRepository.save(booking);

        whatsAppService.sendWhatsApp(user.getPhone(), "Your booking is confirmed");
    }

    public List<Booking> getBooking(){
        return bookingRepository.findAll();
    }

    public void updateBooking(Integer id, Booking booking){

        userService.checkUserRole(booking.getUserId(), "USER");

        Booking oldBooking = bookingRepository.findBookingById(id);

        if(oldBooking == null){
            throw new ApiException("Booking Not Found");
        }

        if(!oldBooking.getUserId().equals(booking.getUserId())){
            throw new ApiException("Not allowed to update this booking");
        }

        if (booking.getEndTime().isBefore(booking.getStartTime())) {
            throw new ApiException("End time must be after start time");
        }

        preventDoubleBooking(booking, id);

        Double price = estimatePrice(oldBooking.getSpaceId(), booking.getStartTime(), booking.getEndTime());

        oldBooking.setBookingDate(booking.getBookingDate());
        oldBooking.setStartTime(booking.getStartTime());
        oldBooking.setEndTime(booking.getEndTime());
        oldBooking.setTotalPrice(price);
        oldBooking.setStatus(booking.getStatus());

        bookingRepository.save(oldBooking);
    }

    public void deleteBooking(Integer id, Integer userId){
        userService.checkUserRole(userId, "USER");

        Booking booking = bookingRepository.findBookingById(id);

        if(booking == null){
            throw new ApiException("Booking Not Exists");
        }

        if(!booking.getUserId().equals(userId)){
            throw new ApiException("Not allowed to delete this booking");
        }

        bookingRepository.delete(booking);
    }

    //----------------

    //Check if the time slot is already booked
    public void preventDoubleBooking(Booking booking, Integer bookingId){

        List<Booking> overlapping = bookingRepository.findOverlappingBookings(booking.getSpaceId(),booking.getBookingDate(),booking.getStartTime(), booking.getEndTime());

        for (Booking b : overlapping) {
            if (bookingId == null || !b.getId().equals(bookingId)) {
                throw new ApiException("Time slot conflicts with another booking");
            }
        }
    }

    // Create multiple bookings on a weekly recurring basis
    public void createRecurringBooking(Booking booking, int repeatCount) {
        for(int i = 0; i < repeatCount; i++){
            Booking newBooking = new Booking();

            newBooking.setUserId(booking.getUserId());
            newBooking.setSpaceId(booking.getSpaceId());
            newBooking.setBookingDate(booking.getBookingDate().plusDays(i * 7));
            newBooking.setStartTime(booking.getStartTime());
            newBooking.setEndTime(booking.getEndTime());
            newBooking.setTotalPrice(booking.getTotalPrice());
            newBooking.setStatus("PENDING");

            preventDoubleBooking(newBooking, null);
            bookingRepository.save(newBooking);
        }
    }

    //Change booking date and time
    public void rescheduleBooking(Integer id, LocalDate date, LocalTime start, LocalTime end){
        Booking booking = bookingRepository.findBookingById(id);

        if(booking == null) {
            throw new ApiException("Booking Not Found");
        }

        if(end.isBefore(start)) {
            throw new ApiException("Invalid time");
        }

        booking.setBookingDate(date);
        booking.setStartTime(start);
        booking.setEndTime(end);

        preventDoubleBooking(booking, id);

        bookingRepository.save(booking);
    }

    //Cancel Booking with Policy
    public void cancelBooking(Integer id){
        Booking booking = bookingRepository.findBookingById(id);

        if(booking == null)
            throw new ApiException("Booking Not Found");

        LocalDateTime bookingDateTime = LocalDateTime.of(booking.getBookingDate(), booking.getStartTime());

        if(bookingDateTime.isBefore(LocalDateTime.now().plusHours(24))){
            throw new ApiException("Cannot cancel within 24 hours of booking");
        }

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);
    }

    //Get all bookings for a specific user sorted
    public List<Booking> getUserBookings(Integer userId){
        return bookingRepository.findUserBookingsSorted(userId);
    }

    //Calculate booking price based on duration
    public Double estimatePrice(Integer spaceId, LocalTime start, LocalTime end){
        Space space = spaceRepository.findSpaceById(spaceId);

        if(space == null){
            throw new ApiException("Space not found");
        }

        long minutes = java.time.Duration.between(start, end).toMinutes();
        Double hours = minutes / 60.0;

        return hours * space.getPricePerHour();
    }

    // Send booking confirmation via email and WhatsApp
    public void notifyBooking(Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);

        if(booking == null){
            throw new ApiException("Booking not found");
        }

        User user = userRepository.findUserById(booking.getUserId());

        if(user == null){
            throw new ApiException("User not found");
        }

        String message = "Your booking is confirmed";

        emailService.sendEmail(user.getEmail(), "Booking", message);
        whatsAppService.sendWhatsApp(user.getPhone(), message);
    }

    //Send reminder for upcoming booking
    public void sendReminder(Integer bookingId){
        Booking booking = bookingRepository.findBookingById(bookingId);

        if(booking.getBookingDate().equals(LocalDate.now().plusDays(1))){
            User user = userRepository.findUserById(booking.getUserId());

            whatsAppService.sendWhatsApp(user.getPhone(),"Reminder for tomorrow booking");
        }
    }

    //Calculate booking summary with total price
    public BookingSummary getBookingSummary(Integer spaceId, LocalTime start, LocalTime end){

        Space space = spaceRepository.findSpaceById(spaceId);

        if(space == null){
            throw new ApiException("Space not found");
        }

        if(end.isBefore(start)){
            throw new ApiException("Invalid time");
        }

        long minutes = Duration.between(start, end).toMinutes();
        double hours = minutes / 60.0;

        double price = hours * space.getPricePerHour();

        return new BookingSummary(space.getName(), start,end,price);
    }

    // Confirm booking payment through payment service
    public PaymentResponse confirmBookingPayment(Integer bookingId){
        return paymentService.pay(bookingId);
    }
}
