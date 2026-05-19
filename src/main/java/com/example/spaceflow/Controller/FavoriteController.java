package com.example.spaceflow.Controller;

import com.example.spaceflow.Api.ApiResponse;
import com.example.spaceflow.Model.Favorite;
import com.example.spaceflow.Service.FavoriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<?> addFavorite(@RequestBody @Valid Favorite favorite){
        favoriteService.addFavorite(favorite);
        return ResponseEntity.status(200).body(new ApiResponse("Favorite Added"));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getFavorites(){
        return ResponseEntity.status(200).body(favoriteService.getFavorites());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFavorite(@PathVariable Integer id, @RequestParam Integer userId){
        favoriteService.deleteFavorite(id,userId);
        return ResponseEntity.status(200).body(new ApiResponse("Favorite Deleted"));
    }

    @GetMapping("/get-fav/{userId}")
    public ResponseEntity<?> getUserFavorites(@PathVariable Integer userId){
        return ResponseEntity.status(200).body(favoriteService.getUserFavorites(userId));
    }

}
