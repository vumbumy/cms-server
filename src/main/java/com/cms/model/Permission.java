package com.cms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private User user;

    @OneToOne
    private Group group;

    public Permission(User user, Group group, AccessRights accessRights) {
        this.user = user;
        this.group = group;
        this.rights = accessRights;
    }

    @AllArgsConstructor
    public enum AccessRights
    {
        READ_RIGHT    ((short)0),
        WRITE_RIGHT  ((short)1);

        @Getter
        private Short value;

        public Boolean isAccessRights(Short value){
            return this.value >= value;
        }

        public Boolean isAccessRights(AccessRights rights){
            return this.isAccessRights(rights.value);
        }
    }

    @Column(nullable = false)
    private AccessRights rights;

    @JsonIgnore
    public Boolean isReadable(){
        return this.rights.isAccessRights(AccessRights.READ_RIGHT);
    }

    @JsonIgnore
    public Boolean isWriteable(){
        return this.rights.isAccessRights(AccessRights.WRITE_RIGHT);
    }
}
