package org.openactive.PomReporter.exceptions;

public class VCSProviderException extends Exception
{
	public VCSProviderException( String message )
	{
		super(message);
	}

	public VCSProviderException( Exception e )
	{
		super(e);
	}
}
