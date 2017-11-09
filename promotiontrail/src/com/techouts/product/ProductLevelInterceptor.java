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
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.user.UserService;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;


/**
 *
 */
public class ProductLevelInterceptor implements ValidateInterceptor<ProductModel>
{

	@Resource(name = "userService")
	private UserService userService;
	private static final Logger LOG = Logger.getLogger(ProductLevelInterceptor.class);

	@Override
	public void onValidate(final ProductModel model, final InterceptorContext ctx) throws InterceptorException
	{
		LOG.info(" =========================         ======================");
		final Map<String, Set<Locale>> map = ctx.getDirtyAttributes(model);
		if ((map.containsKey(ProductModel.NAME) || ctx.isModified(model, ProductModel.NAME)))
		{
			LOG.info(" ========================= Product name has been changed  ======================");
			final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
			final Integer age = customerModel.getCustomer_site_age();
			LOG.info(" ----- age = " + age.intValue());

		}
	}

}
