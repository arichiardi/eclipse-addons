/*******************************************************************************
 * Copyright (c) 2012 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package com.andrearichiardi.eclipse.addons.log;

import javax.inject.Provider;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Concreate implementation for a logger factory using {@link java.util.logging.Logger}
 */
public class JUtilLoggerFactory implements LoggerFactory, Provider<LoggerFactory> {

	@Override
	public LoggerFactory get() {
		return this;
	}
	
	@Override
	public @NonNull Logger createLogger(@NonNull String name) {
		return new LoggerImpl(name);
	}

	static class LoggerImpl implements Logger {
		private java.util.logging.Logger logger;
		
		private String name;
		
		public LoggerImpl(String name) {
			this.name = name;
		}
		
		private java.util.logging.Logger getLogger() {
			if( this.logger == null ) {
				this.logger = java.util.logging.Logger.getLogger(this.name);
			}
			return this.logger;
		}
		
		private static java.util.logging.Level toLogLevel(Level level) {
			switch (level) {
			case TRACE:
				return java.util.logging.Level.FINEST;
			case DEBUG:
				return java.util.logging.Level.FINE;
			case INFO:
				return java.util.logging.Level.INFO;
			case WARNING:
				return java.util.logging.Level.WARNING;
			case ERROR:
				return java.util.logging.Level.SEVERE;
			case FATAL:
				return java.util.logging.Level.SEVERE;
			default:
				return java.util.logging.Level.SEVERE;
			}
		}
		
		private void logInternal(Level level, String message, Throwable t) {
			
			java.util.logging.Level jlevel = toLogLevel(level);
			
			StackTraceElement frame = null;
			StackTraceElement[] e  = Thread.currentThread().getStackTrace();
			if (e.length > 2) {
				for (int i = 2; i < e.length; i++) {
					if (getClass().getName().equals(e[i].getClassName())) {
						continue;
					}
					else {
						frame = e[i];
						break;
					}
				}
			}
			
			if (t == null) {
				if (frame != null) {
					getLogger().logp(jlevel, frame.getClassName(), frame.getMethodName(), message);
				}
				else {
					getLogger().log(jlevel, message);
				}
			} else {
				if (frame != null) {
					getLogger().logp(jlevel, frame.getClassName(), frame.getMethodName(), message, t);
				}
				else {
					getLogger().log(jlevel, message, t);
				}
			}
			
		}
		
		@Override
		public void log(@NonNull Level level, @NonNull String message) {
			if (!isEnabled(level)) {
				return;
			}
			logInternal(level, message, null);
		}
		
		@Override
		public void logf(@NonNull Level level, @NonNull String pattern, @NonNull Throwable t, Object... args) {
			if (!isEnabled(level)) {
				return;
			}
			logInternal(level, String.format(pattern, args), t);
		}
		
		@Override
		public void logf(@NonNull Level level, @NonNull String pattern, Object... args) {
			if (!isEnabled(level)) {
				return;
			}
			logInternal(level, String.format(pattern, args), null);
		}
		
		@Override
		public void log(@NonNull Level level, @NonNull String message, @NonNull Throwable t) {
			if (!isEnabled(level)) {
				return;
			}
			logInternal(level, message, t);
		}

		@Override
		public void trace(@NonNull String message) {
			if (!isEnabled(Level.TRACE)) {
				return;
			}
			logInternal(Level.TRACE, message, null);
		}
		
		@Override
		public void trace(@NonNull String message, @NonNull Throwable t) {
			if (!isEnabled(Level.TRACE)) {
				return;
			}
			logInternal(Level.TRACE, message, t);
		}

		@Override
		public void debug(@NonNull String message) {
			if (!isEnabled(Level.DEBUG)) {
				return;
			}
			logInternal(Level.DEBUG, message, null);
		}
		
		@Override
		public void debug(@NonNull String message, @NonNull Throwable t) {
			if (!isEnabled(Level.DEBUG)) {
				return;
			}
			logInternal(Level.DEBUG, message, t);
		}

		@Override
		public void info(@NonNull String message) {
			if (!isEnabled(Level.INFO)) {
				return;
			}
			logInternal(Level.INFO, message, null);
		}

		
		@Override
		public void info(@NonNull String message, @NonNull Throwable t) {
			if (!isEnabled(Level.INFO)) {
				return;
			}
			logInternal(Level.INFO, message, t);
		}

