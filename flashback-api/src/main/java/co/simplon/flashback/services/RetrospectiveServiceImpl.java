
package co.simplon.flashback.services;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.NoSuchMessageException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.simplon.flashback.dtos.AdminRetroDetails;
import co.simplon.flashback.dtos.AdminRetroDetailsWithMovies;
import co.simplon.flashback.dtos.AdminRetroItem;
import co.simplon.flashback.dtos.DeviceDetails;
import co.simplon.flashback.dtos.DirectorDetails;
import co.simplon.flashback.dtos.FavoriteAndLabelsAndRetroDetailsForUpdate;
import co.simplon.flashback.dtos.MovieForSearch;
import co.simplon.flashback.dtos.MovieItem;
import co.simplon.flashback.dtos.OrgaRetroForUpdate;
import co.simplon.flashback.dtos.ParticipantRetroDetails;
import co.simplon.flashback.dtos.ParticipantRetroForUpdate;
import co.simplon.flashback.dtos.RetroDetailsForUpdate;
import co.simplon.flashback.dtos.RetroItem;
import co.simplon.flashback.dtos.RetroItemsAsOrgaAndParticipant;
import co.simplon.flashback.dtos.RetrospectiveCreate;
import co.simplon.flashback.dtos.RetrospectiveUpdate;
import co.simplon.flashback.entities.Device;
import co.simplon.flashback.entities.Movie;
import co.simplon.flashback.entities.Participant;
import co.simplon.flashback.entities.Program;
import co.simplon.flashback.entities.Retrospective;
import co.simplon.flashback.entities.User;
import co.simplon.flashback.errors.ApiException;
import co.simplon.flashback.errors.ForbiddenException;
import co.simplon.flashback.errors.NotFoundException;
import co.simplon.flashback.repositories.DeviceRepository;
import co.simplon.flashback.repositories.DirectionRepository;
import co.simplon.flashback.repositories.MovieRepository;
import co.simplon.flashback.repositories.ParticipantRepository;
import co.simplon.flashback.repositories.ProgramRepository;
import co.simplon.flashback.repositories.RetrospectiveRepository;
import co.simplon.flashback.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
public class RetrospectiveServiceImpl implements RetrospectiveService {

	private final Logger LOG = LogManager.getLogger(RetrospectiveServiceImpl.class);

	private final RetrospectiveRepository retrospectives;

	private final DeviceRepository devices;

	private final UserRepository users;

	private final ProgramRepository containor;

	private final MovieRepository movies;

	private final ParticipantRepository participants;

	private final DirectionRepository direction;

	private final FavoriteService favoriteService;

	public RetrospectiveServiceImpl(RetrospectiveRepository retrospectives,
			DeviceRepository devices, UserRepository users, ProgramRepository containor,
			MovieRepository movies, ParticipantRepository participants,
			DirectionRepository direction, FavoriteService favoriteService) {
		this.retrospectives = retrospectives;
		this.devices = devices;
		this.users = users;
		this.containor = containor;
		this.movies = movies;
		this.participants = participants;
		this.direction = direction;
		this.favoriteService = favoriteService;
	}

	@Override
	public Collection<AdminRetroItem> getAllRetrospectives() {
		try {
			LOG.info("START >>> getAllRetrospectives");
			return retrospectives
					.findAllProjectedByOrderByStartDateAscEndDateAscRetrospectiveName();
		} finally {
			LOG.info("END <<< getAllRetrospectives");
		}
	}

	@Override
	@Transactional
	public void createRetro(RetrospectiveCreate inputs) {
		try {
			LOG.info("START >>> createRetro");
			Retrospective entity = new Retrospective();
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long id = Long.valueOf(subject);
			User user = users.getReferenceById(id);
			Device device = devices.getReferenceById(inputs.deviceId());
			entity.setRetrospectiveName(inputs.retrospectiveName());
			entity.setStartDate(inputs.startDate());
			entity.setEndDate(inputs.endDate());
			entity.setDescription(inputs.description());
			entity.setDevice(device);
			entity.setOrganizer(user);
			entity.setMoviesNumber(inputs.movieId().size());
			entity.setParticipantsNumber(0);
			Retrospective retrospective = retrospectives.save(entity);
			for (Long movieId : inputs.movieId()) {
				Program retrospectiveContain = new Program();
				Movie movie = movies.getReferenceById(movieId);
				retrospectiveContain.setMovie(movie);
				retrospectiveContain.setRetrospective(retrospective);
				containor.save(retrospectiveContain);
			}
		} finally {
			LOG.info("END <<< createRetro");
		}
	}

