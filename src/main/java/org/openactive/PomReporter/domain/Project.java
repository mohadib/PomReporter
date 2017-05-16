package org.openactive.PomReporter.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "PROJECT")
public class Project
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, unique = true)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  private VCSCredential credentials;

  /**
   *  This is the url to the project *without* the username.
   *  i.e. not https://user@git.foo.org/connect/connect.git
   *
   *  Example for git: https://git.foo.org/connect/connect.git
   *  Example for svn: http://subversion.foo.org/svn/carlx/CarlX/
   */
  @Basic(optional = false)
  private String url;

  /**
   * Used with git when tracking a branch besides master
   */
  @Basic
  private String branch;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  private ProjectGroup projectGroup;

  @OneToOne(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private ProjectInfo projectInfo;

  @Column(nullable = false)
  private String xpathExpression;

  @Column(nullable = false, length = 100, unique = true)
  private String name;

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public VCSCredential getCredentials()
  {
    return credentials;
  }

  public void setCredentials(VCSCredential credentials)
  {
    this.credentials = credentials;
  }

  public ProjectGroup getProjectGroup()
  {
    return projectGroup;
  }

  public void setProjectGroup(ProjectGroup projectGroup)
  {
    this.projectGroup = projectGroup;
  }

  public String getXpathExpression()
  {
    return xpathExpression;
  }

  public void setXpathExpression(String xpathExpression)
  {
    this.xpathExpression = xpathExpression;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public ProjectInfo getProjectInfo()
  {
    return projectInfo;
  }

  public void setProjectInfo(ProjectInfo projectInfo )
  {
    this.projectInfo = projectInfo;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl( String url )
  {
    this.url = url;
  }

  public String getBranch()
  {
    return branch;
  }

  public void setBranch( String branch )
  {
    this.branch = branch;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof Project))
      return false;

    Project project = (Project) o;

    return getName().equals(project.getName());
  }

  @Override
  public int hashCode()
  {
    return getName().hashCode();
  }
}
