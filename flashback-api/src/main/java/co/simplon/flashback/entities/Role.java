package co.simplon.flashback.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_roles")
public class Role extends AbstractEntity {

    @Column(name = "role_name")
    private String roleName;

    public Role() {
    }

    public String getRoleName() {
	return roleName;
    }

    public void setRoleName(String name) {
	this.roleName = name;
    }

    @Override
    public int hashCode() {
	return Objects.hash(roleName);
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj instanceof Role other) {
	    return Objects.equals(roleName, other.roleName);
	}
	return false;
    }

    @Override
    public String toString() {
	return " {name=" + roleName + "}";
    }
}
