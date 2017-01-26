package org.openactive.PomReporter.domain;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiIncludeByDefault;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openactive.PomReporter.domain.Project;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jdavis on 1/24/17.
 */
@Entity
@Table( name = "projectgroup")
@JsonApiResource(type = "groups")
@NoArgsConstructor
public class ProjectGroup
{
	@JsonApiId
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column( nullable = false, unique = true )
	private Integer id;

	@Column( nullable = false, length = 100, unique = true)
	private String name;

	@JsonApiIncludeByDefault
	@JsonApiToMany( opposite = "projectGroup" )
	@OneToMany( fetch = FetchType.EAGER, mappedBy = "projectGroup")
	private List<Project> projects;

	public Integer getId()
	{
		return id;
	}

	public void setId( Integer id )
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public List<Project> getProjects()
	{
		return projects;
	}

	public void setProjects( List<Project> projects )
	{
		this.projects = projects;
	}

	@Override
	public boolean equals( Object o )
	{
		if ( this == o )
			return true;
		if ( !( o instanceof ProjectGroup ) )
			return false;

		ProjectGroup that = (ProjectGroup)o;

		return getName().equals( that.getName() );
	}

	@Override
	public int hashCode()
	{
		return getName().hashCode();
	}
}
