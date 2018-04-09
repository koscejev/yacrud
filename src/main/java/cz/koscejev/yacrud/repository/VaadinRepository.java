package cz.koscejev.yacrud.repository;

import com.vaadin.data.provider.Query;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.stream.Stream;

@NoRepositoryBean
public interface VaadinRepository<T, ID> extends JpaRepository<T, ID> {

	Stream<T> streamAll(Query<T, Void> query);

	Stream<T> streamByExample(Query<T, Example<T>> query);
}
