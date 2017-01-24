package org.openactive.PomReporter.domain;

import io.katharsis.resource.annotations.JsonApiId;
import io.katharsis.resource.annotations.JsonApiResource;
import io.katharsis.resource.annotations.JsonApiToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Created by mohadib on 1/23/17.
 */
@Entity
@Table( name = "SvnCredential")
@JsonApiResource(type = "svncredentials")
@Data
@NoArgsConstructor
public class SvnCredential
{
  @JsonApiId
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY )
  @Column( nullable = false, unique = true )
  private Integer id;

  @JsonApiToMany( opposite = "credentials")
  @OneToMany( fetch = FetchType.EAGER , mappedBy="credentials")
  private List<Project> projects;

  @Column(nullable = false, length = 100)
  private String username;

  @Column(nullable = false, length = 100)
  private String password;

  @Column(nullable = false, length = 100)
  private String name;

  public SvnCredential(String username, String password, String name)
  {
    this.username = username;
    this.password = password;
    this.name = name;
  }
}
