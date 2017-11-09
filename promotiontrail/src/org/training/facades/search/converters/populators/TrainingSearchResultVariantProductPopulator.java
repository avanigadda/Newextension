package org.training.facades.search.converters.populators;

import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.converters.populator.SearchResultVariantProductPopulator;
import de.hybris.platform.commerceservices.search.resultdata.SearchResultValueData;


public class TrainingSearchResultVariantProductPopulator extends SearchResultVariantProductPopulator
{

	@Override
	public void populate(final SearchResultValueData source, final ProductData target)
	{
		super.populate(source, target);
		//Adding online days to product data
		final Object obj = this.getValue(source, "onlineDays");
		if (obj != null)
		{
			target.setOnlineDays(this.<Integer> getValue(source, "onlineDays").intValue());
		}
		else
		{
			target.setOnlineDays(0);
		}


	}
}