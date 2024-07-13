package co.simplon.flashback.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.simplon.flashback.dtos.SignIn;
import co.simplon.flashback.dtos.SignUp;
import co.simplon.flashback.dtos.TokenInfo;
import co.simplon.flashback.dtos.TokenRefreshInfo;
import co.simplon.flashback.dtos.UserItem;
import co.simplon.flashback.entities.Retrospective;
import co.simplon.flashback.entities.Role;
import co.simplon.flashback.entities.User;
import co.simplon.flashback.errors.FlashbackException;
import co.simplon.flashback.repositories.FavoriteRepository;
import co.simplon.flashback.repositories.ParticipantRepository;
import co.simplon.flashback.repositories.ProgramRepository;
import co.simplon.flashback.repositories.RetrospectiveRepository;
import co.simplon.flashback.repositories.RoleRepository;
import co.simplon.flashback.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Value("${flashback-api.auth.tokenAccessExp}")
    private long tokenExpiration;

    @Value("${flashback-api.auth.tokenRefreshExp}")
    private long tokenRefreshExpiration;

    private final MessageSource messageSource;

    private final UserRepository users;

    private final RoleRepository roles;

    private final AuthHelper authHelper;

    private final FavoriteRepository favorites;

    private final ParticipantRepository participants;

    private final RetrospectiveRepository retrospectives;

    private final ProgramRepository containor;

    public UserServiceImpl(UserRepository users,
	    RoleRepository roles, AuthHelper authHelper,
	    FavoriteRepository favorites,
	    ParticipantRepository participants,
	    RetrospectiveRepository retrospectives,
	    ProgramRepository containor,
	    MessageSource messageSource) {
	this.users = users;
	this.roles = roles;
	this.authHelper = authHelper;
	this.favorites = favorites;
	this.participants = participants;
	this.retrospectives = retrospectives;
	this.containor = containor;
	this.messageSource = messageSource;
    }

    @Override
    @Transactional
    public void signUp(SignUp inputs) {
	User user = new User();
	user.setFirstname(inputs.getFirstname());
	user.setLastname(inputs.getLastname());
	user.setEmail(inputs.getEmail());
	String hashPassword = authHelper
		.encode(inputs.getPassword());
	user.setPassword(hashPassword);
	Role role = roles.getReferenceById(2L);
	user.setRole(role);
	LocalDate today = LocalDate.now();
	user.setCreatedAt(today);
	users.save(user);
    }

    @Override
    public Boolean existsByEmail(String email) {
	return users.existsByEmail(email);
    }

    @Override
    public TokenRefreshInfo refresh() {
	String subject = SecurityContextHolder.getContext()
		.getAuthentication().getName();
	Long userId = Long.valueOf(subject);
	User user = users.findById(userId).get();
	Role userRole = user.getRole();
	String roleName = userRole.getRoleName();
	String refreshToken = authHelper.refreshJWT(
		roleName, subject, tokenRefreshExpiration);
	TokenRefreshInfo tokenInfo = new TokenRefreshInfo();
	tokenInfo.setToken(refreshToken);
	tokenInfo.setRole(roleName);
	tokenInfo.setFirstname(user.getFirstname());
	LocalDateTime now = LocalDateTime.now();
	return tokenInfo;
    }

    @Override
    public TokenInfo signIn(SignIn inputs) {
	String email = inputs.getEmail();
	String candidate = inputs.getPassword();
	User user = users.findByEmail(email);
	if (user != null) {
	    boolean match = authHelper.matches(candidate,
		    user.getPassword());
	    if (match) {
		String identifier = user.getId().toString();
		Role userRole = user.getRole();
		String roleName = userRole.getRoleName();
		String token = authHelper
			.createJWT(roleName, identifier);
		TokenInfo tokenInfo = new TokenInfo();
		tokenInfo.setToken(token);
		tokenInfo.setRole(roleName);
		tokenInfo.setFirstname(user.getFirstname());
		LocalDateTime now = LocalDateTime.now();
		tokenInfo.setExp(
			now.plusSeconds(tokenExpiration));
		return tokenInfo;
	    } else {
		throw new FlashbackException(
			"Wrong credentials",
			HttpStatus.BAD_REQUEST.name());
	    }
	} else {
	    throw new FlashbackException(
		    "Wrong credentials",
		    HttpStatus.BAD_REQUEST.name());
	}
    }

    @Override
    public Collection<UserItem> getAllUserItems() {
	return users.getAllUsers();
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
	if (users.existsById(userId)) {
	    favorites.deleteByUserId(userId);
	    if (participants.existsByUserId(userId)) {
		Set<Retrospective> retroWithUserAsParticipant = participants
			.findByUserId(userId);
		participants.deleteByUserId(userId);
		for (Retrospective retrospective : retroWithUserAsParticipant) {
		    retrospective.setParticipantsNumber(
			    retrospective
				    .getParticipantsNumber()
				    - 1);
		    retrospectives.save(retrospective);
		}
	    }
	    if (retrospectives
		    .existsByOrganizerId(userId)) {
		Set<Retrospective> retroWithUserAsOrganizer = retrospectives
			.findByOrganizerId(userId);
		for (Retrospective retrospective : retroWithUserAsOrganizer) {
		    participants.deleteByRetrospectiveId(
			    retrospective.getId());
		    containor.deleteByRetrospectiveId(
			    retrospective.getId());
		}
		retrospectives.deleteByOrganizerId(userId);
	    }
	    users.deleteById(userId);
	} else {
	    throw new FlashbackException(
		    messageSource.getMessage(
			    "error.user.remove.admin", null,
			    Locale.getDefault()),
		    HttpStatus.BAD_REQUEST.name());
	}

    }

}
