package cz.koscejev.yacrud.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import cz.koscejev.yacrud.repository.CustomerNoteRepository;
import cz.koscejev.yacrud.repository.CustomerRepository;

@SpringUI
public class VaadinUI extends UI {

	private VerticalLayout content;

	public VaadinUI(CustomerRepository customers, CustomerNoteRepository customerNotes) {
		CustomerGrid customerGrid = new CustomerGrid(customers);
		customerGrid.setSizeFull();

		CustomerNoteGrid customerNoteGrid = new CustomerNoteGrid(customerNotes);
		customerNoteGrid.setSizeFull();

		CustomerNoteEditor customerNoteEditor = new CustomerNoteEditor(customerNotes);
		customerNoteEditor.setSizeFull();

		// propagate customer selection to customer note grid
		customerGrid.addSelectionListener(event -> {
			customerNoteGrid.setCustomer(event.getFirstSelectedItem().orElse(null));
			customerNoteEditor.setCustomer(event.getFirstSelectedItem().orElse(null));
		});

		customerNoteGrid.addSelectionListener(event ->
			customerNoteEditor.setNote(event.getFirstSelectedItem().orElse(null)));

		customerNoteEditor.addSaveListener(event ->
			customerNoteGrid.refreshAll());

		HorizontalLayout noteManagement = new HorizontalLayout();
		noteManagement.setMargin(false);
		noteManagement.addComponentsAndExpand(customerNoteGrid, customerNoteEditor);

		this.content = new VerticalLayout();
		this.content.setMargin(true);
		this.content.setSizeFull();
		this.content.addComponentsAndExpand(customerGrid, noteManagement);
	}

	@Override
	protected void init(VaadinRequest request) {
		setContent(content);
	}
}
