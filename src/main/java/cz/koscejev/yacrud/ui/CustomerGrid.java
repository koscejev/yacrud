package cz.koscejev.yacrud.ui;

import com.vaadin.data.HasValue;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.shared.Registration;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import cz.koscejev.yacrud.model.Customer;
import cz.koscejev.yacrud.model.CustomerStatus;
import cz.koscejev.yacrud.repository.CustomerRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.springframework.data.domain.ExampleMatcher.matchingAny;

public class CustomerGrid extends CustomComponent {

	private final ConfigurableFilterDataProvider<Customer, Void, Example<Customer>> dataProvider;
	private final Grid<Customer> grid;
	private final ExampleMatcher searchMatcher = matchingAny()
		.withIgnoreCase()
		.withIgnoreNullValues()
		.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

	public CustomerGrid(CustomerRepository repo) {
		grid = new Grid<>();

		grid.addColumn(Customer::getName)
			.setId("name")
			.setCaption("Name")
			.setExpandRatio(1);
		grid.addColumn(Customer::getStatus)
			.setId("status")
			.setCaption("Status")
			.setExpandRatio(1)
			.setEditorComponent(getStatusEditor(), Customer::setStatus);
		grid.addColumn(Customer::getCreatedOn)
			.setId("createdOn")
			.setCaption("Created On")
			.setExpandRatio(1);
		grid.addColumn(Customer::getContact)
			.setId("contact")
			.setCaption("Contact")
			.setExpandRatio(15)
			.setSortable(false);
		grid.setSizeFull();

		dataProvider = DataProvider.<Customer, Example<Customer>>fromFilteringCallbacks(
			query -> query.getFilter()
				.map(filter -> repo.streamByExample(query))
				.orElse(Stream.empty()),
			query -> query.getFilter()
				.map(repo::count)
				.map(Math::toIntExact)
				.orElse(0))
			.withConfigurableFilter();
		dataProvider.setFilter(Example.of(new Customer()));
		grid.setDataProvider(dataProvider);

		grid.getEditor().setEnabled(true);
		grid.getEditor().addSaveListener(event -> repo.save(event.getBean()));

		TextField searchField = new TextField();
		searchField.setPlaceholder("Search by name or contact");
		searchField.setWidth("100%");
		searchField.addValueChangeListener(event -> dataProvider.setFilter(getSearchFilter(event)));

		VerticalLayout layout = new VerticalLayout(searchField, grid);
		layout.setSizeFull();
		layout.setExpandRatio(searchField, 0);
		layout.setExpandRatio(grid, 1);

		Panel panel = new Panel("Customers", layout);
		panel.setSizeFull();

		setCompositionRoot(panel);
	}

	private Example<Customer> getSearchFilter(HasValue.ValueChangeEvent<String> event) {
		return Example.of(
			Customer.builder()
				.name(event.getValue())
				.contact(event.getValue())
				.build(),
			searchMatcher);
	}

	private ComboBox<CustomerStatus> getStatusEditor() {
		ComboBox<CustomerStatus> field = new ComboBox<>("Status", asList(CustomerStatus.values()));
		field.setEmptySelectionAllowed(false);
		field.setTextInputAllowed(false);
		return field;
	}

	public Registration addSelectionListener(SelectionListener<Customer> listener) {
		return grid.addSelectionListener(listener);
	}
}
