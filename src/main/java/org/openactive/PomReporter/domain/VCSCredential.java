package org.openactive.PomReporter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table( name = "CREDENTIAL")
public class VCSCredential
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY )
  @Column( nullable = false, unique = true )
  private Integer id;

  @JsonIgnore
  @OneToMany( fetch = FetchType.LAZY , mappedBy="credentials", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<Project> projects;

  @Column(nullable = false, length = 100)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false, length = 100, unique = true)
  private String name;

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

  @JsonIgnore
  public String getPassword()
  {
    return password;
  }

  @JsonProperty
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
    if ( !( o instanceof VCSCredential ) )
      return false;

    VCSCredential that = (VCSCredential)o;

    return getName().equals( that.getName() );
  }

  @Override
  public int hashCode()
  {
    return getName().hashCode();
  }
}
