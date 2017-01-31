package org.openactive.PomReporter.controller.error;

/**
 * Created by jdavis on 1/31/17.
 */
public class RestError
{
	private int code;
	private String msg;

	public RestError( int code, String msg )
	{
		this.code = code;
		this.msg = msg;
	}

	public int getCode()
	{
		return code;
	}

	public void setCode( int code )
	{
		this.code = code;
	}

	public String getMsg()
	{
		return msg;
	}

	public void setMsg( String msg )
	{
		this.msg = msg;
	}
}