		@Override
		public void warning(@NonNull String message) {
			if (!isEnabled(Level.WARNING)) {
				return;
			}
			logInternal(Level.WARNING, message, null);
		}
		
		@Override
		public void warning(@NonNull String message, @NonNull Throwable t) {
			if (!isEnabled(Level.WARNING)) {
				return;
			}
			logInternal(Level.WARNING, message, t);
		}

		@Override
		public void error(@NonNull String message) {
			if (!isEnabled(Level.ERROR)) {
				return;
			}
			logInternal(Level.ERROR, message, null);
		}

		
		@Override
		public void error(@NonNull String message, @NonNull Throwable t) {
			if (!isEnabled(Level.ERROR)) {
				return;
			}
			logInternal(Level.ERROR, message, t);
		}

		@Override
		public void fatal(@NonNull String message) {
			if (!isEnabled(Level.FATAL)) {
				return;
			}
			logInternal(Level.FATAL, message, null);
		}
		
		@Override
		public void fatal(@NonNull String message, @NonNull Throwable t) {
			if (!isEnabled(Level.FATAL)) {
				return;
			}
			logInternal(Level.FATAL, message, t);
		}

		@Override
		public void tracef(@NonNull String pattern, Object... args) {
			if (!isEnabled(Level.TRACE)) {
				return;
			}
			logInternal(Level.TRACE, String.format(pattern, args), null);
		}
		
		@Override
		public void tracef(@NonNull String pattern, @NonNull Throwable t, Object... args) {
			if (!isEnabled(Level.TRACE)) {
				return;
			}
			logInternal(Level.TRACE, String.format(pattern, args), t);
		}
		
		@Override
		public void debugf(@NonNull String pattern, Object... args) {
			if (!isEnabled(Level.DEBUG)) {
				return;
			}
			logInternal(Level.DEBUG, String.format(pattern, args), null);
		}
		
		@Override
		public void debugf(@NonNull String pattern, @NonNull Throwable t, Object... args) {
			if (!isEnabled(Level.DEBUG)) {
				return;
			}
			logInternal(Level.DEBUG, String.format(pattern, args), t);
		}
		
		@Override
		public void infof(@NonNull String pattern, Object... args) {
			if (!isEnabled(Level.INFO)) {
				return;
			}
			logInternal(Level.INFO, String.format(pattern, args), null);
		}
		
		@Override
		public void infof(@NonNull String pattern, @NonNull Throwable t, Object... args) {
			if (!isEnabled(Level.INFO)) {
				return;
			}
			logInternal(Level.INFO, String.format(pattern, args), t);
		}
		
		@Override
		public void warningf(@NonNull String pattern, Object... args) {
			if (!isEnabled(Level.WARNING)) {
				return;
			}
			logInternal(Level.WARNING, String.format(pattern, args), null);
		}
		
		@Override
		public void warningf(@NonNull String pattern, @NonNull Throwable t, Object... args) {
			if (!isEnabled(Level.WARNING)) {
				return;
			}
			logInternal(Level.WARNING, String.format(pattern, args), t);
		}
		
		@Override
		public void errorf(@NonNull String pattern, Object... args) {
			if (!isEnabled(Level.ERROR)) {
				return;
			}
			logInternal(Level.ERROR, String.format(pattern, args), null);
		}
		
		@Override
		public void errorf(@NonNull String pattern, @NonNull Throwable t, Object... args) {
			if (!isEnabled(Level.ERROR)) {
				return;
			}
			logInternal(Level.ERROR, String.format(pattern, args), t);
		}
		
		@Override
		public void fatalf(@NonNull String pattern, Object... args) {
			if (!isEnabled(Level.FATAL)) {
				return;
			}
			logInternal(Level.FATAL, String.format(pattern, args), null);
		}
		
		@Override
		public void fatalf(@NonNull String pattern, @NonNull Throwable t, Object... args) {
			if (!isEnabled(Level.FATAL)) {
				return;
			}
			logInternal(Level.FATAL, String.format(pattern, args), t);
		}
		
		@Override
		public boolean isEnabled(Level level) {
			return getLogger().isLoggable(toLogLevel(level));
		}		
	}
}