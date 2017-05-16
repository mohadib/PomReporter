package org.openactive.PomReporter.domain;


import javax.persistence.*;
import java.util.List;

/**
 * Created by jdavis on 1/24/17.
 */
@Entity
@Table( name = "PROJECTGROUP")
public class ProjectGroup
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column( nullable = false, unique = true )
	private Integer id;

	@Column( nullable = false, length = 100, unique = true)
	private String name;

	@OneToMany( fetch = FetchType.EAGER, mappedBy = "projectGroup", cascade = CascadeType.PERSIST)
	private List<Project> projects;

	@Basic
	private boolean isDefault;

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

	public boolean isDefault()
	{
		return isDefault;
	}

	public boolean getIsDefault()
	{
		return isDefault;
	}

	public void setIsDefault( boolean aDefault )
	{
		isDefault = aDefault;
	}

	public void setDefault( boolean aDefault )
	{
		isDefault = aDefault;
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
