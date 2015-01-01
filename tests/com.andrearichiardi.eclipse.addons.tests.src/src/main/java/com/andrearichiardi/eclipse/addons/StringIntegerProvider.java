package com.andrearichiardi.eclipse.addons;

import com.andrearichiardi.eclipse.addons.adapter.AdapterProvider;
import com.andrearichiardi.eclipse.addons.adapter.AdapterService.ValueAccess;

/**
 *
 */
public class StringIntegerProvider implements AdapterProvider<String, Integer> {

	@Override
	public Class<String> getSourceType() {
		return String.class;
	}

	@Override
	public Class<Integer> getTargetType() {
		return Integer.class;
	}

	@Override
	public boolean canAdapt(String sourceObject, Class<Integer> targetType) {
		try {
			Integer.parseInt(sourceObject);
			return true;
		} catch(Exception e) {
			// nothing
		}

		return false;
	}

	@Override
	public Integer adapt(String sourceObject, Class<Integer> targetType, ValueAccess... valueAccess) {
		return Integer.valueOf(sourceObject);
	}
}