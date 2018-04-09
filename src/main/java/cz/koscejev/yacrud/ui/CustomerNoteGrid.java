package cz.koscejev.yacrud.ui;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.ui.Grid;
import cz.koscejev.yacrud.model.Customer;
import cz.koscejev.yacrud.model.CustomerNote;
import cz.koscejev.yacrud.repository.CustomerNoteRepository;
import org.springframework.data.domain.Example;

import java.util.stream.Stream;

public class CustomerNoteGrid extends Grid<CustomerNote> {
	private final ConfigurableFilterDataProvider<CustomerNote, Void, Example<CustomerNote>> dataProvider;

	public CustomerNoteGrid(CustomerNoteRepository repo) {
		addColumn(CustomerNote::getCreatedOn)
			.setId("createdOn")
			.setCaption("Created On")
			.setExpandRatio(1);
		addColumn(CustomerNote::getText)
			.setId("text")
			.setCaption("Text")
			.setExpandRatio(15)
			.setSortable(false);
		setSizeFull();

		setDataProvider(dataProvider = DataProvider.<CustomerNote, Example<CustomerNote>>fromFilteringCallbacks(
			query -> query.getFilter()
				.map(filter -> repo.streamByExample(query))
				.orElse(Stream.empty()),
			query -> query.getFilter()
				.map(repo::count)
				.map(Math::toIntExact)
				.orElse(0))
			.withConfigurableFilter());
	}

	public void setCustomer(Customer customer) {
		if (customer != null) {
			dataProvider.setFilter(Example.of(CustomerNote.builder().customer(customer).build()));
		} else {
			dataProvider.setFilter(null);
		}
	}
}
