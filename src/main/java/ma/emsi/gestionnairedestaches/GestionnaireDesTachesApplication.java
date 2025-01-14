package ma.emsi.gestionnairedestaches;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestionnaireDesTachesApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(GestionnaireDesTachesApplication.class, args);
    }
    @Override
    public void run(String... args) throws Exception { }

}
