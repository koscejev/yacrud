package cz.koscejev.yacrud.ui;

import com.vaadin.event.EventRouter;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.util.ReflectTools;
import cz.koscejev.yacrud.model.Customer;
import cz.koscejev.yacrud.model.CustomerNote;
import cz.koscejev.yacrud.repository.CustomerNoteRepository;
import lombok.Getter;

import java.util.EventObject;

/**
 * Panel for displaying and editing CustomerNote details.
 * Allows creating a new CustomerNote, if a CustomerNote is not loaded yet.
 */
public class CustomerNoteEditor extends CustomComponent {
	private final EventRouter eventRouter = new EventRouter();

	private final Panel panel;
	private final Label fieldCustomer;
	private final Label fieldCreatedOn;
	private final TextArea fieldText;

	private Customer customer;
	private CustomerNote note;

	public CustomerNoteEditor(CustomerNoteRepository repo) {
		VerticalLayout layout = new VerticalLayout();
		Component buttons = initButtonPanel(repo);
		layout.addComponent(buttons);

		fieldCustomer = new Label();
		fieldCustomer.setCaption("Customer");
		layout.addComponent(fieldCustomer);

		fieldCreatedOn = new Label();
		fieldCreatedOn.setCaption("Created on");
		layout.addComponent(fieldCreatedOn);

		fieldText = new TextArea("Text");
		fieldText.setSizeFull();
		layout.addComponent(fieldText);

		layout.setSizeFull();
		layout.setExpandRatio(fieldCustomer, 0);
		layout.setExpandRatio(fieldCreatedOn, 0);
		layout.setExpandRatio(fieldText, 1);
		layout.setExpandRatio(buttons, 0);

		panel = new Panel("Customer Note", layout);
		panel.setSizeFull();

		setCompositionRoot(panel);

		reset();
	}

	private Component initButtonPanel(CustomerNoteRepository repo) {
		Button buttonSave = new Button("Save");
		buttonSave.addClickListener(event -> {
			if (note == null) {
				note = new CustomerNote();
				note.setCustomer(customer);
			}
			note.setText(fieldText.getValue());
			repo.save(note);
			eventRouter.fireEvent(new CustomerNoteSaveEvent(this, note));
		});

		Button buttonReset = new Button("Reset");
		buttonReset.addClickListener(event -> reset());

		return new HorizontalLayout(buttonSave, buttonReset);
	}

	private void reset() {
		if (customer != null) {
			setEnabled(true);
			fieldCustomer.setValue(customer.getName());
		} else {
			setEnabled(false);
			fieldCustomer.setValue("");
		}

		if (note != null) {
			panel.setCaption("Edit Customer Note");
			fieldCreatedOn.setValue(note.getCreatedOn().toString());
			fieldText.setValue(note.getText());
		} else {
			panel.setCaption("Add Customer Note");
			fieldCreatedOn.setValue("");
			fieldText.setValue("");
		}
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		this.note = null;
		reset();
	}

	public void setNote(CustomerNote note) {
		this.note = note;
		reset();
	}

	public Registration addSaveListener(CustomerNoteSaveListener listener) {
		return eventRouter.addListener(CustomerNoteSaveEvent.class, listener,
			ReflectTools.getMethod(CustomerNoteSaveListener.class));
	}

	public interface CustomerNoteSaveListener {
		void onCustomerNoteSave(CustomerNoteSaveEvent event);
	}

	public class CustomerNoteSaveEvent extends EventObject {
		@Getter
		private final CustomerNote bean;

		private CustomerNoteSaveEvent(Object source, CustomerNote bean) {
			super(source);
			this.bean = bean;
		}
	}
}
