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
package com.andrearichiardi.eclipse.addons.di.context.internal;

import java.util.Collections;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.InjectionException;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.andrearichiardi.eclipse.addons.di.Invoke;
import com.andrearichiardi.eclipse.addons.di.ScopedObjectFactory;

/**
 * Context function to create requested object factory
 */
@SuppressWarnings("restriction")
public class ScopedObjectFactoryFunction extends ContextFunction {
	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		IEventBroker eventBroker = context.get(IEventBroker.class);
		return new ScopedObjectFactoryImpl(context,eventBroker);
	}
	
	static class ScopedObjectFactoryImpl implements ScopedObjectFactory {
		private final IEclipseContext context;
		private final IEventBroker eventBroker;
		
		ScopedObjectFactoryImpl(IEclipseContext context, IEventBroker eventBroker) {
			this.context = context;
			this.eventBroker = eventBroker;
		}
		
		@Override
		public <C> C newInstance(Class<C> c) {
			return ContextInjectionFactory.make(c, this.context);
		}

		@Override
		public @NonNull ScopedObjectFactory createChild(@NonNull String name) {
			return new ScopedObjectFactoryImpl(this.context.createChild(name),this.eventBroker);
		}
		
		@SuppressWarnings("null")
		@Override
		public <O> void put(@NonNull Class<O> key, @NonNull O value) {
			this.context.set(key, value);
			if( this.eventBroker != null ) {
				this.eventBroker.send(ScopedObjectFactory.KEYMODIFED_TOPIC, Collections.singletonMap(key.toString(), value));
			}
		}
		
		@Override
		public void put(@NonNull String key, @NonNull Object value) {
			this.context.set(key, value);
			if( this.eventBroker != null ) {
				this.eventBroker.send(ScopedObjectFactory.KEYMODIFED_TOPIC, Collections.singletonMap(key, value));
			}
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public @Nullable
		<O> O invoke(@NonNull Object instance) throws IllegalStateException {
			try {
				return (O) ContextInjectionFactory.invoke(instance, Invoke.class, this.context);
			} catch(InjectionException e) {
				throw new IllegalStateException(e);
			}
			
		}
		
		@Override
		public void dispose() {
			this.context.dispose();
		}
	}
}
