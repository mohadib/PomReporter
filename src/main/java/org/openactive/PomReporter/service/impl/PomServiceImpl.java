package org.openactive.PomReporter.service.impl;

import org.openactive.PomReporter.dao.ProjectInfoDAO;
import org.openactive.PomReporter.domain.Project;
import org.openactive.PomReporter.domain.ProjectInfo;
import org.openactive.PomReporter.service.PomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;

/**
 * Created by mohadib on 1/24/17.
 */
@Service
public class PomServiceImpl implements PomService
{
  @Autowired
  private ProjectInfoDAO projectSvnInfoDAO;

  @Override
  public void parsePom(Project project) throws Exception
  {
    ProjectInfo info = project.getProjectInfo();
    File svnProjectDir = new File( info.getFilePath() );
    File pom = new File( svnProjectDir, "pom.xml");

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse( pom );
    XPathFactory xPathfactory = XPathFactory.newInstance();
    XPath xpath = xPathfactory.newXPath();
    XPathExpression expr = xpath.compile( project.getXpathExpression() );
    NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
    String xpathResult = nl.item( 0 ).getNodeValue();

    info.setXpathResult( xpathResult );
    projectSvnInfoDAO.save( info );
  }
}
