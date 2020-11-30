package com.cms.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="approval_path")
public class ApprovalPath {
	private static final long serialVersionUID = 1L;

//	@Id
//	@OneToOne
//	private Content content;

//	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinTable(name = "content_approval_path",
//			joinColumns = @JoinColumn(name = "content_id", referencedColumnName = "id"),
//			inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
//	@Column(nullable = false)
//	private Collection<User> users;
}
