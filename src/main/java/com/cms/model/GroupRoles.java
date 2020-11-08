package com.cms.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;


@Entity
@Data
@NoArgsConstructor
@Table(name="group_roles")
public class GroupRoles {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Group group;

    @ElementCollection
    @CollectionTable(name = "roles")
    private Collection<Role> roles;

    public GroupRoles(Group group, Role role) {
        this.group = group;
        this.addRole(role);
    }

    public Boolean addRole(Role role){
        if(this.roles == null)
            this.roles = new ArrayList<>();

        return this.roles.add(role);
    }

    public Boolean roleContains(Role role){
        if(this.roles == null)
            return false;

        return this.roles.contains(role);
    }

    @NoArgsConstructor
    public enum Role {
        USER(),
        ADVERTISER(),
        PUBLISHER(),
        ADMIN(),
        SUPER_ADMIN();

        public String getName(){
            return name();
        }
    }
}
