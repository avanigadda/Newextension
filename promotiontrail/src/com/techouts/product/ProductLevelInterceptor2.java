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
package com.techouts.product;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.techouts.events.StocklevelEvent;


/**
 *
 */
public class ProductLevelInterceptor2 implements ValidateInterceptor<ProductModel>
{

	@Resource(name = "userService")
	private UserService userService;
	@Resource(name = "eventService")
	private EventService eventService;
	private static final Logger LOG = Logger.getLogger(ProductLevelInterceptor2.class);

	@Override
	public void onValidate(final ProductModel model, final InterceptorContext ctx) throws InterceptorException
	{
		LOG.info(" =========================   ProductLevelInterceptor2      ======================");
		final Map<String, Set<Locale>> map = ctx.getDirtyAttributes(model);
		if ((map.containsKey(ProductModel.NAME) || ctx.isModified(model, ProductModel.NAME)))
		{
			LOG.info(" ========================= Product name has been changed  ======================");
			//final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
			//final Integer age = customerModel.getCustomer_site_age();
			//LOG.info(" ----- age = " + age.intValue());
			final StocklevelEvent stocklevelEvent = new StocklevelEvent();
			stocklevelEvent.setProductModel(model);
			//stocklevelEvent.getStockLevelModel(model.getStockLevels().)
			eventService.publishEvent(stocklevelEvent);
			LOG.info(" ========================= After Publishing the event  ======================");
		}
	}

}
