package com.example.spaceflow.Service;

import com.example.spaceflow.Api.ApiException;
import com.example.spaceflow.DTO.TimeSlot;
import com.example.spaceflow.Model.Availability;
import com.example.spaceflow.Model.Booking;
import com.example.spaceflow.Model.Space;
import com.example.spaceflow.Repository.AvailabilityRepository;
import com.example.spaceflow.Repository.BookingRepository;
import com.example.spaceflow.Repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final BookingRepository bookingRepository;
    private final SpaceRepository spaceRepository;
    private final UserService userService;


    //CRUD

    public void addAvailability(Availability availability, Integer ownerId){
        userService.checkUserRole(ownerId, "OWNER");

        Space space = spaceRepository.findSpaceById(availability.getSpaceId());

        if(space == null){
            throw new ApiException("Space not found");
        }

        if(!space.getOwnerId().equals(ownerId)){
            throw new ApiException("You are not the owner of this space");
        }

        if(availabilityRepository.findAvailabilityBySpaceIdAndDay(
                availability.getSpaceId(),
                availability.getDay()) != null){

            throw new ApiException("Availability already exists for this day");
        }

        if(availability.getEndTime().isBefore(availability.getStartTime())){
            throw new ApiException("End time must be after start time");
        }

        availabilityRepository.save(availability);
    }

    public List<Availability> getAvailability(){
        return availabilityRepository.findAll();
    }

    public void updateAvailability(Integer id, Availability availability){

        Availability old = availabilityRepository.findAvailabilityById(id);

        if(old == null){
            throw new ApiException("Not Found");
        }

        Space space = spaceRepository.findSpaceById(availability.getSpaceId());

        userService.checkUserRole(space.getOwnerId(), "OWNER");

        if(availability.getEndTime().isBefore(availability.getStartTime())){
            throw new ApiException("Invalid time");
        }

        old.setDay(availability.getDay());
        old.setStartTime(availability.getStartTime());
        old.setEndTime(availability.getEndTime());
        old.setIsAvailable(availability.getIsAvailable());

        availabilityRepository.save(old);
    }

    public void deleteAvailability(Integer id){
        Availability exists = availabilityRepository.findAvailabilityById(id);

        if(exists == null){
            throw new ApiException("Availability Not Exists");
        }

        availabilityRepository.delete(exists);
    }

    //----------------

    // Find the next available time slot for a specific space on a given date
    public List<TimeSlot> nextAvailable(Integer spaceId, LocalDate date){

        Availability availability = availabilityRepository.findAvailabilityBySpaceIdAndDay(spaceId,date.getDayOfWeek().toString().substring(0,3).toUpperCase());

        if (availability == null || !availability.getIsAvailable()) {
            throw new ApiException("Space not available on this day");
        }

        List<Booking> allBookings = bookingRepository.findBookingBySpaceId(spaceId);

        List<Booking> bookings = new ArrayList<>();

        for (Booking b : allBookings) {
            if (b.getBookingDate().equals(date)) {
                bookings.add(b);
            }
        }

        bookings.sort((a, b) -> a.getStartTime().compareTo(b.getStartTime()));

        List<TimeSlot> slots = new ArrayList<>();

        LocalTime current = availability.getStartTime();

        for (Booking b : bookings) {

            if (current.isBefore(b.getStartTime())) {
                slots.add(new TimeSlot(current, b.getStartTime()));
            }

            if (current.isBefore(b.getEndTime())) {
                current = b.getEndTime();
            }
        }

        if (current.isBefore(availability.getEndTime())) {
            slots.add(new TimeSlot(current, availability.getEndTime()));
        }

        if (slots.isEmpty()) {
            throw new ApiException("No available slots for this day");
        }

        return slots;
    }
}
