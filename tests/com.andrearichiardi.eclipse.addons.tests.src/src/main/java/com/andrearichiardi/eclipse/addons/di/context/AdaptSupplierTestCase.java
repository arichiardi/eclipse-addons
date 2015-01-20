/*******************************************************************************
 * Copyright (c) 2014 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package com.andrearichiardi.eclipse.addons.di.context;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.andrearichiardi.eclipse.addons.Common;
import com.andrearichiardi.eclipse.addons.StringDoubleProvider;
import com.andrearichiardi.eclipse.addons.StringIntegerProvider;
import com.andrearichiardi.eclipse.addons.di.adapter.Adapt;
import com.andrearichiardi.eclipse.addons.di.adapter.AdapterProvider;

/**
 * 
 */
public class AdaptSupplierTestCase {
	
	static class Bean {
		@Inject
		@Adapt("test")
		Integer integerValue;
		
		@Inject
		@Adapt("test")
		Double doubleValue;
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
		BundleContext bundleContext = FrameworkUtil.getBundle(getClass()).getBundleContext();
		
		bundleContext.registerService(AdapterProvider.class, new StringIntegerProvider(), null);
		bundleContext.registerService(AdapterProvider.class, new StringDoubleProvider(), null);
		
		serviceContext = EclipseContextFactory.getServiceContext(bundleContext);

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
	public void testAdapt() {
		
		serviceContext.set("test", "12");
		Bean bean = ContextInjectionFactory.make(Bean.class, serviceContext);
		
		Assert.assertEquals(12,bean.integerValue.intValue());
		Assert.assertEquals(12.0, bean.doubleValue.doubleValue(), 0.0);
		
		serviceContext.set("test", "14");

		Assert.assertEquals(14,bean.integerValue.intValue());
		Assert.assertEquals(14.0, bean.doubleValue.doubleValue(), 0.0);

		serviceContext.set("test", "15");

		Assert.assertEquals(15,bean.integerValue.intValue());
		Assert.assertEquals(15.0, bean.doubleValue.doubleValue(), 0.0);
	}
}
