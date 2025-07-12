package co.simplon.flashback;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlashbackApiApplication {

	public static void main(String[] args) {
		Logger LOG = LogManager.getLogger(FlashbackApiApplication.class);
		LOG.info("Démarrage de l'application FlashbackApiApplication");
		SpringApplication.run(FlashbackApiApplication.class, args);
	}

}
