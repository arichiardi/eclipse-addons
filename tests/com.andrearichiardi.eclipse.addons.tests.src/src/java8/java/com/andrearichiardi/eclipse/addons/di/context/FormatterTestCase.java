package com.andrearichiardi.eclipse.addons.di.context;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;

import com.andrearichiardi.eclipse.addons.Common;
import com.andrearichiardi.eclipse.addons.text.DateFormatter;
import com.andrearichiardi.eclipse.addons.text.Formatter;
import com.andrearichiardi.eclipse.addons.text.MessageFormatter;
import com.andrearichiardi.eclipse.addons.text.NumberFormatter;
import com.andrearichiardi.eclipse.addons.text.TemporalAccessorFormatter;

public class FormatterTestCase {

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

    @Inject
    private NumberFormatter numberFormatter;

    @Inject
    private DateFormatter dateFormatter;
    
    @Inject
    private TemporalAccessorFormatter temporalFormatter;
    
    private static final String NUMBER_FORMAT = "#,##0.00";
    private static final String DATE_FORMAT = "MMM/dd/yyyy";
    private static final String DATETIME_FORMAT = "d MMM uuuu (H:m:s)";
    
    private static final int AMOUNT1 = 20_000;
    private static final int AMOUNT2 = 200;
    private static final float AMOUNT3 = 0.12345F;
    
    
    @Test
    public void testNumberFormatter() {
        ContextInjectionFactory.inject(this, serviceContext);
        NumberFormat f = new DecimalFormat(NUMBER_FORMAT);
        
        String formatted1 = numberFormatter.format(AMOUNT1, NUMBER_FORMAT);
        Assert.assertEquals(formatted1, f.format(AMOUNT1));
        
        String formatted2 = numberFormatter.format(AMOUNT2, NUMBER_FORMAT);
        Assert.assertEquals(formatted2, f.format(AMOUNT2));
        
        String formatted3 = numberFormatter.format(AMOUNT3, NUMBER_FORMAT);
        Assert.assertEquals(formatted3, f.format(AMOUNT3));
    }
    
    @Test
    public void testDateFormatter() {
        ContextInjectionFactory.inject(this, serviceContext);
        DateFormat f = new SimpleDateFormat(DATE_FORMAT);
        Date now = new Date();
        String formatted1 = dateFormatter.format(now, DATE_FORMAT);
        Assert.assertEquals(formatted1, f.format(now));
    }
    
    @Test
    public void testTemporalAccessorFormatter() {
        ContextInjectionFactory.inject(this, serviceContext);
        DateTimeFormatter f = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        TemporalAccessor now = LocalDateTime.now();
        String formatted1 = temporalFormatter.format(now, DATETIME_FORMAT);
        Assert.assertEquals(formatted1, f.format(now));
    }
    
    @Test
    public void testMessageFormatter() {
        ContextInjectionFactory.inject(this, serviceContext);
        
        NumberFormat nf = new DecimalFormat(NUMBER_FORMAT);
        DateFormat df = new SimpleDateFormat(DATE_FORMAT);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATETIME_FORMAT);
        
        // Formatters
        Map<String,Formatter<?>> formatters = new HashMap<>();
        formatters.put("number", numberFormatter);
        formatters.put("date", dateFormatter);
        formatters.put("temporal", temporalFormatter);
        
        // Data
        Date dateNow = new Date();
        TemporalAccessor temporalNow = LocalDateTime.now();
        
        Map<String,Object> data = new HashMap<>();
        data.put("amount1", AMOUNT1);
        data.put("amount2", AMOUNT2);
        data.put("amount3", AMOUNT3);
        data.put("date1", dateNow);
        data.put("date2", temporalNow);
        
        String message1 = "The final amount is ${amount1,number," + NUMBER_FORMAT + "}";
        String formatted1 = MessageFormatter.create(data::get, formatters::get).apply(message1);
        Assert.assertEquals(formatted1, "The final amount is " + nf.format(AMOUNT1));
        
        String message2 = "The final amount is ${amount2,number," + NUMBER_FORMAT + "}";
        String formatted2 = MessageFormatter.create(data::get, formatters::get).apply(message2);
        Assert.assertEquals(formatted2, "The final amount is " + nf.format(AMOUNT2));
        
        String message3 = "The final amount is ${amount3,number," + NUMBER_FORMAT + "}";
        String formatted3 = MessageFormatter.create(data::get, formatters::get).apply(message3);
        Assert.assertEquals(formatted3, "The final amount is " + nf.format(AMOUNT3));
        
        String message4 = "The final date is ${date1,date," + DATE_FORMAT + "}";
        String formatted4 = MessageFormatter.create(data::get, formatters::get).apply(message4);
        Assert.assertEquals(formatted4, "The final date is " + df.format(dateNow));
        
        String message5 = "The final date is ${date2,temporal," + DATETIME_FORMAT + "}";
        String formatted5 = MessageFormatter.create(data::get, formatters::get).apply(message5);
        Assert.assertEquals(formatted5, "The final date is " + dtf.format(temporalNow));
    }
}
