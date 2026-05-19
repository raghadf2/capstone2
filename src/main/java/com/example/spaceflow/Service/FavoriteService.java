package com.example.spaceflow.Service;

import com.example.spaceflow.Api.ApiException;
import com.example.spaceflow.Model.Favorite;
import com.example.spaceflow.Model.Space;
import com.example.spaceflow.Repository.FavoriteRepository;
import com.example.spaceflow.Repository.SpaceRepository;
import com.example.spaceflow.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final SpaceRepository spaceRepository;
    private final UserService userService;

    //CRUD
    public void addFavorite(Favorite favorite){
        userService.checkUserRole(favorite.getUserId(), "USER");

        if(spaceRepository.findSpaceById(favorite.getSpaceId()) == null){
            throw new ApiException("Space Not Found");
        }

        if(favoriteRepository.findFavoriteByUserIdAndSpaceId(favorite.getUserId(), favorite.getSpaceId()) != null){
            throw new ApiException("This space is already in your favorites");
        }

        favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavorites(){
        return favoriteRepository.findAll();
    }

    public void deleteFavorite(Integer id, Integer userId){
        userService.checkUserRole(userId, "USER");

        Favorite fav = favoriteRepository.findFavoriteById(id);

        if(fav == null){
            throw new ApiException("Favorite Not Exists");
        }

        if(!fav.getUserId().equals(userId)){
            throw new ApiException("Not allowed");
        }

        favoriteRepository.delete(fav);
    }

    //------------

    //Get all favorite spaces for a specific user
    public List<Space> getUserFavorites(Integer userId){
        List<Space> spaces = favoriteRepository.getUserFavorites(userId);

        if(spaces.isEmpty()){
            throw new ApiException("No favorites found");
        }

        return spaces;
    }
}
