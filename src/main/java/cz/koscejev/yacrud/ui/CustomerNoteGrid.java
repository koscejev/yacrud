package cz.koscejev.yacrud.ui;

import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import cz.koscejev.yacrud.model.Customer;
import cz.koscejev.yacrud.model.CustomerNote;
import cz.koscejev.yacrud.repository.CustomerNoteRepository;
import org.springframework.data.domain.Example;

import java.util.stream.Stream;

public class CustomerNoteGrid extends CustomComponent {

	private final ConfigurableFilterDataProvider<CustomerNote, Void, Example<CustomerNote>> dataProvider;
	private final Grid<CustomerNote> grid;

	public CustomerNoteGrid(CustomerNoteRepository repo) {
		grid = new Grid<>();

		grid.addColumn(CustomerNote::getCreatedOn)
			.setId("createdOn")
			.setCaption("Created On")
			.setExpandRatio(0);
		grid.addColumn(CustomerNote::getText)
			.setId("text")
			.setCaption("Text")
			.setExpandRatio(1)
			.setSortable(false);
		grid.setSelectionMode(Grid.SelectionMode.SINGLE);
		grid.setSizeFull();

		dataProvider = DataProvider.<CustomerNote, Example<CustomerNote>>fromFilteringCallbacks(
			query -> query.getFilter()
				.map(filter -> repo.streamByExample(query))
				.orElse(Stream.empty()),
			query -> query.getFilter()
				.map(repo::count)
				.map(Math::toIntExact)
				.orElse(0))
			.withConfigurableFilter();
		grid.setDataProvider(dataProvider);

		Panel panel = new Panel("Customer Notes", grid);
		panel.setSizeFull();

		setCompositionRoot(panel);
	}

	public void setCustomer(Customer customer) {
		if (customer != null) {
			dataProvider.setFilter(Example.of(CustomerNote.builder().customer(customer).build()));
		} else {
			dataProvider.setFilter(null);
		}
	}

	public Registration addSelectionListener(SelectionListener<CustomerNote> listener) {
		return grid.addSelectionListener(listener);
	}

	public void refreshAll() {
		dataProvider.refreshAll();
	}
}
