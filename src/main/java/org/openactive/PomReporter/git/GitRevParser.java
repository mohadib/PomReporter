package org.openactive.PomReporter.git;

import org.eclipse.jgit.revwalk.RevCommit;
import org.openactive.PomReporter.domain.Project;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class GitRevParser
{
   private String hash;
   private String gitlabCommitUrl;
   private String gitlabMRUrl;
   private String commitDate;
   private String projectBaseUrl;
   private String shortComment;
   private final String commitUrlTemplate = "<a target='_blank' href='%s/commit/%s'>commit</a>";
   private final String mrUrlTemplate = "<a target='_blank' href='%s/merge_requests/%s'><img src='/gl16.png'></a>";

   public void parse( RevCommit commit , Project project )
   {
      hash = commit.getId().getName();
      shortComment = commit.getShortMessage();
      projectBaseUrl = project.getUrl().substring(0, project.getUrl().lastIndexOf('.'));
      gitlabCommitUrl = String.format( commitUrlTemplate, projectBaseUrl, hash );
      Instant commitTime = Instant.ofEpochSecond( commit.getCommitTime() );

      DateTimeFormatter formatter =
        DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
          .withLocale( Locale.ENGLISH )
          .withZone( ZoneId.systemDefault() );

      commitDate = formatter.format( commitTime );

      parseMergeRequest( commit, project );
   }

   private void parseMergeRequest( RevCommit commit, Project project )
   {
      String msg = commit.getFullMessage();
      if(msg.contains( "See merge request !" ) )
      {
         String mid = msg.substring(msg.lastIndexOf('!') + 1);
         gitlabMRUrl = String.format( mrUrlTemplate, projectBaseUrl, mid );
      }
   }

   public String getDisplayedMessage()
   {
      if( gitlabMRUrl != null  )
      {
         return shortComment + " " + gitlabMRUrl;
      }
      return shortComment;
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

   public String getGitlabMRUrl()
   {
      return gitlabMRUrl;
   }
}
