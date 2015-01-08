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
package com.andrearichiardi.eclipse.addons.di;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;

import com.andrearichiardi.eclipse.addons.Callback;
import com.andrearichiardi.eclipse.addons.Subscription;
import com.andrearichiardi.eclipse.addons.adapter.Adaptable;

/**
 * A value bound to the IEclipseContext
 * 
 * @param <T>
 *            the type
 */
public interface ContextBoundValue<T> extends Adaptable {
	/**
	 * @return the current value
	 */
	@Nullable
	public T getValue();

	/**
	 * Publish the value back to the context
	 * 
	 * @param value
	 *            the value to publish
	 */
	public void publish(@Nullable T value);

	/**
	 * Subscribe for value change events
	 * 
	 * @param callback
	 *            the callback to be invoked
	 * @return the subscription to revoke it
	 */
	@NonNull
	public Subscription subscribeOnValueChange(@NonNull Callback<T> callback);

	/**
	 * Get informed about disposal
	 * 
	 * @param callback
	 *            the callback
	 * @return the subscription to revoke it
	 */
	@NonNull
	public Subscription subscribeOnDispose(@NonNull Callback<Void> callback);

	/**
	 * Allows to adapt the type to the given type.
	 * 
	 * By default the following adapters are available:
	 * <ul>
	 * <li>Eclipse Databinding - {@link org.eclipse.core.databinding.observable.value.IObservableValue}</li>
	 * </ul>
	 * 
	 * @param adapt
	 *            the type to adapt to
	 * @return the adapted type
	 * @see Adaptable#adaptTo(Class)
	 */
	@SuppressWarnings("javadoc")
	@Override
	@Nullable
	public <A> A adaptTo(@NonNull Class<A> adapt);

	/**
	 * Test if the instance can be adapted to the target
	 * 
	 * By default the following adapters are available:
	 * <ul>
	 * <li>Eclipse Databinding - {@link org.eclipse.core.databinding.observable.value.IObservableValue}</li>
	 * </ul>
	 * 
	 * @see Adaptable#canAdaptTo(Class)
	 */
	@Override
	@SuppressWarnings("javadoc")
	public boolean canAdaptTo(@NonNull Class<?> adapt);
}