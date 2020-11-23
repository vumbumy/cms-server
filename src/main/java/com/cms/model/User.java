package com.cms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.AccessLevel;
import lombok.Builder;
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
import java.util.stream.Collectors;

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

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@JsonSetter
	@Column(nullable = false)
	private String password = "P@ssw0rd";

	public User(String email){
		this.email = email;
	}

	public User(String email, Group group){
		this.email = email;
		this.addGroup(group);
	}

	public User(String email, Group group, Set<GroupRoles.Role> roles){
		this.email = email;
		this.addGroupRoles(group, roles);
	}

	public User(String email, Group group, GroupRoles.Role role){
		this.email = email;
		this.addGroupRole(group, role);
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_group_roles",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "group_roles_id", referencedColumnName = "id"))
	@Column(nullable = false)
	private Collection<GroupRoles> groupRolesList;

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

		return this.groupRolesList.add(new GroupRoles(group));
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

	private Boolean addGroupRoles(Group group, Set<GroupRoles.Role> roles) {
		if(this.groupRolesList == null){
			this.groupRolesList = new HashSet<>();
		}

		for(GroupRoles groupRoles : this.groupRolesList){
			if(groupRoles.isEqualGroup(group)) {
				for(GroupRoles.Role role : roles)
					if(!groupRoles.addRole(role))
						return false;

				return true;
			}
		}

		return this.groupRolesList.add(
				new GroupRoles(group, roles)
		);
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
				new GroupRoles(group, role)
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
	public Boolean isAdmin(Group group){
		if(this.isSuperAdmin())
			return true;

		if(this.groupRolesList == null)
			return false;

		for(GroupRoles groupRoles : this.groupRolesList){
			if(groupRoles.isEqualGroup(group) && groupRoles.roleContains(GroupRoles.Role.ADMIN))
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
	public Set<GroupRoles.Role> getRoles(){
		if(this.groupRolesList == null)
			return null;

		Set<GroupRoles.Role> roles = new HashSet<>();
		for(GroupRoles groupRoles : this.groupRolesList){
			roles.addAll(groupRoles.getRoles());
		}

		return roles;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public Collection<SimpleGrantedAuthority> getAuthorities() {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		for(GroupRoles.Role role : this.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return authorities;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public String getUsername() {
		return email;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
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
