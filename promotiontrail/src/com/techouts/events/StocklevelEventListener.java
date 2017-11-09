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
package com.techouts.events;

import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import jersey.repackaged.com.google.common.collect.Lists;


/**
 *
 */
public class StocklevelEventListener extends AbstractEventListener<StocklevelEvent>
{
	private static final Logger LOG = Logger.getLogger(StocklevelEventListener.class);

	@Override
	protected void onEvent(final StocklevelEvent stocklevelEvent)
	{
		LOG.info(" *************************** StocklevelEventListener ************************ ");
		LOG.info(" *************************** stocklevelEvent.getScope()= " + stocklevelEvent.getScope());
		final Set<StockLevelModel> stockLevels = stocklevelEvent.getProductModel().getStockLevels();
		final List<StockLevelModel> stockLevelList = Lists.newArrayList(stockLevels);
		for (final StockLevelModel stockLevel : stockLevelList)
		{
			LOG.info(" available stock" + stockLevel.getAvailable());
		}
	}


}
