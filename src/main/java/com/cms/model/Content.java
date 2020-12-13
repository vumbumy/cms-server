package com.cms.model;

import com.cms.model.core.BaseContentEntity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="contents")
public class Content extends BaseContentEntity {
    public Content(User user){
        super(user.getId());
    }

    public Content(Long createdBy){
        super(createdBy);
    }
}
