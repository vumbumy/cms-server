package com.cms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="contents")
public class Content {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@OneToOne
	private User author;

	@Column
	@NotEmpty
	private String name;

	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "content_permissions",
			joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
	@Column(nullable = false)
	private Collection<Permission> permissions;

	public Boolean addPermission(Permission permission){
		if(this.permissions == null){
			this.permissions = new ArrayList<>();
		}

		return this.permissions.add(permission);
	}

	public Boolean deletePermission(Permission permission){
		return this.permissions.remove(permission);
	}
}
