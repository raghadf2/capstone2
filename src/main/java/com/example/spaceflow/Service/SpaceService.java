package com.example.spaceflow.Service;

import com.example.spaceflow.Api.ApiException;
import com.example.spaceflow.Model.Booking;
import com.example.spaceflow.Model.Space;
import com.example.spaceflow.Model.User;
import com.example.spaceflow.Repository.BookingRepository;
import com.example.spaceflow.Repository.CategoryRepository;
import com.example.spaceflow.Repository.SpaceRepository;
import com.example.spaceflow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Service
@RequiredArgsConstructor
public class SpaceService {

    private final UserService userService;
    private final SpaceRepository spaceRepository;
    private final CategoryRepository categoryRepository;
    private final BookingRepository bookingRepository;


    //CRUD

    public void addSpace(Space space){
        userService.checkUserRole(space.getOwnerId(), "OWNER");

        if(spaceRepository.findSpaceByName(space.getName()) != null){
            throw new ApiException("Space Already Exists");
        }

        if(categoryRepository.findCategoriesById(space.getCategoryId()) == null){
            throw new ApiException("Category Not Found");
        }

        spaceRepository.save(space);
    }

    public List<Space> getSpaces(){
        return spaceRepository.findAll();
    }

    public void updateSpace(Integer id, Space space){
        Space oldSpace = spaceRepository.findSpaceById(id);

        if(oldSpace == null){
            throw new ApiException("Space not found");
        }

        userService.checkUserRole(space.getOwnerId(), "OWNER");

        if(!oldSpace.getOwnerId().equals(space.getOwnerId())){
            throw new ApiException("You are not the owner of this space");
        }

        Space exists = spaceRepository.findSpaceByName(space.getName());
        if(exists != null && !exists.getId().equals(id)){
            throw new ApiException("Space name already exists");
        }

        if(categoryRepository.findCategoriesById(space.getCategoryId()) == null){
            throw new ApiException("Category not found");
        }

        oldSpace.setName(space.getName());
        oldSpace.setDescription(space.getDescription());
        oldSpace.setPricePerHour(space.getPricePerHour());
        oldSpace.setCapacity(space.getCapacity());
        oldSpace.setSize(space.getSize());
        oldSpace.setCity(space.getCity());
        oldSpace.setAddress(space.getAddress());
        oldSpace.setImageUrl(space.getImageUrl());
        oldSpace.setFacilities(space.getFacilities());
        oldSpace.setCategoryId(space.getCategoryId());
        oldSpace.setStatus(space.getStatus());

        spaceRepository.save(oldSpace);
    }

    public void deleteSpace(Integer id, Integer ownerId){
        userService.checkUserRole(ownerId, "OWNER");

        Space space = spaceRepository.findSpaceById(id);

        if(space == null){
            throw new ApiException("Space not found");
        }

        if(!space.getOwnerId().equals(ownerId)){
            throw new ApiException("You are not the owner of this space");
        }

        spaceRepository.delete(space);
    }

    //---------------------

    //Get all available spaces based on capacity and selected date
    public List<Space> getAvailableSpaces(Integer capacity,LocalDate date) {
        String day = date.getDayOfWeek().toString().substring(0,3);
        day = day.toUpperCase();

        return spaceRepository.findAvailableSpacesByDay(day, capacity);
    }

    //Apply advanced filters like city and price range
    public List<Space> filterAdvanced(String city, Double min, Double max) {
        return spaceRepository.filterAdvanced(city, min, max);
    }

    //Get spaces with highest ratings
    public List<Space> getTopRatedSpaces() {
        return spaceRepository.topRatedSpaces();
    }

    //Get spaces that are booked the most
    public List<Integer> getMostBookedSpaces() {
        return spaceRepository.mostBookedSpaces();
    }

    public List<Space> recommendSpaces(Integer userId){
        List<Booking> bookings = bookingRepository.findBookingByUserId(userId);

        if(bookings.isEmpty()){
            return spaceRepository.topRatedSpaces();
        }

        Booking lastBooking = bookings.get(bookings.size()-1);

        Space lastSpace = spaceRepository.findSpaceById(lastBooking.getSpaceId());

        if(lastSpace == null){
            return spaceRepository.topRatedSpaces();
        }

        double min = lastSpace.getPricePerHour() - 50;
        double max = lastSpace.getPricePerHour() + 50;

        return spaceRepository.filterAdvanced(lastSpace.getCity(), min, max);
    }

    //Find similar spaces based on city and price range
    public List<Space> similarSpaces(Integer spaceId) {
        Space space = spaceRepository.findSpaceById(spaceId);
        if(space == null){
            throw new ApiException("Space not found");
        }
        return spaceRepository.filterAdvanced(space.getCity(), space.getPricePerHour() - 50, space.getPricePerHour() + 50);
    }

}