	@Override
	@Transactional
	public void deleteRetroByOrganizer(Long retrospectiveId)
			throws NoSuchMessageException, ApiException {
		try {
			LOG.info("START >>> deleteRetroByOrganizer");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			Retrospective retrospective = retrospectives.findById(retrospectiveId)
					.orElseThrow(() -> {
						LOG.error("Retrospective not found in deleteRetroByOrganizer: {}",
								retrospectiveId);
						return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
								"Retrospective not found: " + retrospectiveId);
					});
			if (userId == retrospective.getOrganizer().getId()) {
				containor.deleteByRetrospectiveId(retrospectiveId);
				participants.deleteByRetrospectiveId(retrospectiveId);
				retrospectives.deleteById(retrospectiveId);
			} else {
				LOG.error("User {} is not the organizer of this retrospective {}", userId,
						retrospectiveId);
				throw new ForbiddenException("NOT_ORGANIZER",
						"Only the organizer can delete this retrospective.");
			}
		} finally {
			LOG.info("END <<< deleteRetroByOrganizer");
		}
	}

	@Override
	@Transactional
	public void deleteRetrospectiveByAdmin(Long retrospectiveId) {
		try {
			LOG.info("START >>> deleteRetrospectiveByAdmin");
			if (retrospectives.existsById(retrospectiveId)) {
				containor.deleteByRetrospectiveId(retrospectiveId);
				participants.deleteByRetrospectiveId(retrospectiveId);
				retrospectives.deleteById(retrospectiveId);
			} else {
				LOG.error("Retrospective not found: {}", retrospectiveId);
				throw new NotFoundException("RETROSPECTIVE_NOT_FOUND",
						"Retrospective not found, deletion could not be completed.");
			}
		} finally {
			LOG.info("END <<< deleteRetrospectiveByAdmin");
		}
	}

	@Override
	public FavoriteAndLabelsAndRetroDetailsForUpdate getRetroDetailsForUpdate(
			Long retrospectiveId) {
		try {
			LOG.info("START >>> getRetroDetailsForUpdate");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			Retrospective retrospective = retrospectives.findById(retrospectiveId)
					.orElseThrow(() -> {
						LOG.error("Retrospective not found in getRetroDetailsForUpdate: {}",
								retrospectiveId);
						return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
								"Retrospective not found, could not access to this retrospective.");
					});
			if (userId == retrospective.getOrganizer().getId()) {
				FavoriteAndLabelsAndRetroDetailsForUpdate detailsForUpdate = new FavoriteAndLabelsAndRetroDetailsForUpdate();
				Collection<MovieForSearch> favorites = favoriteService
						.getAllFavoritesWithDirectors();
				Collection<DeviceDetails> labels = devices.findAllProjectedByOrderByDeviceName();
				OrgaRetroForUpdate retroDetails = getOrgaRetroDetails(retrospectiveId);
				detailsForUpdate.setDevices(labels);
				detailsForUpdate.setFavorites(favorites);
				detailsForUpdate.setRetroDetails(retroDetails);
				return detailsForUpdate;
			} else {
				LOG.error("User {} is not the organizer of this retrospective {}", userId,
						retrospectiveId);
				throw new ForbiddenException("NOT_ORGANIZER",
						"Only the organizer can update this retrospective.");
			}
		} finally {
			LOG.info("END <<< getRetroDetailsForUpdate");
		}
	}

	@Override
	@Transactional
	public void updateRetro(Long retrospectiveId, RetrospectiveUpdate inputs) {
		try {
			LOG.info("START >>> updateRetro");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			Retrospective entity = retrospectives.findById(retrospectiveId).orElseThrow(() -> {
				LOG.error("Retrospective not found in updateRetro: {}", retrospectiveId);
				return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
						"Retrospective not found, update could not be completed.");
			});
			if (userId == entity.getOrganizer().getId()) {
				entity.setRetrospectiveName(inputs.retrospectiveName());
				entity.setStartDate(inputs.startDate());
				entity.setEndDate(inputs.endDate());
				entity.setDescription(inputs.description());
				Device device = devices.getReferenceById(inputs.deviceId());
				entity.setDevice(device);
				entity.setMoviesNumber(inputs.movieId().size());
				retrospectives.save(entity);
				containor.deleteByRetrospectiveId(retrospectiveId);
				for (Long movieId : inputs.movieId()) {
					Program updateContainor = containor.findByMovieIdAndRetrospectiveId(movieId,
							retrospectiveId);
					Program retrospectiveContain = new Program();
					Movie movie = movies.getReferenceById(movieId);
					retrospectiveContain.setMovie(movie);
					retrospectiveContain.setRetrospective(entity);
					containor.save(retrospectiveContain);
				}
			} else {
				LOG.error("User {} is not the organizer of this retrospective {}", userId,
						retrospectiveId);
				throw new ForbiddenException("NOT_ORGANIZER",
						"Only the organizer can update this retrospective.");
			}
		} finally {
			LOG.info("END <<< updateRetro");
		}
	}

	@Override
	public Optional<Long> existsByRetrospectiveNameForUpdate(RetrospectiveUpdate inputs) {
		try {
			LOG.info("START >>> existsByRetrospectiveNameForUpdate");
			return retrospectives.existsByRetrospectiveNameForUpdate(inputs.retrospectiveName(),
					inputs.id());
		} finally {
			LOG.info("END <<< existsByRetrospectiveNameForUpdate");
		}
	}

	@Override
	public Boolean existsByRetroName(String name) {
		try {
			LOG.info("START >>> existsByRetroName");
			return retrospectives.existsByRetrospectiveName(name);
		} finally {
			LOG.info("END <<< existsByRetroName");
		}
	}

	@Override
	public Collection<DeviceDetails> getAllDeviceLabels() {
		try {
			LOG.info("START >>> getAllDeviceLabels");
			Collection<DeviceDetails> labels = devices.findAllProjectedByOrderByDeviceName();
			return labels;
		} finally {
			LOG.info("END <<< getAllDeviceLabels");
		}
	}

	@Override
	public RetroItemsAsOrgaAndParticipant getAllRetroAsOrgaAndParticipant() {
		try {
			LOG.info("START >>> getAllRetroAsOrgaAndParticipant");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			RetroItemsAsOrgaAndParticipant allRetros = new RetroItemsAsOrgaAndParticipant();
			LocalDate today = LocalDate.now();
			Collection<RetroItem> retroByOrga = retrospectives.findRetroByOrganizerAndByDate(userId,
					today);
			Collection<RetroItem> retroByParticipant = participants.findByUserIdAndDate(userId,
					today);
			allRetros.setRetroByOrga(retroByOrga);
			allRetros.setRetroByParticipant(retroByParticipant);
			return allRetros;
		} finally {
			LOG.info("END <<< getAllRetroAsOrgaAndParticipant");
		}
	}

	@Override
	public Collection<RetroItem> getAllRetroToCome() {
		try {
			LOG.info("--- START >>> getAllRetroToCome");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			LocalDate today = LocalDate.now();
			Collection<RetroItem> allRetros = new HashSet<>();
			Collection<RetroItem> retroExcludeAsOrganizerAndZeroParticipant = retrospectives
					.findRetroToCome(userId, today);
			for (RetroItem retro : retroExcludeAsOrganizerAndZeroParticipant) {
				Optional<Long> isParticipant = participants.existsByUserIdAndRetroId(retro.getId(),
						userId);
				if (!isParticipant.isPresent()) {
					allRetros.add(retro);
				}
			}
			return allRetros;
		} finally {
			LOG.info("END <<< getAllRetroToCome");
		}
	}

	@Override
	@Transactional
	public void addParticipant(Long retrospectiveId) {
		try {
			LOG.info("START >>> addParticipant");
			Participant entity = new Participant();
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			User user = users.findProjectedById(userId);
			Retrospective retrospective = retrospectives.findById(retrospectiveId)
					.orElseThrow(() -> {
						LOG.error("Retrospective not found in addParticipant: {}", retrospectiveId);
						return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
								"Retrospective not found: " + retrospectiveId);
					});
			Optional<Long> participantId = participants.existsByUserIdAndRetroId(retrospectiveId,
					userId);
			if (!participantId.isPresent() && (retrospective.getOrganizer() != user)) {
				retrospective.setParticipantsNumber(retrospective.getParticipantsNumber() + 1);
				retrospectives.save(retrospective);
				entity.setUser(user);
				entity.setRetrospective(retrospective);
				participants.save(entity);
			} else {
				LOG.error(
						"User {} is already a participant or the organizer of this retrospective {}",
						userId, retrospectiveId);
				throw new ForbiddenException("DUPLICATE_PARTICIPANT",
						"User already participant or organizer of this retrospective.");
			}
		} finally {
			LOG.info("END <<< addParticipant");
		}
	}

	@Override
	@Transactional
	public void removeParticipant(Long retrospectiveId) {
		try {
			LOG.info("START >>> removeParticipant");
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			User user = users.findProjectedById(userId);
			Retrospective retrospective = retrospectives.findById(retrospectiveId)
					.orElseThrow(() -> {
						LOG.error("Retrospective not found in removeParticipant: {}",
								retrospectiveId);
						return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
								"Retrospective not found, participant could not be removed from this retrospective.");
					});
			Optional<Long> isParticipantId = participants.existsByUserIdAndRetroId(retrospectiveId,
					userId);
			if (isParticipantId.isPresent() && (retrospective.getOrganizer() != user)) {
				Long participationId = participants.findByUserIdAndRetroId(retrospectiveId, userId);
				retrospective.setParticipantsNumber(retrospective.getParticipantsNumber() - 1);
				retrospectives.save(retrospective);
				participants.deleteById(participationId);
			} else {
				LOG.error(
						"User {} is not a participant or is the organizer of this retrospective {}",
						userId, retrospectiveId);
				throw new ForbiddenException("NOT_PARTICIPANT",
						"User is not participant or is the organizer of this retrospective.");
			}
		} finally {
			LOG.info("END <<< removeParticipant");
		}
	}

	@Override
	public OrgaRetroForUpdate getOrgaRetroDetails(Long retrospectiveId) {
		try {
			LOG.info("START >>> getOrgaRetroDetails");
			Retrospective retrospective = retrospectives.findById(retrospectiveId)
					.orElseThrow(() -> {
						LOG.error("Retrospective not found in getOrgaRetroDetails: {}",
								retrospectiveId);
						return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
								"Retrospective not found, could not access to this retrospective.");
					});
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			if (retrospective.getOrganizer().getId() == userId) {
				OrgaRetroForUpdate retroDetailsWithMovies = new OrgaRetroForUpdate();
				Set<MovieForSearch> moviesToWatch = new HashSet<>();
				RetroDetailsForUpdate retroDetails = new RetroDetailsForUpdate();
				retroDetails.setRetrospectiveName(retrospective.getRetrospectiveName());
				retroDetails.setStartDate(retrospective.getStartDate());
				retroDetails.setEndDate(retrospective.getEndDate());
				retroDetails.setDescription(retrospective.getDescription());
				retroDetails.setDevice(retrospective.getDevice());
				retroDetails.setDeviceName(retrospective.getDevice().getDeviceName());
				retroDetails.setParticipantsNumber(retrospective.getParticipantsNumber());
				Set<MovieItem> moviesList = containor.findByRetrospectiveId(retrospectiveId);
				for (MovieItem movie : moviesList) {
					MovieForSearch movieDetails = new MovieForSearch();
					movieDetails.setId(movie.getId());
					movieDetails.setTitle(movie.getTitle());
					movieDetails.setPoster(movie.getPoster());
					movieDetails.setReleaseYear(movie.getReleaseYear());
					Set<DirectorDetails> directorList = direction.getMovieDirector(movie.getId());
					movieDetails.setDirectors(directorList);
					moviesToWatch.add(movieDetails);
				}
				retroDetailsWithMovies.setMovieDetails(moviesToWatch);
				retroDetailsWithMovies.setRetroDetails(retroDetails);
				return retroDetailsWithMovies;
			} else {
				LOG.error("User {} is not the organizer of this retrospective {}", userId,
						retrospectiveId);
				throw new ForbiddenException("NOT_ORGANIZER",
						"Only the organizer can have access to this retrospective.");
			}
		} finally {
			LOG.info("END <<< getOrgaRetroDetails");
		}
	}

	@Override
	public ParticipantRetroForUpdate getParticipantRetroDetails(Long retrospectiveId) {
		try {
			LOG.info("START >>> getParticipantRetroDetails");
			Retrospective retrospective = retrospectives.findById(retrospectiveId)
					.orElseThrow(() -> {
						LOG.error("Retrospective not found in getParticipantRetroDetails: {}",
								retrospectiveId);
						return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
								"Retrospective not found, could not access to this retrospective.");
					});
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			Optional<Long> participantId = participants.existsByUserIdAndRetroId(retrospectiveId,
					userId);
			if (participantId.isPresent()) {
				ParticipantRetroForUpdate retroDetailsWithMovies = new ParticipantRetroForUpdate();
				ParticipantRetroDetails retroDetailsAsParticipant = new ParticipantRetroDetails();
				Set<MovieForSearch> moviesToWatch = new HashSet<>();
				retroDetailsAsParticipant
						.setRetrospectiveName(retrospective.getRetrospectiveName());
				retroDetailsAsParticipant.setStartDate(retrospective.getStartDate());
				retroDetailsAsParticipant.setEndDate(retrospective.getEndDate());
				retroDetailsAsParticipant.setDescription(retrospective.getDescription());
				retroDetailsAsParticipant.setDeviceName(retrospective.getDevice().getDeviceName());
				Set<MovieItem> moviesList = containor.findByRetrospectiveId(retrospectiveId);
				for (MovieItem movie : moviesList) {
					MovieForSearch movieDetails = new MovieForSearch();
					movieDetails.setId(movie.getId());
					movieDetails.setTitle(movie.getTitle());
					movieDetails.setPoster(movie.getPoster());
					movieDetails.setReleaseYear(movie.getReleaseYear());
					Set<DirectorDetails> directorList = direction.getMovieDirector(movie.getId());
					movieDetails.setDirectors(directorList);
					moviesToWatch.add(movieDetails);
				}
				retroDetailsWithMovies.setMovieDetails(moviesToWatch);
				retroDetailsWithMovies.setRetroDetails(retroDetailsAsParticipant);
				return retroDetailsWithMovies;
			} else {
				LOG.error("User {} is not a participant of this retrospective {}", userId,
						retrospectiveId);
				throw new ForbiddenException("NOT_PARTICIPANT",
						"Only participants can have access to this retrospective.");
			}
		} finally {
			LOG.info("END <<< getParticipantRetroDetails");
		}
	}

	@Override
	public ParticipantRetroForUpdate getRetroToComeDetails(Long retrospectiveId) {
		try {
			LOG.info("START >>> getRetroToComeDetails");
			Retrospective retrospective = retrospectives.findById(retrospectiveId)
					.orElseThrow(() -> {
						LOG.error("Retrospective not found in getRetroToComeDetails: {}",
								retrospectiveId);
						return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
								"Retrospective not found, could not access to this retrospective.");
					});
			String subject = SecurityContextHolder.getContext().getAuthentication().getName();
			Long userId = Long.valueOf(subject);
			Optional<Long> participantId = participants.existsByUserIdAndRetroId(retrospectiveId,
					userId);
			if (!participantId.isPresent() && (userId != retrospective.getOrganizer().getId())) {
				ParticipantRetroForUpdate retroDetailsWithMovies = new ParticipantRetroForUpdate();
				ParticipantRetroDetails retroDetailsAsParticipant = new ParticipantRetroDetails();
				Set<MovieForSearch> moviesToWatch = new HashSet<>();
				retroDetailsAsParticipant
						.setRetrospectiveName(retrospective.getRetrospectiveName());
				retroDetailsAsParticipant.setStartDate(retrospective.getStartDate());
				retroDetailsAsParticipant.setEndDate(retrospective.getEndDate());
				retroDetailsAsParticipant.setDescription(retrospective.getDescription());
				retroDetailsAsParticipant.setDeviceName(retrospective.getDevice().getDeviceName());
				Set<MovieItem> moviesList = containor.findByRetrospectiveId(retrospectiveId);
				for (MovieItem movie : moviesList) {
					MovieForSearch movieDetails = new MovieForSearch();
					movieDetails.setId(movie.getId());
					movieDetails.setTitle(movie.getTitle());
					movieDetails.setPoster(movie.getPoster());
					movieDetails.setReleaseYear(movie.getReleaseYear());
					Set<DirectorDetails> directorList = direction.getMovieDirector(movie.getId());
					movieDetails.setDirectors(directorList);
					moviesToWatch.add(movieDetails);
				}
				retroDetailsWithMovies.setMovieDetails(moviesToWatch);
				retroDetailsWithMovies.setRetroDetails(retroDetailsAsParticipant);
				return retroDetailsWithMovies;
			} else {
				LOG.error(
						"User {} is already a participant or the organizer of this retrospective {}",
						userId, retrospectiveId);
				throw new ForbiddenException("NOT_PARTICIPANT",
						"User already participant or organizer of this retrospective.");
			}
		} finally {
			LOG.info("END <<< getRetroToComeDetails");
		}
	}

	@Override
	public AdminRetroDetailsWithMovies getAdminRetroDetails(Long retrospectiveId) {
		try {
			LOG.info("START >>> getAdminRetroDetails");
			Retrospective retrospective = retrospectives.findById(retrospectiveId)
					.orElseThrow(() -> {
						LOG.error("Retrospective not found in getRetroToComeDetails: {}",
								retrospectiveId);
						return new NotFoundException("RETROSPECTIVE_NOT_FOUND",
								"Retrospective not found, could not access to this retrospective.");
					});
			AdminRetroDetailsWithMovies retroAndMovies = new AdminRetroDetailsWithMovies();
			AdminRetroDetails retroDetails = new AdminRetroDetails();
			Set<MovieForSearch> moviesToWatch = new HashSet<>();
			retroDetails.setRestrospectiveName(retrospective.getRetrospectiveName());
			retroDetails.setStartDate(retrospective.getStartDate());
			retroDetails.setEndDate(retrospective.getEndDate());
			retroDetails.setDescription(retrospective.getDescription());
			retroDetails.setDeviceName(retrospective.getDevice().getDeviceName());
			retroDetails.setParticipantsNumber(retrospective.getParticipantsNumber());
			retroDetails.setUserEmail(retrospective.getOrganizer().getEmail());
			Set<MovieItem> moviesList = containor.findByRetrospectiveId(retrospectiveId);
			for (MovieItem movie : moviesList) {
				MovieForSearch movieDetails = new MovieForSearch();
				movieDetails.setId(movie.getId());
				movieDetails.setTitle(movie.getTitle());
				movieDetails.setPoster(movie.getPoster());
				movieDetails.setReleaseYear(movie.getReleaseYear());
				Set<DirectorDetails> directorList = direction.getMovieDirector(movie.getId());
				movieDetails.setDirectors(directorList);
				moviesToWatch.add(movieDetails);
			}
			retroAndMovies.setMovieDetails(moviesToWatch);
			retroAndMovies.setRetroDetails(retroDetails);
			return retroAndMovies;
		} finally {
			LOG.info("END <<< getAdminRetroDetails");
		}
	}

}
