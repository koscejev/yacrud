package cz.koscejev.yacrud.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import cz.koscejev.yacrud.repository.CustomerNoteRepository;
import cz.koscejev.yacrud.repository.CustomerRepository;

@SpringUI
public class VaadinUI extends UI {

	private VerticalLayout content;

	public VaadinUI(CustomerRepository customers, CustomerNoteRepository customerNotes) {
		CustomerGrid customerGrid = new CustomerGrid(customers);
		CustomerNoteGrid customerNoteGrid = new CustomerNoteGrid(customerNotes);
		CustomerNoteEditor customerNoteEditor = new CustomerNoteEditor(customerNotes);

		// propagate customer selection to customer note grid
		customerGrid.addSelectionListener(event -> {
			customerNoteGrid.setCustomer(event.getFirstSelectedItem().orElse(null));
			customerNoteEditor.setCustomer(event.getFirstSelectedItem().orElse(null));
		});

		customerNoteGrid.addSelectionListener(event ->
			customerNoteEditor.setNote(event.getFirstSelectedItem().orElse(null)));

		customerNoteEditor.addSaveListener(event ->
			customerNoteGrid.getDataProvider().refreshAll());

		Panel customerNotesPanel = new Panel("Customer Notes", customerNoteGrid);
		customerNotesPanel.setSizeFull();

		Panel customerNotePanel = new Panel("Customer Note", customerNoteEditor);
		customerNotePanel.setSizeFull();

		HorizontalLayout noteManagement = new HorizontalLayout();
		noteManagement.setMargin(false);
		noteManagement.addComponentsAndExpand(customerNotesPanel, customerNotePanel);

		content = new VerticalLayout();
		content.setMargin(true);
		content.setSizeFull();
		content.addComponentsAndExpand(
			new Panel("Customers", customerGrid),
			noteManagement);
	}

	@Override
	protected void init(VaadinRequest request) {
		setContent(content);
	}
}
