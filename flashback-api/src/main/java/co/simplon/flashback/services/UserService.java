package co.simplon.flashback.services;

import java.util.Collection;

import co.simplon.flashback.dtos.SignIn;
import co.simplon.flashback.dtos.SignUp;
import co.simplon.flashback.dtos.TokenInfo;
import co.simplon.flashback.dtos.TokenRefreshInfo;
import co.simplon.flashback.dtos.UserItem;
import co.simplon.flashback.errors.DataException;

public interface UserService {

	void signUp(SignUp inputs);

	Boolean existsByEmail(String email);

	TokenInfo signIn(SignIn inputs);

	Collection<UserItem> getAllUserItems() throws DataException;

	void deleteUser(Long userId);

	TokenRefreshInfo refresh();
}
