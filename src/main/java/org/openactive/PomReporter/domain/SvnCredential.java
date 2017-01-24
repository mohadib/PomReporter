package org.openactive.PomReporter.domain;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToMany;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Created by mohadib on 1/23/17.
 */
@Entity
@Table( name = "SvnCredential")
@JsonApiResource(type = "svncredentials")
@NoArgsConstructor
public class SvnCredential
{
  @JsonApiId
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY )
  @Column( nullable = false, unique = true )
  private Integer id;

  @JsonApiToMany( opposite = "credentials")
  @OneToMany( fetch = FetchType.LAZY , mappedBy="credentials")
  private List<Project> projects;

  @Column(nullable = false, length = 100)
  private String username;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false, length = 100)
  private String name;

  public SvnCredential(String username, String password, String name)
  {
    this.username = username;
    this.password = password;
    this.name = name;
  }

  public Integer getId()
  {
    return id;
  }

  public void setId( Integer id )
  {
    this.id = id;
  }

  public List<Project> getProjects()
  {
    return projects;
  }

  public void setProjects( List<Project> projects )
  {
    this.projects = projects;
  }

  public String getUsername()
  {
    return username;
  }

  public void setUsername( String username )
  {
    this.username = username;
  }

  public String getPassword()
  {
    return password;
  }

  public void setPassword( String password )
  {
    this.password = password;
  }

  public String getName()
  {
    return name;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  @Override
  public boolean equals( Object o )
  {
    if ( this == o )
      return true;
    if ( !( o instanceof SvnCredential ) )
      return false;

    SvnCredential that = (SvnCredential)o;

    return getName().equals( that.getName() );
  }

  @Override
  public int hashCode()
  {
    return getName().hashCode();
  }
}
