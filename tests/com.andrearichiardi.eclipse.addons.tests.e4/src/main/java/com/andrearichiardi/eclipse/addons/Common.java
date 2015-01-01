package com.andrearichiardi.eclipse.addons;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;

@SuppressWarnings("restriction")
public class Common {

	/**
	 * 
	 * @param context
	 */
	public static void pushLogger(IEclipseContext context) {
		Logger logger = new Logger() {

			@Override
			public void warn(Throwable t, String message) {
			}

			@Override
			public void trace(Throwable t, String message) {
			}

			@Override
			public boolean isWarnEnabled() {
				return false;
			}

			@Override
			public boolean isTraceEnabled() {
				return false;
			}

			@Override
			public boolean isInfoEnabled() {
				return false;
			}

			@Override
			public boolean isErrorEnabled() {
				return false;
			}

			@Override
			public boolean isDebugEnabled() {
				return false;
			}

			@Override
			public void info(Throwable t, String message) {
			}

			@Override
			public void error(Throwable t, String message) {
			}

			@Override
			public void debug(Throwable t, String message) {
			}

			@Override
			public void debug(Throwable t) {
			}
		};

		context.set(Logger.class, logger);
	}

	/**
	 * @param context
	 */
	public static void cleanLogger(IEclipseContext context) {
		context.declareModifiable(Logger.class);
		context.modify(Logger.class, null);
	}
}
