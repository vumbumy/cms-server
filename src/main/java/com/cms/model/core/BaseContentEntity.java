package com.cms.model.core;

import com.cms.model.Permission;
import com.cms.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseContentEntity extends BaseEntity{
	private Long createdBy;
	private Long updatedBy;

	@Column
	@NotEmpty
	private String name;

	public BaseContentEntity(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public BaseContentEntity(Long createdBy, Long updatedBy) {
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
	}

//	public Boolean isAuthor(User user){
//		return this.createdBy.equals(user);
//	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(
			joinColumns = @JoinColumn(referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
	@Column(nullable = false)
	private Collection<Permission> permissions;

	public Boolean addPermission(Permission permission){
		if(this.permissions == null){
			this.permissions = new ArrayList<>();
		}

		return this.permissions.add(permission);
	}

	public Boolean deletePermission(Permission permission){
		if(this.permissions == null)
			return false;

		return this.permissions.remove(permission);
	}
}
