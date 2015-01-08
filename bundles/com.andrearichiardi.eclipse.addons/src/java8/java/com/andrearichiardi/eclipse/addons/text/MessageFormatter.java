/*******************************************************************************
 * Copyright (c) 2014 BestSolution.at and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Tom Schindl<tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package com.andrearichiardi.eclipse.addons.text;

import java.util.function.Function;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Factory to create message formatter
 *
 * @since 1.1.0
 */
public class MessageFormatter {

	/**
	 * Create a formatter function
	 *
	 * @param dataProvider
	 *            provides the dynamic data
	 * @param formatProvider
	 *            provides the dynamic formatters
	 * @return a formatting function
	 */
	public static @NonNull Function<Object, String> create(@NonNull Function<String, Object> dataProvider, @NonNull Function<String, Formatter<?>> formatProvider) {
	    return (Object o) -> replace(o.toString(), dataProvider, formatProvider);
	}

	/**
	 * Given a message to be formatted containing a placeholder in the form <code>${dataKey, formatterKey, format}</code>
	 * and two lookup functions (key->data and key->formatter), applies the formatting replacing the formatted data
	 * within the message itself.<br/> <br/>
	 * This function replaces StrSubstitutor's functionality. It has been adapted from
     * <a href="http://stackoverflow.com/questions/14044715/strsubstitutor-replacement-with-jre-libraries">this</a>
     * Stack Overflow post.
	 * @param str
	 * @return
	 */
	public static String replace(String str, @NonNull Function<String, Object> dataProvider, @NonNull Function<String, Formatter<?>> formatProvider) {
        StringBuilder sb = new StringBuilder();
        char[] strArray = str.toCharArray();
        int i = 0;
        while (i < strArray.length - 1) {
            if (strArray[i] == '$' && strArray[i + 1] == '{') {
                i = i + 2;
                int begin = i;
                while (strArray[i] != '}') ++i;
                
                String subValue = str.substring(begin, i++);
                String key = subValue.substring(0, subValue.indexOf(','));
                String formatData = subValue.substring(subValue.indexOf(',') + 1);
                String formatter = formatData.substring(0, formatData.indexOf(','));
                String format = formatData.substring(formatData.indexOf(',') + 1);
                
                Object object = dataProvider.apply(key);
                @SuppressWarnings("unchecked")
                Formatter<Object> formatterInstance = (Formatter<Object>) formatProvider.apply(formatter);
                if (formatterInstance == null) {
                    return object + ""; //$NON-NLS-1$
                }
                
                String formatted = formatterInstance.format(object, format);
                
                sb.append(formatted);
            } else {
                sb.append(strArray[i]);
                ++i;
            }
        }
        if(i<strArray.length) sb.append(strArray[i]);
        return sb.toString();
    }
}
