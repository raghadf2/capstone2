package com.example.spaceflow.Controller;

import com.example.spaceflow.Api.ApiResponse;
import com.example.spaceflow.Model.Space;
import com.example.spaceflow.Service.SpaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/api/v1/space")
@RequiredArgsConstructor
public class SpaceController {

    private final SpaceService spaceService;

    @PostMapping("/add")
    public ResponseEntity<?> addSpace(@RequestBody @Valid Space space){
        spaceService.addSpace(space);
        return ResponseEntity.status(200).body(new ApiResponse("Space Added"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getSpaces(){
        return ResponseEntity.status(200).body(spaceService.getSpaces());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateSpace(@PathVariable Integer id,@RequestBody @Valid Space space){
        spaceService.updateSpace(id,space);
        return ResponseEntity.status(200).body(new ApiResponse("Space Updated"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSpace(@PathVariable Integer id, @RequestParam Integer ownerId){
        spaceService.deleteSpace(id,ownerId);
        return ResponseEntity.status(200).body(new ApiResponse("Space Deleted"));
    }

    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSpaces(@RequestParam Integer capacity, @RequestParam LocalDate date) {
        return ResponseEntity.ok(spaceService.getAvailableSpaces(capacity, date));
    }

    @GetMapping("/filter/advanced")
    public ResponseEntity<?> filterAdvanced(@RequestParam String city,@RequestParam Double min, @RequestParam Double max) {
        return ResponseEntity.status(200).body(spaceService.filterAdvanced(city, min, max));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<?> getTopRatedSpaces() {
        return ResponseEntity.ok(spaceService.getTopRatedSpaces());
    }

    @GetMapping("/most-booked")
    public ResponseEntity<?> getMostBookedSpaces() {
        return ResponseEntity.ok(spaceService.getMostBookedSpaces());
    }

    @GetMapping("/recommend/{userId}")
    public ResponseEntity<?> recommendSpaces(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(spaceService.recommendSpaces(userId));
    }

    @GetMapping("/similar/{spaceId}")
    public ResponseEntity<?> similarSpaces(@PathVariable Integer spaceId) {
        return ResponseEntity.status(200).body(spaceService.similarSpaces(spaceId));
    }


}

