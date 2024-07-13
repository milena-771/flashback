package co.simplon.flashback;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import co.simplon.flashback.controllers.AdminController;
import co.simplon.flashback.controllers.AuthController;
import co.simplon.flashback.controllers.FavoriteController;
import co.simplon.flashback.controllers.MovieController;
import co.simplon.flashback.controllers.RetrospectiveController;

@TestConfiguration
class ControllerMocks {

    @Bean
    MovieController movieController() {
	return Mockito.mock(MovieController.class);
    }

    @Bean
    AdminController adminController() {
	return Mockito.mock(AdminController.class);
    }

    @Bean
    AuthController authController() {
	return Mockito.mock(AuthController.class);
    }

    @Bean
    FavoriteController favoriteController() {
	return Mockito.mock(FavoriteController.class);
    }

    @Bean
    RetrospectiveController retrospectiveController() {
	return Mockito.mock(RetrospectiveController.class);
    }

}