/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.techouts.promotions.setup;

import static com.techouts.promotions.constants.PromotiontrailConstants.PLATFORM_LOGO_CODE;

import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import com.techouts.promotions.constants.PromotiontrailConstants;
import com.techouts.promotions.service.PromotiontrailService;


@SystemSetup(extension = PromotiontrailConstants.EXTENSIONNAME)
public class PromotiontrailSystemSetup
{
	private final PromotiontrailService promotiontrailService;

	public PromotiontrailSystemSetup(final PromotiontrailService promotiontrailService)
	{
		this.promotiontrailService = promotiontrailService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		promotiontrailService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return PromotiontrailSystemSetup.class.getResourceAsStream("/promotiontrail/sap-hybris-platform.png");
	}
}
