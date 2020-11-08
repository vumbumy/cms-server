package com.cms.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;


@Entity
@Data
@NoArgsConstructor
@Table(name="group_roles")
public class GroupRoles {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @OneToOne
    private Group group;

    @ElementCollection
    @CollectionTable(name = "roles")
    private Collection<Role> roles;

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
