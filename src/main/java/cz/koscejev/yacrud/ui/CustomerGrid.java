package cz.koscejev.yacrud.ui;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import cz.koscejev.yacrud.model.Customer;
import cz.koscejev.yacrud.model.CustomerStatus;
import cz.koscejev.yacrud.repository.CustomerRepository;

import static com.vaadin.data.provider.DataProvider.fromCallbacks;
import static java.util.Arrays.asList;

public class CustomerGrid extends Grid<Customer> {

	public CustomerGrid(CustomerRepository repo) {
		addColumn(Customer::getName)
			.setId("name")
			.setCaption("Name")
			.setExpandRatio(1);
		addColumn(Customer::getStatus)
			.setId("status")
			.setCaption("Status")
			.setExpandRatio(1)
			.setEditorComponent(getStatusEditor(), Customer::setStatus);
		addColumn(Customer::getCreatedOn)
			.setId("createdOn")
			.setCaption("Created On")
			.setExpandRatio(1);
		addColumn(Customer::getContact)
			.setId("contact")
			.setCaption("Contact")
			.setExpandRatio(15)
			.setSortable(false);

		setSizeFull();
		setDataProvider(fromCallbacks(repo::streamAll, query -> (int) repo.count()));

		getEditor().setEnabled(true);
		getEditor().addSaveListener(event -> repo.save(event.getBean()));
	}

	private ComboBox<CustomerStatus> getStatusEditor() {
		ComboBox<CustomerStatus> field = new ComboBox<>("Status", asList(CustomerStatus.values()));
		field.setEmptySelectionAllowed(false);
		field.setTextInputAllowed(false);
		return field;
	}

}
