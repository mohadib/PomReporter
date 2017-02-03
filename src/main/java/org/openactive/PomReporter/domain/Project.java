package org.openactive.PomReporter.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by jdavis on 1/24/17.
 */
@Entity
@Table(name = "project")
public class Project
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false, unique = true)
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  private SvnCredential credentials;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  private ProjectGroup projectGroup;

  @OneToOne(mappedBy = "project", cascade = CascadeType.REMOVE, orphanRemoval = true)
  private ProjectSvnInfo svnInfo;

  @Basic(optional = false)
  private String path;

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

  public SvnCredential getCredentials()
  {
    return credentials;
  }

  public void setCredentials(SvnCredential credentials)
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

  public String getPath()
  {
    return path;
  }

  public void setPath(String path)
  {
    this.path = path;
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

  public ProjectSvnInfo getSvnInfo()
  {
    return svnInfo;
  }

  public void setSvnInfo(ProjectSvnInfo svnInfo)
  {
    this.svnInfo = svnInfo;
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
