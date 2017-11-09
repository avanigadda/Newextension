/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2017 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.techouts.customdynamicattribute;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;


/**
 *
 */
public class CustomerSiteAgeHandler extends AbstractDynamicAttributeHandler<Integer, CustomerModel>
{
	private static final Logger LOG = Logger.getLogger(CustomerSiteAgeHandler.class);

	@Override
	public final Integer get(final CustomerModel customerModel)
	{
		LOG.info(" ============== Inside CustomerSiteAgeHandler ===============");
		int customerSiteAge = 0;
		try
		{
			final Date customerRegisteredDate = customerModel.getCreationtime();
			final Calendar cal = Calendar.getInstance();
			cal.setTime(customerRegisteredDate);
			final int registeredYear = cal.get(Calendar.YEAR);
			final int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			customerSiteAge = currentYear - registeredYear;
		}
		catch (final Exception e)
		{
			e.printStackTrace();
		}
		return Integer.valueOf(customerSiteAge);
	}

}
