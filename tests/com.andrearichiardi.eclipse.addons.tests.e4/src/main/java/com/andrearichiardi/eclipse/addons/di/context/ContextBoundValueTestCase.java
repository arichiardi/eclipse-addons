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
package com.andrearichiardi.eclipse.addons.di.context;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.IValueChangeListener;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;

import com.andrearichiardi.eclipse.addons.Common;
import com.andrearichiardi.eclipse.addons.di.ContextBoundValue;
import com.andrearichiardi.eclipse.addons.di.ContextValue;

/**
 * ContextValue annotation tests.
 */
public class ContextBoundValueTestCase {
	
	/**
	 *
	 */
	public static class SimpleInject {
		/**
		 * 
		 */
		@Inject
		@ContextValue("simpleValue")
		public ContextBoundValue<String> value;
	}
	
	
	/**
	 *
	 */
	public static class ObservableInject {
		/**
		 * 
		 */
		@Inject
		@ContextValue("simpleValue")
		public ContextBoundValue<String> value;
		
		/**
		 * 
		 */
		public IObservableValue observableValue;

		/**
		 * 
		 */
		@Inject
		@Named("simpleValue")
		@Optional
		public String injectedValue;
		
		@PostConstruct
		void makeObservable() {
			this.observableValue = this.value.adaptTo(IObservableValue.class);
		}
	}
	
	/**
	 *
	 */
	public static class DirectObservableInject {
		/**
		 * 
		 */
		@Inject
		@ContextValue("simpleValue")
		public IObservableValue value;
	}
	
	/**
	 *
	 */
	public static class Target {
		/**
		 * 
		 */
		@Inject
		@Named("simpleValue")
		@Optional
		public String injectedValue;
		 
	}

	/**
	 * The test context
	 */
	private IEclipseContext serviceContext;

	/**
	 * 
	 */
	@Before
	public void initialize() {
		serviceContext = EclipseContextFactory.getServiceContext(
				FrameworkUtil.getBundle(getClass()).getBundleContext());

		Common.pushLogger(this.serviceContext);

	}

	/**
	 * 
	 */
	@After
	public void clean() {
		Common.cleanLogger(serviceContext);
	}

	/**
	 * 
	 */
	@Test
	public void testSimpleInjection() {
		SimpleInject simpleInject = ContextInjectionFactory.make(SimpleInject.class, serviceContext);
		Assert.assertNotNull(simpleInject.value);
	}
	
	/**
	 * 
	 */
	@Test
	public void testSimpleObservable() {
		ObservableInject obsInject = ContextInjectionFactory.make(ObservableInject.class, serviceContext);
		Assert.assertNotNull(obsInject.observableValue);
	}
	
	/**
	 * 
	 */
	@Test
	public void observableSupportModify() {
		serviceContext.declareModifiable("simpleValue"); //$NON-NLS-1$
		ObservableInject obsInject = ContextInjectionFactory.make(ObservableInject.class, serviceContext);
		Assert.assertNull(obsInject.injectedValue);
		
		{
			String uuid = UUID.randomUUID().toString();
			obsInject.observableValue.setValue(uuid);
			
			Assert.assertEquals(uuid,obsInject.injectedValue);
			Assert.assertEquals(uuid, obsInject.value.getValue());
		}
		
		final AtomicBoolean bool = new AtomicBoolean();
		final String uuid = UUID.randomUUID().toString();
		obsInject.observableValue.addValueChangeListener(new IValueChangeListener() {
			
			@Override
			public void handleValueChange(ValueChangeEvent event) {
				bool.set(uuid.equals(event.diff.getNewValue()));
			}
		});
		serviceContext.modify("simpleValue", uuid); //$NON-NLS-1$
		Assert.assertTrue(bool.get());
	}
	
	/**
	 * 
	 */
	@Test
	public void testContextModify() {
		serviceContext.declareModifiable("simpleValue"); //$NON-NLS-1$
		
		Target t = ContextInjectionFactory.make(Target.class, serviceContext);
		
		IEclipseContext c = serviceContext.createChild();
		SimpleInject i = ContextInjectionFactory.make(SimpleInject.class, c);
		String uuid = UUID.randomUUID().toString();
		i.value.publish(uuid);
		
		Assert.assertEquals(uuid, t.injectedValue);
	}
	
	/**
	 * 
	 */
	@Test
	public void testDirectObservable() {
		serviceContext.declareModifiable("simpleValue"); //$NON-NLS-1$
		Realm r = new Realm() {

			@Override
			public boolean isCurrent() {
				return true;
			}
			
		};
		serviceContext.set(Realm.class, r);
		DirectObservableInject directObservableInject = ContextInjectionFactory.make(DirectObservableInject.class,
				serviceContext);
		Assert.assertNotNull(directObservableInject.value);
		Assert.assertEquals(r, directObservableInject.value.getRealm());
	}
}
