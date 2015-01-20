package com.andrearichiardi.eclipse.addons;

import com.andrearichiardi.eclipse.addons.di.adapter.AdapterProvider;
import com.andrearichiardi.eclipse.addons.di.adapter.AdapterService.ValueAccess;

/**
 *
 */
public class StringDoubleProvider implements AdapterProvider<String, Double> {

    @Override
    public Class<String> getSourceType() {
        return String.class;
    }

    @Override
    public Class<Double> getTargetType() {
        return Double.class;
    }

    @Override
    public boolean canAdapt(String sourceObject, Class<Double> targetType) {
        try {
            Double.parseDouble(sourceObject);
            return true;
        } catch(Exception e) {
            // nothing
        }

        return false;
    }

    @Override
    public Double adapt(String sourceObject, Class<Double> targetType, ValueAccess... valueAccess) {
        return Double.valueOf(sourceObject);
    }
}