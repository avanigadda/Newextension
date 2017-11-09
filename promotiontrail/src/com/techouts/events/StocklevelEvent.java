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

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.event.ClusterAwareEvent;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

import org.apache.log4j.Logger;


/**
 *
 */
public class StocklevelEvent extends AbstractEvent implements ClusterAwareEvent
{
	private ProductModel productModel;

	public ProductModel getProductModel()
	{
		return productModel;
	}

	public void setProductModel(final ProductModel productModel)
	{
		this.productModel = productModel;
	}

	//private StockLevelModel stockLevelModel;
	private static final Logger LOG = Logger.getLogger(StocklevelEvent.class);

	public StocklevelEvent()
	{
		LOG.info(" ***************** StocklevelEvent  **************************** ");
	}

	@Override
	public boolean publish(final int sourceNodeId, final int targetNodeId)
	{
		LOG.info("sourceNodeId= " + sourceNodeId + "targetNodeId =" + targetNodeId);
		return sourceNodeId == targetNodeId;
	}
}
