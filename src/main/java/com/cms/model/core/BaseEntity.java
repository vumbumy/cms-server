package com.cms.model.core;

import com.cms.model.Permission;
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
public class BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@CreationTimestamp
	private LocalDateTime createDateTime;

	@UpdateTimestamp
	private LocalDateTime updateDateTime;
}
