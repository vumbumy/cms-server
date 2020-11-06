package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@Table(name="users")
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = 1L;
	public User() {}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Column(unique=true, nullable = false)
	public String email;

	@JsonIgnore
	@JsonSetter
	@Column(nullable = false)
	public String password = "P@ssw0rd";

	@JsonIgnore
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority("ROLE_NORMAL"));
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		return email;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isEnabled() {
		return true;
	}
}
