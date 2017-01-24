package org.openactive.PomReporter.domain;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Created by jdavis on 1/24/17.
 */
@Entity
@Table( name = "project")
@JsonApiResource(type = "projects")
@Data
@NoArgsConstructor
public class Project
{
	@JsonApiId
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column( nullable = false, unique = true )
	private Integer id;

	@JsonApiToOne( opposite = "projects")
	@ManyToOne( fetch = FetchType.EAGER )
	private SvnCredential credentials;

	@Column(nullable = false)
	private String url;

	@Column(nullable = false)
	private String xpathExpression;

	@Column(nullable = false, length = 100)
	private String name;
}
