package co.simplon.flashback.services;

import java.util.Collection;

import co.simplon.flashback.dtos.FavoriteAdd;
import co.simplon.flashback.dtos.MovieFavorite;
import co.simplon.flashback.dtos.MovieForSearch;

public interface FavoriteService {

    void addFavorite(FavoriteAdd inputs);

    void deleteFavorite(Long movieId);

    Collection<MovieFavorite> getAllFavorites();

    Collection<MovieForSearch> getAllFavoritesWithDirectors();
}
