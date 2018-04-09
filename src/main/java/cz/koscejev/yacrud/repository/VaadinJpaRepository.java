package cz.koscejev.yacrud.repository;

import com.vaadin.data.provider.Query;
import com.vaadin.shared.data.sort.SortDirection;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class VaadinJpaRepository<T, ID> extends SimpleJpaRepository<T, ID> implements VaadinRepository<T, ID> {

	public VaadinJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	public VaadinJpaRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
	}

	@Override
	public Stream<T> streamAll(Query<T, Void> query) {
		Sort sort = query.getSortOrders().stream()
			.map(order -> new Sort.Order(asDirection(order.getDirection()), order.getSorted()))
			.collect(collectingAndThen(toList(), Sort::by));

		return getQuery(null, sort)
			.setFirstResult(query.getOffset())
			.setMaxResults(query.getLimit())
			.getResultList().stream();
	}

	@Override
	public Stream<T> streamByExample(Query<T, Example<T>> query) {
		Sort sort = query.getSortOrders().stream()
			.map(order -> new Sort.Order(asDirection(order.getDirection()), order.getSorted()))
			.collect(collectingAndThen(toList(), Sort::by));

		return query.getFilter()
			.map(filter -> getQuery(getExampleSpecification(filter), sort)
				.setFirstResult(query.getOffset())
				.setMaxResults(query.getLimit())
				.getResultList().stream())
			.orElse(Stream.empty());
	}

	private Specification<T> getExampleSpecification(Example<T> example) {
		return (Specification<T>) (root, query, criteriaBuilder) ->
			QueryByExamplePredicateBuilder.getPredicate(root, criteriaBuilder, example);
	}

	private Sort.Direction asDirection(SortDirection direction) {
		switch (direction) {
			case ASCENDING:
				return Sort.Direction.ASC;
			case DESCENDING:
				return Sort.Direction.DESC;
			default:
				throw new IllegalArgumentException("Unknown direction: " + direction);
		}
	}
}
