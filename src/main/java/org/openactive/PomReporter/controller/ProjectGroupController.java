package org.openactive.PomReporter.controller;

import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.ProjectGroupDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
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


  @PostMapping
  public ProjectGroup post(@RequestBody ProjectGroup projectGroup)
  {
    return projectGroupDAO.save(projectGroup);
  }

  @PatchMapping
  public ProjectGroup patch(@RequestBody ProjectGroup projectGroup)
  {
    ProjectGroup orig = projectGroupDAO.findOne(projectGroup.getId());
    orig.setName(projectGroup.getName());

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

    // make sure all link in projectGroup are valid
    for( Project p : projectGroup.getProjects() )
    {
      p.setProjectGroup(orig);
      projectDAO.save(p);
    }
    return projectGroupDAO.save(orig);
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
