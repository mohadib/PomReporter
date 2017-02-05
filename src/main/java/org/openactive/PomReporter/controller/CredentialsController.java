package org.openactive.PomReporter.controller;

import org.openactive.PomReporter.service.DeleteService;
import org.openactive.PomReporter.util.EncyrptionUtil;
import org.openactive.PomReporter.controller.error.RestError;
import org.openactive.PomReporter.dao.SvnCredenitalDAO;
import org.openactive.PomReporter.domain.SvnCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by jdavis on 1/31/17.
 */
@RestController
@RequestMapping("/credentials")
public class CredentialsController
{
  @Autowired
  private SvnCredenitalDAO credenitalDAO;

  @Autowired
  private DeleteService deleteService;

  @Value("${encryption.secret}")
  private String secret;

  @Value("${encryption.salt}")
  private String salt;

  @DeleteMapping("/{id}")
  public void delete( @PathVariable("id") Integer id) throws Exception
  {
    SvnCredential credential = credenitalDAO.findOne(id);
    deleteService.deleteCredentials(credential);
  }

  @GetMapping
  public List<SvnCredential> getAll()
  {
    List<SvnCredential> creds = credenitalDAO.findAll();
    return creds;
  }

  @GetMapping("/{id}")
  public SvnCredential get(@PathVariable("id") Integer id)
  {
    SvnCredential cred = credenitalDAO.findByIdAndFetchProjectsEagerly(id);
    if (cred == null)
    {
      throw new EntityNotFoundException();
    }
    return cred;
  }

  @PatchMapping
  public SvnCredential patch(@RequestBody SvnCredential credential) throws UnsupportedEncodingException, GeneralSecurityException
  {
    SvnCredential orig = credenitalDAO.findByIdAndFetchProjectsEagerly(credential.getId());
    orig.setName(credential.getName());
    orig.setProtocol(credential.getProtocol());
    orig.setPort(credential.getPort());
    orig.setHost(credential.getHost());
    orig.setUsername(credential.getUsername());
    String encPass = new EncyrptionUtil().encrypt(credential.getPassword(), secret.toCharArray(), salt.getBytes("UTF-8"));
    orig.setPassword(encPass);
    return credenitalDAO.save(orig);
  }

  @PostMapping
  public SvnCredential post(@RequestBody SvnCredential credential) throws UnsupportedEncodingException, GeneralSecurityException
  {
    String encPass = new EncyrptionUtil().encrypt(credential.getPassword(), secret.toCharArray(), salt.getBytes("UTF-8"));
    credential.setPassword(encPass);
    return credenitalDAO.save(credential);
  }
}
