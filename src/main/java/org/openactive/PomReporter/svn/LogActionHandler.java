package org.openactive.PomReporter.svn;

import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Created by mohadib on 1/24/17.
 */
public class LogActionHandler implements SvnActionHandler
{
  private ISVNLogEntryHandler logEntryHandler;

  public LogActionHandler(ISVNLogEntryHandler logEntryHandler)
  {
    this.logEntryHandler = logEntryHandler;
  }

  @Override
  public void handle(SvnActionContext context) throws SVNException
  {
    LocalDate ld = LocalDate.now().minusDays( 10 );
    Date date = Date.from(ld.atStartOfDay( ZoneId.systemDefault()).toInstant());

    SVNLogClient logClient = context.manager.getLogClient();
    logClient.doLog( new File[]{ context.svnProjectDir }, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNRevision.create( date ), true, false, 0, logEntryHandler);
  }
}
