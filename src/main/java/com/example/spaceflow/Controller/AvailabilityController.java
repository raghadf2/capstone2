package com.example.spaceflow.Controller;

import com.example.spaceflow.Api.ApiResponse;
import com.example.spaceflow.Model.Availability;
import com.example.spaceflow.Service.AvailabilityService;
import com.example.spaceflow.Service.SpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping("/add")
    public ResponseEntity<?> addAvailability(@RequestBody @Valid Availability availability, @RequestParam Integer ownerId){
        availabilityService.addAvailability(availability,ownerId);
        return ResponseEntity.status(200).body(new ApiResponse("Availability Added"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAvailability(){
        return ResponseEntity.status(200).body(availabilityService.getAvailability());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAvailability(@PathVariable Integer id,@RequestBody @Valid Availability availability){
        availabilityService.updateAvailability(id,availability);
        return ResponseEntity.status(200).body(new ApiResponse("Availability Updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAvailability(@PathVariable Integer id){
        availabilityService.deleteAvailability(id);
        return ResponseEntity.status(200).body(new ApiResponse("Availability Deleted"));
    }

    // Next Available Slot
    @GetMapping("/next-available/{spaceId}")
    public ResponseEntity<?> nextAvailable(@PathVariable Integer spaceId, @RequestParam LocalDate date) {
        return ResponseEntity.ok(availabilityService.nextAvailable(spaceId,date));
    }

}
