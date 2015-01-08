/*******************************************************************************
 * Copyright (c) 2013 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package com.andrearichiardi.eclipse.addons.di.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.contexts.RunAndTrack;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.andrearichiardi.eclipse.addons.Callback;
import com.andrearichiardi.eclipse.addons.Subscription;
import com.andrearichiardi.eclipse.addons.adapter.AdapterService;
import com.andrearichiardi.eclipse.addons.adapter.AdapterService.ValueAccess;
import com.andrearichiardi.eclipse.addons.di.ContextBoundValue;
import com.andrearichiardi.eclipse.addons.di.ScopedObjectFactory;
import com.andrearichiardi.eclipse.addons.log.Log;
import com.andrearichiardi.eclipse.addons.log.Logger;

/**
 * Implementation of a ContextBoundValue
 * 
 * @param <T>
 *            the type
 */
public class EclipseContextBoundValue<T> implements ContextBoundValue<T> {
	@NonNull
	private IEclipseContext context;
	@Nullable
	private String contextKey;
	@Nullable
	List<Callback<T>> callbacks;
	@Nullable
	List<Callback<Void>> disposalCallbacks;
	@NonNull
	private AdapterService adapterService;
	@Nullable
	private T value;
	
	@Inject
	@Optional
	@Nullable
	IEventBroker eventBroker;
	
	@Inject
	@Log
	private Logger logger;

	/**
	 * Create a new bound value
	 * 
	 * @param context
	 *            the context
	 * @param adapterService
	 *            the adapter service
	 */
	@Inject
	public EclipseContextBoundValue(@NonNull IEclipseContext context, @NonNull AdapterService adapterService) {
		this.context = context;
		this.adapterService = adapterService;
	}

	/**
	 * Setting the context key
	 * 
	 * @param contextKey
	 *            the key
	 */
	public void setContextKey(@NonNull final String contextKey) {
		this.contextKey = contextKey;
		this.context.runAndTrack(new RunAndTrack() {

			@SuppressWarnings("unchecked")
			@Override
			public boolean changed(IEclipseContext context) {
				setCurrentValue((T) context.get(contextKey));
				return true;
			}
		});
	}

	@SuppressWarnings("unchecked")
	void setCurrentValue(@Nullable T o) {
		this.value = o;
		if (this.callbacks != null) {
			for (Callback<?> c : this.callbacks.toArray(new Callback<?>[0])) {
				try {
					((Callback<T>) c).call(o);	
				} catch(Throwable t) {
					this.logger.error("Failed while executing callback", t); //$NON-NLS-1$
				}
			}
		}
	}

	@Override
	@Nullable
	public T getValue() {
		return this.value;
	}

	@Override
	public void publish(@Nullable T value) {
		this.context.modify(this.contextKey, value);
		if( this.eventBroker != null ) {
			this.eventBroker.send(ScopedObjectFactory.KEYMODIFED_TOPIC, Collections.singletonMap(this.contextKey, value));
		}
	}

	@Override
	public Subscription subscribeOnValueChange(final Callback<T> callback) {
		if (this.callbacks == null) {
			this.callbacks = new ArrayList<Callback<T>>();
		}

		if (this.callbacks != null) {
			this.callbacks.add(callback);
		}

		return new Subscription() {

			@Override
			public void dispose() {
				List<Callback<T>> callbacks = EclipseContextBoundValue.this.callbacks;
				if (callbacks != null) {
					callbacks.remove(callback);
				}
			}
		};
	}

	@Override
	public Subscription subscribeOnDispose(final Callback<Void> callback) {
		if (this.disposalCallbacks == null) {
			this.disposalCallbacks = new ArrayList<Callback<Void>>();
		}
		if (this.disposalCallbacks != null) {
			this.disposalCallbacks.add(callback);
		}

		return new Subscription() {

			@Override
			public void dispose() {
				List<Callback<Void>> disposalCallbacks = EclipseContextBoundValue.this.disposalCallbacks;
				if (disposalCallbacks != null) {
					disposalCallbacks.remove(callback);
				}
			}
		};
	}

	@SuppressWarnings("null")
	@Override
	public <A> A adaptTo(@NonNull Class<A> adapt) {
		return this.adapterService.adapt(this, adapt, new ValueAccessImpl(this.context));
	}

	@Override
	public boolean canAdaptTo(Class<?> adapt) {
		return this.adapterService.canAdapt(this, adapt);
	}

	@PreDestroy
	void dispose() {
		List<Callback<Void>> disposalCallbacks = this.disposalCallbacks;
		if (disposalCallbacks != null) {
			for (Callback<?> callback : disposalCallbacks.toArray(new Callback<?>[0])) {
				try {
					callback.call(null);	
				} catch(Throwable t) {
					this.logger.error("Failure while executing clean up callback", t); //$NON-NLS-1$
				}
				
			}
			disposalCallbacks.clear();
		}
		if (this.callbacks != null) {
			this.callbacks.clear();
		}
		this.value = null;
	}

	static class ValueAccessImpl implements ValueAccess {
		private final IEclipseContext context;

		public ValueAccessImpl(IEclipseContext context) {
			this.context = context;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <O> O getValue(String key) {
			return (O) this.context.get(key);
		}

		@SuppressWarnings("null")
		@Override
		public <O> O getValue(@NonNull Class<O> key) {
			return this.context.get(key);
		}

	}
}