package org.openactive.PomReporter.controller;

import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/projectgroups")
public class ProjectGroupController
{
  @Autowired
  private ProjectGroupDAO projectGroupDAO;
  @Autowired
  private ProjectDAO projectDAO;


  @DeleteMapping("/{id}")
  public void delete( @PathVariable("id") Integer id )
  {
    ProjectGroup pg = projectGroupDAO.findOne( id );
    for( Project project : pg.getProjects() )
    {
      project.setProjectGroup( null );
      projectDAO.save( project );
    }
    pg.getProjects().clear();
    projectGroupDAO.delete( pg.getId() );
  }

  @PostMapping
  public ProjectGroup post(@RequestBody ProjectGroup projectGroup)
  {
    List<Project> projects = new ArrayList<>( projectGroup.getProjects() );
    projectGroup.getProjects().clear();
    projectGroup = projectGroupDAO.save(projectGroup);
    for( Project p : projects )
    {
      p.setProjectGroup( projectGroup );
      projectDAO.save( p );
    }
    return projectGroupDAO.findOne( projectGroup.getId() );
  }

  @PatchMapping
  public ProjectGroup patch(@RequestBody ProjectGroup projectGroup)
  {
    ProjectGroup orig = projectGroupDAO.findOne(projectGroup.getId());

    if( projectGroup.isDefault() && !orig.isDefault() )
    {
      projectGroupDAO.removeDefault();
    }

    // remove old existing links that are not present in projectGroup now
    Iterator<Project> it = orig.getProjects().iterator();
    while(it.hasNext())
    {
      Project existing = it.next();
      if(!projectGroup.getProjects().contains(existing))
      {
        existing.setProjectGroup(null);
        projectDAO.save(existing);
      }
    }

    orig.getProjects().clear();
    orig.setName(projectGroup.getName());
    orig.setDefault(projectGroup.isDefault());
    orig = projectGroupDAO.save(orig);

    // make sure all link in projectGroup are valid
    for( Project p : projectGroup.getProjects() )
    {
      Project proj = projectDAO.findOne( p.getId() );
      proj.setProjectGroup( orig );
      projectDAO.save(proj);
    }

    return projectGroupDAO.findOne( orig.getId() );
  }

  @GetMapping
  public List<ProjectGroup> getAll()
  {
    return projectGroupDAO.findAll();
  }

  @GetMapping("/{id}")
  public ProjectGroup get(@PathVariable("id") Integer id)
  {
    ProjectGroup pg = projectGroupDAO.findOne(id);
    if( pg == null )
    {
      throw new EntityNotFoundException();
    }
    return pg;
  }
}
