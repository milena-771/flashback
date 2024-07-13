package co.simplon.flashback.services;

import java.util.Collection;

import co.simplon.flashback.dtos.FavoriteAdd;
import co.simplon.flashback.dtos.MovieFavorite;
import co.simplon.flashback.dtos.MovieForSearch;

public interface FavoriteService {

    void add(FavoriteAdd inputs);

    void delete(Long movieId);

    Collection<MovieFavorite> getAll();

    Collection<MovieForSearch> getAllFavoritesWithDirectors();
}
