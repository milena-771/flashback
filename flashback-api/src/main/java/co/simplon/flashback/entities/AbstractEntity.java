package co.simplon.flashback.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
abstract class AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    AbstractEntity() {
	// Required no-arg constructor
    }

    public Long getId() {
	return id;
    }

    @SuppressWarnings("unused")
    private void setId(Long id) {
	// Prevents from accidental assignment (set by DB)
	throw new UnsupportedOperationException();
    }

    /**
     * redefine equals and hashCode in all classes which extend AbstractEntity
     */
    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
