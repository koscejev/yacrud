package cz.koscejev.yacrud;

import cz.koscejev.yacrud.model.Customer;
import cz.koscejev.yacrud.model.CustomerNote;
import cz.koscejev.yacrud.model.CustomerStatus;
import cz.koscejev.yacrud.repository.CustomerNoteRepository;
import cz.koscejev.yacrud.repository.CustomerRepository;
import cz.koscejev.yacrud.repository.VaadinJpaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.Instant;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = VaadinJpaRepository.class)
public class YacrudApplication {

	public static void main(String[] args) {
		SpringApplication.run(YacrudApplication.class, args);
	}

	@Bean
	public CommandLineRunner defaultData(CustomerRepository customers, CustomerNoteRepository customerNotes) {
		return (args) -> {
			Customer johnDoe = customers.save(Customer.builder()
				.name("John Doe")
				.contact("Important Person Street 1")
				.createdOn(Instant.now())
				.status(CustomerStatus.CURRENT)
				.build());
			customerNotes.save(CustomerNote.builder()
				.customer(johnDoe)
				.createdOn(Instant.now())
				.text("Possibly the best customer")
				.build());
			customerNotes.save(CustomerNote.builder()
				.customer(johnDoe)
				.createdOn(Instant.now())
				.text("Always pays on time")
				.build());
			customerNotes.save(CustomerNote.builder()
				.customer(johnDoe)
				.createdOn(Instant.now())
				.text("Doesn't ask for unreasonable customizations")
				.build());

			Customer adamSmith = customers.save(Customer.builder()
				.name("Adam Smith")
				.contact("Important Person Street 2")
				.createdOn(Instant.now())
				.status(CustomerStatus.PROSPECTIVE)
				.build());
			customerNotes.save(CustomerNote.builder()
				.customer(adamSmith)
				.createdOn(Instant.now())
				.text("This customer has always asked for additional customizations that make no sense."
					+ "\nSomehow we always underestimated the effort required for those customizations."
					+ "\nSo we were barely able to break even. Avoid this customer if at all possible."
					+ "\nAlso, this is a very long text.")
				.build());
		};
	}
}
