package nw104;

import org.apache.nifi.logging.ComponentLog;
import org.apache.nifi.logging.LogLevel;

public class MockLogger implements ComponentLog {

	@Override
	public void warn(String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(String msg, Object[] os) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(String msg, Object[] os, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void warn(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trace(String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trace(String msg, Object[] os) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trace(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void trace(String msg, Object[] os, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isWarnEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTraceEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInfoEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isErrorEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDebugEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void info(String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(String msg, Object[] os) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void info(String msg, Object[] os, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void error(String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String msg, Object[] os) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void error(String msg, Object[] os, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String msg, Object[] os) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String msg, Object[] os, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(String msg) {
		System.out.println(msg);
	}

	@Override
	public void log(LogLevel level, String msg, Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(LogLevel level, String msg, Object[] os) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(LogLevel level, String msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void log(LogLevel level, String msg, Object[] os, Throwable t) {
		// TODO Auto-generated method stub
		
	}

}
