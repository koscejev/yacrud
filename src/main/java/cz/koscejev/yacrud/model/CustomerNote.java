package cz.koscejev.yacrud.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerNote {

	@Id
	@GeneratedValue
	private Long customerNoteId;
	@Column(updatable = false)
	private Instant createdOn;
	@ManyToOne
	private Customer customer;
	@Column(columnDefinition = "TEXT")
	private String text;

	@PrePersist
	private void prePersist() {
		if (createdOn == null) {
			createdOn = Instant.now();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		return o instanceof CustomerNote
			&& customerNoteId != null
			&& customerNoteId.equals(((CustomerNote) o).customerNoteId);
	}

	@Override
	public int hashCode() {
		return customerNoteId != null ? customerNoteId.hashCode() : super.hashCode();
	}
}
