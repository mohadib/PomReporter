package org.openactive.PomReporter.controller;

import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.SvnCredential;
import org.openactive.PomReporter.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Created by mohadib on 2/2/17.
 */
@RestController
@RequestMapping("/projects")
public class ProjectController
{
  @Autowired
  private ProjectDAO projectDAO;
  @Autowired
  private DeleteService deleteService;

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") Integer id) throws Exception
  {
    Project project = projectDAO.findOne(id);
    deleteService.deleteProject(project);
  }

  @GetMapping
  public List<Project> getAll()
  {
    return projectDAO.findAll();
  }

  @PostMapping
  public Project post(@RequestBody Project project)
  {
    return projectDAO.save(project);
  }

  @PatchMapping
  public Project patch(@RequestBody Project project )
  {
    Project orig = projectDAO.findOne(project.getId());
    orig.setPath( project.getPath() );
    orig.setName( project.getName());
    orig.setXpathExpression( project.getXpathExpression() );
    return projectDAO.save( orig );
  }

  @GetMapping("/{id}")
  public Project get( @PathVariable("id") Integer id)
  {
    Project project = projectDAO.findOne(id);
    if (project == null)
    {
      throw new EntityNotFoundException();
    }
    return project;
  }
}
