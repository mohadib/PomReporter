package org.openactive.PomReporter.controller;

import org.openactive.PomReporter.service.DeleteService;
import org.openactive.PomReporter.util.EncryptionUtil;
import org.openactive.PomReporter.dao.VCSCredenitalDAO;
import org.openactive.PomReporter.domain.VCSCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
@RequestMapping("/credentials")
public class CredentialsController
{
  @Autowired
  private VCSCredenitalDAO credenitalDAO;

  @Autowired
  private DeleteService deleteService;

  @Value("${encryption.secret}")
  private String secret;

  @Value("${encryption.salt}")
  private String salt;

  @DeleteMapping("/{id}")
  public void delete( @PathVariable("id") Integer id) throws Exception
  {
    VCSCredential credential = credenitalDAO.findOne(id);
    deleteService.deleteCredentials(credential);
  }

  @GetMapping
  public List<VCSCredential > getAll()
  {
    return credenitalDAO.findAll();
  }

  @GetMapping("/{id}")
  public VCSCredential get(@PathVariable("id") Integer id)
  {
    VCSCredential cred = credenitalDAO.findByIdAndFetchProjectsEagerly(id);
    if (cred == null)
    {
      throw new EntityNotFoundException();
    }
    return cred;
  }

  @PatchMapping
  public VCSCredential patch(@RequestBody VCSCredential credential) throws UnsupportedEncodingException, GeneralSecurityException
  {
    VCSCredential orig = credenitalDAO.findByIdAndFetchProjectsEagerly(credential.getId());
    orig.setName(credential.getName());
    orig.setUsername(credential.getUsername());
    String encPass = new EncryptionUtil().encrypt(credential.getPassword(), secret.toCharArray(), salt.getBytes("UTF-8" ) );
    orig.setPassword(encPass);
    return credenitalDAO.save(orig);
  }

  @PostMapping
  public VCSCredential post(@RequestBody VCSCredential credential) throws UnsupportedEncodingException, GeneralSecurityException
  {
    String encPass = new EncryptionUtil().encrypt(credential.getPassword(), secret.toCharArray(), salt.getBytes("UTF-8" ) );
    credential.setPassword(encPass);
    return credenitalDAO.save(credential);
  }
}
