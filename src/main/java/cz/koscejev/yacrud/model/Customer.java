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
import javax.persistence.PrePersist;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Customer {

	@Id
	@GeneratedValue
	private Long customerId;
	@Column(updatable = false)
	private Instant createdOn;
	private CustomerStatus status;
	private String name;
	private String contact;

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
		return o instanceof Customer
			&& customerId != null
			&& customerId.equals(((Customer) o).customerId);
	}

	@Override
	public int hashCode() {
		return customerId != null ? customerId.hashCode() : super.hashCode();
	}
}
