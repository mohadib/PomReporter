package org.openactive.PomReporter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table( name = "PROJECTINFO")
public class ProjectInfo
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY )
  @Column( nullable = false, unique = true )
  private Integer id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "lastUpdated" )
  private Date lastUpdated;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", nullable = false )
  private Date created;

  @JsonIgnore
  @OneToOne( optional = false )
  private Project project;

  @Column( nullable = false, unique = true)
  private String filePath;

  // will be null right after first checkout
  @Basic(optional = true )
  private String revision;

  // will be null right after first checkout
  @Basic( optional = true )
  private String xpathResult;

  @Column( columnDefinition="TEXT" )
  private String logEntries;

  @Basic
  private String projectUrl;

  public Integer getId()
  {
    return id;
  }

  public void setId(Integer id)
  {
    this.id = id;
  }

  public Date getLastUpdated()
  {
    return lastUpdated;
  }

  public void setLastUpdated(Date lastUpdated)
  {
    this.lastUpdated = lastUpdated;
  }

  public Project getProject()
  {
    return project;
  }

  public void setProject(Project project)
  {
    this.project = project;
  }

  public String getRevision()
  {
    return revision;
  }

  public void setRevision( String revision )
  {
    this.revision = revision;
  }

  public String getFilePath()
  {
    return filePath;
  }

  public void setFilePath(String filePath)
  {
    this.filePath = filePath;
  }

  public String getXpathResult()
  {
    return xpathResult;
  }

  public void setXpathResult(String xpathResult)
  {
    this.xpathResult = xpathResult;
  }

  public Date getCreated()
  {
    return created;
  }

  public void setCreated(Date created)
  {
    this.created = created;
  }

  public String getLogEntries()
  {
    return logEntries;
  }

  public void setLogEntries(String logEntries)
  {
    this.logEntries = logEntries;
  }

  public String getProjectUrl()
  {
    return projectUrl;
  }

  public void setProjectUrl(String projectUrl)
  {
    this.projectUrl = projectUrl;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (!(o instanceof ProjectInfo )) return false;

    ProjectInfo that = (ProjectInfo) o;

    return getFilePath().equals(that.getFilePath());
  }

  @Override
  public int hashCode()
  {
    return getFilePath().hashCode();
  }
}
