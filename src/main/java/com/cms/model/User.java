package com.cms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="users")
public class User implements Serializable, UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique=true, nullable = false)
	@Email
	private String email;

	@JsonIgnore
	@JsonSetter
	@Column(nullable = false)
	private String password = "P@ssw0rd";

	public User(String email){
		this.email = email;
	}

	public User(String email, GroupRoles groupRoles){
		this.email = email;
		this.groupRolesList = Collections.singleton(groupRoles);
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "user_group_roles",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "group_roles_id", referencedColumnName = "id"))
	@Column(nullable = false)
	private Set<GroupRoles> groupRolesList;

	public Boolean containsGroup(Group group){
		if(this.groupRolesList == null || this.groupRolesList.isEmpty())
			return true;

		for(GroupRoles groupRoles : this.groupRolesList){
			if(groupRoles.isEqualGroup(group))
				return true;
		}

		return false;
	}

	public Boolean addGroup(Group group){
		if(this.groupRolesList == null){
			this.groupRolesList = new HashSet<>();
		}

		for(GroupRoles groupRoles : this.groupRolesList){
			if(groupRoles.isEqualGroup(group))
				return true;
		}

		return this.groupRolesList.add(
				new GroupRoles(group)
		);
	}

	public Set<GroupRoles.Role> getGroupRoles(Group group){
		if(this.groupRolesList == null)
			return null;

		for(GroupRoles groupRoles : this.groupRolesList){
			if(groupRoles.isEqualGroup(group))
				return groupRoles.getRoles();
		}

		return null;
	}

	public Boolean addGroupRole(Group group, GroupRoles.Role role){
		if(this.groupRolesList == null){
			this.groupRolesList = new HashSet<>();
		}

		for(GroupRoles groupRoles : this.groupRolesList){
			if(groupRoles.isEqualGroup(group)) {
				return groupRoles.addRole(role);
			}
		}

		return this.groupRolesList.add(
				new GroupRoles(group)
		);
	}

	@JsonIgnore
	public HashMap<Group, Collection<GroupRoles.Role>> getGroupListMap(){
		if(this.groupRolesList == null)
			return null;

		HashMap<Group, Collection<GroupRoles.Role>> groupListMap = new HashMap<>();
		for(GroupRoles groupRoles : this.groupRolesList){
			groupListMap.put(groupRoles.getGroup(), groupRoles.getRoles());
		}

		return groupListMap;
	}

	@JsonIgnore
	public Boolean isSuperAdmin(){
		if(this.groupRolesList == null)
			return false;

		for(GroupRoles groupRoles : this.groupRolesList){
			if(groupRoles.roleContains(GroupRoles.Role.SUPER_ADMIN))
				return true;
		}

		return false;
	}

	@JsonIgnore
	public Set<Group> getGroups(){
		HashMap<Group, Collection<GroupRoles.Role>> groupListMap = this.getGroupListMap();
		if(groupListMap == null)
			return null;

		return groupListMap.keySet();
	}

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

//	@JsonIgnore
//	public boolean equals(User user){
//		if(user == null)
//			return false;
//
//		return this.id.equals(user.id);
//	}
}
