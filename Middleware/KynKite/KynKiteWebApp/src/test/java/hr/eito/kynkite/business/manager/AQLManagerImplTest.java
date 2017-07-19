
/*
    Copyright (C) 2017 e-ito Technology Services GmbH
    e-mail: info@e-ito.de
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package hr.eito.kynkite.business.manager;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import hr.eito.kynkite.aql.model.AqlParams;
import hr.eito.kynkite.aql.model.dto.RulesetReturnResult;
import hr.eito.kynkite.utils.CustomError;
import hr.eito.kynkite.utils.CustomMessage;
import hr.eito.model.JsonReturnData;

/**
 * Tests the AQLManagerImpl.
 *
 * @author Hrvoje
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/config/app-config.xml" })
@ActiveProfiles("test")
public class AQLManagerImplTest {
	
	@Autowired
	private AQLManager aqlManager;

	/**
	 * Runs before the tests start.
	 */
	@BeforeClass
	public static void testStart() {}
	
	/**
	 * Runs after the tests end.
	 */
	@AfterClass
	public static void testEnd() {}

	/**
	 * Check we have a manager.
	 */
	@Test
	public void testAqlManager() {
		Assert.assertNotNull(aqlManager);
	}
	
	/**
	 * Test getting aql rules list
	 */
	@Test
	public void testAqlRulesList() {
		JsonReturnData<RulesetReturnResult> returnResult = aqlManager.aqlRulesList();
		
		Assert.assertNotNull(returnResult.getContent());
		Assert.assertEquals(3, returnResult.getContent().getRecordsFiltered());
		Assert.assertEquals(3, returnResult.getContent().getRecordsTotal());
	}
	
	/**
	 * Test adding AQL rule
	 */
	@Test
	public void testAddAqlRule() {
		// Test adding empty rule
		AqlParams paramsEmptyRule = new AqlParams();
		paramsEmptyRule.setRule("");
		JsonReturnData<RulesetReturnResult> resultEmptyRule = aqlManager.addAqlRule(paramsEmptyRule);
		
		Assert.assertFalse(resultEmptyRule.isOK());
		Assert.assertEquals(CustomError.AQL_RULE_MISSING.getErrorMessage(), resultEmptyRule.getErrorMessage());
		
		// Test adding existing rule
		AqlParams paramsExistingRule = new AqlParams();
		paramsExistingRule.setRule(aqlManager.aqlRulesList().getContent().getData().get(0).getRule()); // Rule from first ruleset
		JsonReturnData<RulesetReturnResult> resultExistingRule = aqlManager.addAqlRule(paramsExistingRule);
		
		Assert.assertFalse(resultExistingRule.isOK());
		Assert.assertEquals(CustomError.AQL_RULE_ALREADY_EXISTS.getErrorMessage(), resultExistingRule.getErrorMessage());
		
		// Test correct insert
		String newRule = "New Rule";
		String newDescription = "Description for new rule";
		AqlParams paramsCorrectRule = new AqlParams();
		paramsCorrectRule.setRule(newRule);
		paramsCorrectRule.setDescription(newDescription);
		JsonReturnData<RulesetReturnResult> resultCorrectRule = aqlManager.addAqlRule(paramsCorrectRule);
		
		Assert.assertNotNull(resultCorrectRule.getContent());
		Assert.assertEquals(1, resultCorrectRule.getContent().getRecordsFiltered());
		Assert.assertEquals(1, resultCorrectRule.getContent().getRecordsTotal());
		Assert.assertEquals(new Integer(4), resultCorrectRule.getContent().getData().get(0).getId());
		Assert.assertEquals(newRule, resultCorrectRule.getContent().getData().get(0).getRule());
		Assert.assertEquals(newDescription, resultCorrectRule.getContent().getData().get(0).getDescription());
	}
	
	/**
	 * Test editing AQL rule
	 */
	@Test
	public void testEditAqlRule() {
		// Test editing with empty rule
		AqlParams paramsEmptyRule = new AqlParams();
		paramsEmptyRule.setRule("");
		JsonReturnData<RulesetReturnResult> resultEmptyRule = aqlManager.editAqlRule(paramsEmptyRule);
		
		Assert.assertFalse(resultEmptyRule.isOK());
		Assert.assertEquals(CustomError.AQL_RULE_MISSING.getErrorMessage(), resultEmptyRule.getErrorMessage());
		
		// Test editing with existing rule
		AqlParams paramsExistingRule = new AqlParams();
		paramsExistingRule.setRule(aqlManager.aqlRulesList().getContent().getData().get(0).getRule()); // Rule from first ruleset
		JsonReturnData<RulesetReturnResult> resultExistingRule = aqlManager.editAqlRule(paramsExistingRule);
		
		Assert.assertFalse(resultExistingRule.isOK());
		Assert.assertEquals(CustomError.AQL_RULE_ALREADY_EXISTS.getErrorMessage(), resultExistingRule.getErrorMessage());
		
		// Test editing missing rule
		AqlParams paramsMissingRule = new AqlParams();
		paramsMissingRule.setId(99);
		paramsMissingRule.setRule("some rule");
		JsonReturnData<RulesetReturnResult> resultMissingRule = aqlManager.editAqlRule(paramsMissingRule);
		
		Assert.assertFalse(resultMissingRule.isOK());
		Assert.assertEquals(CustomError.AQL_RULESET_MISSING.getErrorMessage(), resultMissingRule.getErrorMessage());
		
		// Test correct edit
		Integer id = 2;
		String rule = "Edit of rule 2";
		String description = "Edited description for rule 2";
		AqlParams paramsCorrectRule = new AqlParams();
		paramsCorrectRule.setId(id);
		paramsCorrectRule.setRule(rule);
		paramsCorrectRule.setDescription(description);
		JsonReturnData<RulesetReturnResult> resultCorrectRule = aqlManager.editAqlRule(paramsCorrectRule);
		
		Assert.assertNotNull(resultCorrectRule.getContent());
		Assert.assertEquals(1, resultCorrectRule.getContent().getRecordsFiltered());
		Assert.assertEquals(1, resultCorrectRule.getContent().getRecordsTotal());
		Assert.assertEquals(new Integer(2), resultCorrectRule.getContent().getData().get(0).getId());
		Assert.assertEquals(rule, resultCorrectRule.getContent().getData().get(0).getRule());
		Assert.assertEquals(description, resultCorrectRule.getContent().getData().get(0).getDescription());
	}
	
	/**
	 * Test deleting AQL rule
	 */
	@Test
	public void testDeleteAqlRule() {
		// Test deleting missing rule
		AqlParams paramsMissingRule = new AqlParams();
		paramsMissingRule.setId(99);
		JsonReturnData<String> resultMissingRule = aqlManager.deleteAqlRule(paramsMissingRule);
		
		Assert.assertFalse(resultMissingRule.isOK());
		Assert.assertEquals(CustomError.AQL_RULESET_MISSING.getErrorMessage(), resultMissingRule.getErrorMessage());

		// Test correct delete
		Integer id = 2;
		AqlParams paramsCorrectRule = new AqlParams();
		paramsCorrectRule.setId(id);
		JsonReturnData<String> resultCorrectRule = aqlManager.deleteAqlRule(paramsCorrectRule);
		
		Assert.assertTrue(resultCorrectRule.isOK());
	}
	
	/**
	 * Test AQL rule validation
	 */
	@Test
	public void testAqlRuleValidation() {
		String[] ruleList = {"((COUNT SSCOPE \"10.0.0.0/8\", \"now-15m\", \"now\", DST FOUND (LISTED \"now-30d\",\"now\",\"ip\" AS \"kyn-malware\")) > 0)",
				"((DCARD SSCOPE \"0.0.0.0/0\", \"now-15m\", \"now\",PRT MATCHES 22) > 5) AND ((DCARD SSCOPE \"0.0.0.0/0\", \"now-10080m\", \"now-15m\",PRT MATCHES 22)<5)",
				"((COUNT DSCOPE \"0.0.0.0/0\",\"now-15m\", \"now\", PRT MATCHES 22)/(COUNT DSCOPE \"0.0.0.0/0\",\"now-30m\", \"now-15m\", PRT MATCHES 22) > 10) AND ((SUM DSCOPE \"0.0.0.0/0\",\"now-15m\", \"now\", PRT MATCHES 22)/(SUM DSCOPE \"0.0.0.0/0\",\"now-30m\", \"now-15m\", PRT MATCHES 22) < 2)",
				"((COUNT SSCOPE \"10.0.0.0/0\", \"now-15m\",\"now\", DST FOUND (LISTED \"now-30d\",\"now\",\"ip\" AS \"kyn-malware\")) > 0)",
				"((SUM SSCOPE \"10.0.0.0/8\",\"now-15m\",\"now\",PRT MATCHES 445)>0) AND ((COUNT SSCOPE \"10.0.0.0/8\",\"now-15m\",\"now\",DST FOUND (LISTED \"now-30d\",\"now\",\"ip\" AS \"kyn-malware\")) > 0)",
				"(COUNT SSCOPE \"0.0.0.0/0\",\"now-15m\", \"now\", SRC MATCHES \"0.0.0.0/0\")/(COUNT \"now-30m\", \"now-15m\", SRC MATCHES \"0.0.0.0/0\") > 10",
				"(DCARD SSCOPE \"0.0.0.0/0\", \"now-15m\",\"now\", SRC MATCHES \"0.0.0.0/0\")/(DCARD SSCOPE \"0.0.0.0/0\", \"now-1440m\",\"now-15m\", SRC MATCHES \"0.0.0.0/0\") > 10",
				"(SCARD DSCOPE \"10.0.0.0/8\", \"now-15m\", \"now\", PRT MATCHES 53)/(SCARD DSCOPE \"10.0.0.0/8\", \"now-1d\", \"now-15m\", PRT MATCHES 53)>10",
				"((SCARD DSCOPE \"0.0.0.0/0\", \"now-15m\", \"now\", FLAGS MATCHES \"....S.\") > 5) AND ((SCARD DSCOPE \"0.0.0.0/0\", \"now-15m\", \"now\", FLAGS MATCHES \".A....\") = 0)",
				"((COUNT SSCOPE \"10.0.0.0/0\", \"now-15m\",\"now\",DST FOUND(LISTED \"now-365d\",\"now\",\"IP\" AS \"protectedhosts\"))>0) AND ((COUNT SSCOPE \"10.0.0.0/0\", \"now-30d\",\"now\",DST FOUND(LISTED \"now-365d\",\"now\",\"IP\" AS \"protectedhosts\")) = 0)",
				"(SUM SSCOPE \"0.0.0.0/0\",\"now-15m\", \"now\", PRT MATCHES 23) > 0",
				"(COUNT DSCOPE \"0.0.0.0/0\",\"now-15m\", \"now\", PRT MATCHES 88)/(COUNT DSCOPE \"0.0.0.0/0\",\"now-30m\", \"now-15m\", PRT MATCHES 88) > 10",
				"((SCARD DSCOPE \"0.0.0.0/0\",\"now-15m\",\"now\",PRT MATCHES 88) > 0) AND ((SCARD DSCOPE \"0.0.0.0/0\",\"now-7d\",\"now-15m\",PRT MATCHES 88) = 0)",
				"(SUM DSCOPE \"0.0.0.0/0\",\"now-15m\", \"now\", PRT MATCHES 88)/(SUM DSCOPE \"0.0.0.0/0\",\"now-30m\", \"now-15m\", PRT MATCHES 88) > 10",
				"(COUNT SSCOPE \"10.0.0.0/8\", \"now-15m\", \"now\", DST FOUND (LISTED \"now-30d\",\"now\",\"ip\" AS \"kyn-malware\")) > 0",
				"((DCARD SSCOPE \"0.0.0.0/0\", \"now-60m\", \"now\",PROTO MATCHES 6) >100)",
				"((PCARD SSCOPE \"0.0.0.0/0\", \"now-15m\", \"now\",PROTO MATCHES 6)>100) AND ((PCARD SSCOPE \"0.0.0.0/0\", \"now-30m\", \"now-15m\",PROTO MATCHES 6)<50)",
				"(SCARD DSCOPE \"0.0.0.0/0\", \"now-15m\",\"now\", DST MATCHES \"0.0.0.0/0\")/(SCARD DSCOPE \"0.0.0.0/8\", \"now-1440m\",\"now-15m\", DST MATCHES \"0.0.0.0/0\") > 10",
				"(SCARD DSCOPE \"0.0.0.0/0\",\"now-15m\", \"now\", PRT MATCHES 445)/(SCARD DSCOPE \"0.0.0.0/0\",\"now-30m\", \"now-15m\", PRT MATCHES 445) > 10",
				"(SUM SSCOPE \"0.0.0.0/0\", \"now-15m\", \"now\", PROTO MATCHES 47) > 0",
				"(SUM SSCOPE \"0.0.0.0/0\",\"now-15m\", \"now\", SRC MATCHES \"0.0.0.0/0\")/(SUM SSCOPE \"0.0.0.0/0\",\"now-30m\", \"now-15m\", SRC MATCHES \"0.0.0.0/0\") > 10",
				"((COUNT SSCOPE \"0.0.0.0/0\", \"now-1d\",\"now\",PRT MATCHES 53)/(DCARD SSCOPE \"10.0.0.0/0\", \"now-1d\",\"now\",PRT MATCHES 80))/((COUNT SSCOPE \"10.0.0.0/0\", \"now-7d\",\"now-1d\",PRT MATCHES 53)/(DCARD SSCOPE \"10.0.0.0/0\", \"now-7d\",\"now-1d\",PRT MATCHES 80))<20",
				"(((SUM DSCOPE \"213.0.0.0/8\", \"now-15m\",\"now\", PRT MATCHES 80)/(COUNT DSCOPE \"213.0.0.0/8\", \"now-15m\",\"now\", PRT MATCHES 80))/((SUM DSCOPE \"213.0.0.0/8\", \"now-30m\",\"now-15m\", PRT MATCHES 80)/(COUNT DSCOPE \"213.0.0.0/8\", \"now-15m\",\"now\", PRT MATCHES 80))>10) OR (((SUM DSCOPE \"213.0.0.0/8\",\"now-15m\",\"now\", PRT MATCHES 80)/(COUNT DSCOPE \"213.0.0.0/8\",\"now-15m\",\"now\", PRT MATCHES 80))/((SUM DSCOPE \"213.0.0.0/8\",\"now-30m\",\"now-15m\", PRT MATCHES 80)/(COUNT DSCOPE \"213.0.0.0/8\",\"now-15m\",\"now\", PRT MATCHES 80))<0.1)"};
		for (String rule : ruleList) {
			testSingleAqlRuleValidation(rule);
		}
	}
	
	/**
	 * Helper method for validating single aql rule
	 * 
	 * @param rule to be validated
	 */
	private void testSingleAqlRuleValidation(final String rule) {
		AqlParams params = new AqlParams();
		params.setRule(rule);
		JsonReturnData<String> resultMissingRule = aqlManager.aqlRuleValidation(params);
		
		Assert.assertTrue(resultMissingRule.isOK());
		Assert.assertEquals(CustomMessage.AQL_RULE_VALID.getMessage(), resultMissingRule.getContent());
	}
	
	/**
	 * Test AQL rule validation - invalid rule
	 */
	@Test
	public void testAqlRuleValidation_invalid() {
		String rule = "invalid_rule";
		AqlParams params = new AqlParams();
		params.setRule(rule);
		JsonReturnData<String> resultMissingRule = aqlManager.aqlRuleValidation(params);
		
		Assert.assertFalse(resultMissingRule.isOK());
	}
	
}
