package org.openactive.PomReporter.git;

import org.eclipse.jgit.revwalk.RevCommit;
import org.openactive.PomReporter.domain.Project;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * Created by jdavis on 5/16/17.
 */
public class GitRevParser
{
   private String hash;
   private String gitlabCommitUrl;
   private String commitDate;
   private final String commitUrlTemplate = "<a target='_blank' href='%s/commit/%s'>commit</a>";

   public void parse( RevCommit commit , Project project )
   {
      hash = commit.getId().getName();
      String projectBaseUrl = project.getUrl().substring(0, project.getUrl().lastIndexOf('.'));
      gitlabCommitUrl = String.format( commitUrlTemplate, projectBaseUrl, hash );
      Instant commitTime = Instant.ofEpochSecond( commit.getCommitTime() );

      DateTimeFormatter formatter =
        DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
          .withLocale( Locale.ENGLISH )
          .withZone( ZoneId.systemDefault() );

      commitDate = formatter.format( commitTime );
   }

   public String getHash()
   {
      return hash;
   }

   public String getGitlabCommitUrl()
   {
      return gitlabCommitUrl;
   }

   public String getCommitDate()
   {
      return commitDate;
   }
}
