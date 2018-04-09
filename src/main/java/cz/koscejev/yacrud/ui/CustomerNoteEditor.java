package cz.koscejev.yacrud.ui;

import com.vaadin.event.EventRouter;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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
public class CustomerNoteEditor extends VerticalLayout {
	private final Label fieldCustomer;
	private final Label fieldCreatedOn;
	private final TextArea fieldText;
	private final EventRouter eventRouter = new EventRouter();

	private CustomerNote note;

	public CustomerNoteEditor(CustomerNoteRepository repo) {
		Component buttons = initButtonPanel(repo);
		addComponent(buttons);

		fieldCustomer = new Label();
		fieldCustomer.setCaption("Customer");
		addComponent(fieldCustomer);

		fieldCreatedOn = new Label();
		fieldCreatedOn.setCaption("Created on");
		addComponent(fieldCreatedOn);

		fieldText = new TextArea("Text");
		fieldText.setSizeFull();
		addComponent(fieldText);

		resetFields();
		setSizeFull();
		setExpandRatio(fieldCustomer, 0);
		setExpandRatio(fieldCreatedOn, 0);
		setExpandRatio(fieldText, 1);
		setExpandRatio(buttons, 0);
	}

	private Component initButtonPanel(CustomerNoteRepository repo) {
		Button buttonSave = new Button("Save");
		buttonSave.addClickListener(event -> {
			note.setText(fieldText.getValue());
			repo.save(note);
			eventRouter.fireEvent(new CustomerNoteSaveEvent(this, note));
		});

		Button buttonReset = new Button("Reset");
		buttonReset.addClickListener(event -> resetFields());

		return new HorizontalLayout(buttonSave, buttonReset);
	}

	private void resetFields() {
		if (note != null) {
			fieldCustomer.setValue(note.getCustomer() != null ? note.getCustomer().getName() : "");
			fieldCreatedOn.setValue(note.getCreatedOn() != null ? note.getCreatedOn().toString() : "");
			fieldText.setValue(note.getText() != null ? note.getText() : "");
		} else {
			fieldCustomer.setValue("");
			fieldCreatedOn.setValue("");
			fieldText.setValue("");
		}
		setEnabled(note != null);
	}

	public void setCustomer(Customer customer) {
		if (customer == null) {
			this.note = null;
		} else {
			this.note = CustomerNote.builder().customer(customer).build();
		}
		resetFields();
	}

	public void setNote(CustomerNote note) {
		if (note == null) {
			this.note = CustomerNote.builder().customer(this.note.getCustomer()).build();
		} else {
			this.note = note;
		}
		resetFields();
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
