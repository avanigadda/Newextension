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
package com.techouts.promotions.custom;


import de.hybris.platform.ruledefinitions.AmountOperator;
import de.hybris.platform.ruledefinitions.CollectionOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleConditionTranslator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeRelCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrFalseCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrGroupOperator;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class CustomRuleProductPriceConditionTranslator implements RuleConditionTranslator
{
	public static final String OPERATOR_PARAM = "operator";
	public static final String VALUE_PARAM = "value";
	public static final String CART_RAO_ENTRIES_ATTRIBUTE = "entries";
	public static final String ORDER_ENTRY_RAO_BASE_PRICE_ATTRIBUTE = "basePrice";
	public static final String ORDER_ENTRY_RAO_TOTAL_PRICE_ATTRIBUTE = "totalPrice";
	public static final String CART_RAO_CURRENCY_ATTRIBUTE = "currencyIsoCode";
	private static final Logger LOG = LoggerFactory.getLogger(CustomRuleProductPriceConditionTranslator.class);


	@Override
	public RuleIrCondition translate(final RuleCompilerContext context, final RuleConditionData condition,
			final RuleConditionDefinitionData conditionDefinition) throws RuleCompilerException
	{
		LOG.info(" ------ CustomRuleProductPriceConditionTranslator ----------  ");
		final RuleParameterData operatorParameter = condition.getParameters().get("operator");
		final RuleParameterData valueParameter = condition.getParameters().get("value");
		final RuleParameterData productsOperatorParameter = condition.getParameters().get("products_operator");
		final RuleParameterData productsParameter = condition.getParameters().get("products");


		if ((operatorParameter == null) || (valueParameter == null) || (productsOperatorParameter == null)
				|| (productsParameter == null))
		{
			return new RuleIrFalseCondition();
		}

		final AmountOperator operator = (AmountOperator) operatorParameter.getValue();
		final Map<String, BigDecimal> value = (Map) valueParameter.getValue();
		final CollectionOperator productsOperator = (CollectionOperator) productsOperatorParameter.getValue();
		final List<String> products = (List) productsParameter.getValue();
		if ((operator == null) || (MapUtils.isEmpty(value)))
		{
			return new RuleIrFalseCondition();
		}

		final RuleIrGroupCondition irGroupCondition = new RuleIrGroupCondition();
		irGroupCondition.setOperator(RuleIrGroupOperator.OR);
		irGroupCondition.setChildren(new ArrayList());

		addProductPriceConditions(context, operator, value, irGroupCondition, productsOperator, products);
		return irGroupCondition;
	}


	protected void addProductPriceConditions(final RuleCompilerContext context, final AmountOperator operator,
			final Map<String, BigDecimal> value, final RuleIrGroupCondition irGroupCondition,
			final CollectionOperator productsOperator, final List<String> products)
	{
		final String orderEntryRaoVariable = context.generateVariable(OrderEntryRAO.class);
		final String cartRaoVariable = context.generateVariable(CartRAO.class);
		LOG.info(" orderEntryRaoVariable = " + orderEntryRaoVariable + "cartRaoVariable =" + cartRaoVariable + "operator ="
				+ operator);
		for (final Map.Entry<String, BigDecimal> entry : value.entrySet())
		{
			if ((entry.getKey() != null) && (entry.getValue() != null))
			{
				LOG.info(" entry.getKey() = " + entry.getKey() + " entry.getValue() = " + entry.getValue());
				final RuleIrGroupCondition irCurrencyGroupCondition = new RuleIrGroupCondition();
				irCurrencyGroupCondition.setOperator(RuleIrGroupOperator.AND);
				irCurrencyGroupCondition.setChildren(new ArrayList());

				final RuleIrAttributeCondition irCurrencyCondition = new RuleIrAttributeCondition();
				irCurrencyCondition.setVariable(cartRaoVariable);
				irCurrencyCondition.setAttribute(CART_RAO_CURRENCY_ATTRIBUTE);
				irCurrencyCondition.setOperator(RuleIrAttributeOperator.EQUAL);
				irCurrencyCondition.setValue(entry.getKey());

				final RuleIrAttributeCondition irOrderEntryBasePriceCondition = new RuleIrAttributeCondition();
				irOrderEntryBasePriceCondition.setVariable(orderEntryRaoVariable);
				irOrderEntryBasePriceCondition.setAttribute(ORDER_ENTRY_RAO_BASE_PRICE_ATTRIBUTE);
				irOrderEntryBasePriceCondition.setOperator(RuleIrAttributeOperator.valueOf(operator.name()));
				irOrderEntryBasePriceCondition.setValue(entry.getValue());

				final RuleIrAttributeCondition irOrderEntryTotalPriceCondition = new RuleIrAttributeCondition();
				irOrderEntryBasePriceCondition.setVariable(orderEntryRaoVariable);
				irOrderEntryBasePriceCondition.setAttribute(ORDER_ENTRY_RAO_TOTAL_PRICE_ATTRIBUTE);
				irOrderEntryBasePriceCondition.setOperator(RuleIrAttributeOperator.valueOf(operator.name()));
				irOrderEntryBasePriceCondition.setValue(entry.getValue());

				final RuleIrAttributeRelCondition irCartOrderEntryRel = new RuleIrAttributeRelCondition();
				irCartOrderEntryRel.setVariable(cartRaoVariable);
				irCartOrderEntryRel.setAttribute(CART_RAO_ENTRIES_ATTRIBUTE);
				irCartOrderEntryRel.setOperator(RuleIrAttributeOperator.CONTAINS);
				irCartOrderEntryRel.setTargetVariable(orderEntryRaoVariable);

				irCurrencyGroupCondition.getChildren().add(irCurrencyCondition);
				irCurrencyGroupCondition.getChildren().add(irOrderEntryBasePriceCondition);
				irCurrencyGroupCondition.getChildren().add(irOrderEntryTotalPriceCondition);
				irCurrencyGroupCondition.getChildren().add(irCartOrderEntryRel);

				irGroupCondition.getChildren().add(irCurrencyGroupCondition);
			}
		}
	}





}
