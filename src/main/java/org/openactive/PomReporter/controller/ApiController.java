package org.openactive.PomReporter.controller;

import org.openactive.PomReporter.dao.ProjectDAO;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.service.DeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by jdavis on 1/25/17.
 */
@Controller
public class ApiController
{
	@Autowired
	private DeleteService deleteService;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private SvnCredenitalDAO svnCredenitalDAO;

	@RequestMapping("/")
	@ResponseBody
	public String index()
	{
		return "wtf";
	}

	@RequestMapping( value = "/delete", method = RequestMethod.GET)
	@ResponseBody
	public String delete() throws Exception
	{
		//deleteService.deleteProject( projectDAO.findByName( "LS2P Trunk" ) );
		deleteService.deleteCredentials( svnCredenitalDAO.findByNameAndFetchProjectsEagerly( "TLC" ) );
		return "win";
	}
}
