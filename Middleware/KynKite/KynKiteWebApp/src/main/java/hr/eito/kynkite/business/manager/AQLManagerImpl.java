
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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cup.parser;
import jflex.Scanner;
import hr.eito.kynkite.aql.dao.RulesetDAO;
import hr.eito.kynkite.aql.model.AqlParams;
import hr.eito.kynkite.aql.model.Ruleset;
import hr.eito.kynkite.aql.model.dto.RulesetReturnResult;
import hr.eito.kynkite.utils.CustomError;
import hr.eito.kynkite.utils.CustomMessage;
import hr.eito.model.JsonReturnData;


/**
 * AQL manager for managing AQL requests and data
 * 
 * @author Hrvoje
 *
 */
@Component
public class AQLManagerImpl implements AQLManager {
	
	@Autowired
	private RulesetDAO rulesetDAO;

	@Override
	public JsonReturnData<RulesetReturnResult> aqlRulesList() {
		try {
			List<Ruleset> list = rulesetDAO.getAllRuleset();
			RulesetReturnResult result = new RulesetReturnResult(list);
			return new JsonReturnData<RulesetReturnResult>(result);
		} catch (Exception e) {
			return new JsonReturnData<RulesetReturnResult>(e.getMessage());
		}
	}

	@Override
	public JsonReturnData<RulesetReturnResult> addAqlRule(AqlParams params) {
		try {
			// Validate rule
			JsonReturnData<String> validationResult = this.validateRule(params.getRule());
			if (!validationResult.isOK()) {
				return new JsonReturnData<>(validationResult.getErrorMessage());
			}
			
			// Check if rule already exists in database
			if (ruleAlreadyExists(params.getRule())) {
				return new JsonReturnData<>(CustomError.AQL_RULE_ALREADY_EXISTS.getErrorMessage());
			}
			
			// Prepare new Ruleset
			Ruleset ruleset = new Ruleset();
			ruleset.setRule(params.getRule());
			ruleset.setDescription(params.getDescription());
			
			// Insert Ruleset into DB
			rulesetDAO.insertRuleset(ruleset);
			
			// Prepare return data and return
			List<Ruleset> list = new ArrayList<>();
			list.add(ruleset);
			RulesetReturnResult result = new RulesetReturnResult(list);
			return new JsonReturnData<RulesetReturnResult>(result);
		} catch (Exception e) {
			return new JsonReturnData<RulesetReturnResult>(e.getMessage());
		}
	}
	
	@Override
	public JsonReturnData<RulesetReturnResult> editAqlRule(AqlParams params) {
		try {
			// Validate rule
			JsonReturnData<String> validationResult = this.validateRule(params.getRule());
			if (!validationResult.isOK()) {
				return new JsonReturnData<>(validationResult.getErrorMessage());
			}
			
			// Check if rule already exists in database in some other
			if (ruleAlreadyExistsAmongOthers(params.getRule(), params.getId())) {
				return new JsonReturnData<>(CustomError.AQL_RULE_ALREADY_EXISTS.getErrorMessage());
			}
			
			// Check if Ruleset for this ID exists
			if (rulesetDAO.getById(params.getId())==null) {
				return new JsonReturnData<>(CustomError.AQL_RULESET_MISSING.getErrorMessage());
			}
			
			// Prepare Ruleset for update
			Ruleset ruleset = new Ruleset();
			ruleset.setId(params.getId());
			ruleset.setRule(params.getRule());
			ruleset.setDescription(params.getDescription());
			
			// Update Ruleset in DB
			rulesetDAO.updateRuleset(ruleset);
			
			// Prepare return data and return
			List<Ruleset> list = new ArrayList<>();
			list.add(ruleset);
			RulesetReturnResult result = new RulesetReturnResult(list);
			return new JsonReturnData<RulesetReturnResult>(result);
		} catch (Exception e) {
			return new JsonReturnData<RulesetReturnResult>(e.getMessage());
		}
	}
	
	@Override
	public JsonReturnData<String> deleteAqlRule(AqlParams params) {
		try {
			// Getting ruleset for the specific Id
			Ruleset ruleset = rulesetDAO.getById(params.getId());
			
			// Check if ruleset was found
			if (ruleset==null) {
				return new JsonReturnData<>(CustomError.AQL_RULESET_MISSING.getErrorMessage());
			}
			
			// Delete ruleset
			rulesetDAO.deleteRuleset(ruleset);
			
			// Prepare success answer
			JsonReturnData<String> returnJson = new JsonReturnData<>();
			returnJson.setOK();
			return returnJson;
		} catch (Exception e) {
			return new JsonReturnData<>(e.getMessage());
		}
	}
	
	@Override
	public JsonReturnData<String> aqlRuleValidation(final AqlParams params) {
		return this.validateRule(params.getRule());
	}
	
	@SuppressWarnings("deprecation")
	private JsonReturnData<String> validateRule(String rule) {
		// Check if rule is empty
		if (isRuleEmpty(rule)) {
			return new JsonReturnData<>(CustomError.AQL_RULE_MISSING.getErrorMessage());
		}
		// Create return object
		JsonReturnData<String> returnJson = new JsonReturnData<>();
		
		// Create a stream to hold the output
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PrintStream ps = new PrintStream(baos);
	    // IMPORTANT: Save the old System.out and System.err
	    PrintStream oldOut = System.out;
	    PrintStream oldErr = System.err;
	    // Tell Java to use your special stream
	    System.setOut(ps);
	    System.setErr(ps);
		try {
			// Try parsing the rule
			parser p = new parser(new Scanner(new java.io.StringReader(rule)));
			p.parse();
			
			// Prepare success answer
			returnJson.setOK();
			returnJson.setContent(CustomMessage.AQL_RULE_VALID.getMessage());
		} catch (Exception e) {
			returnJson.setErrorMessage(baos.toString() + e.getMessage());
		} finally {
			// Put things back
		    System.setOut(oldOut);
		    System.setErr(oldErr);
		}
		return returnJson;
	}
	
	/**
	 * Checking if rule text is empty or null
	 * 
	 * @param rule to be checked
	 * 
	 * @return true is the rule text is empty or null, otherwise false
	 */
	private boolean isRuleEmpty(final String rule) {
		if (StringUtils.isEmpty(rule)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checking if rule text exists with some other ruleset
	 * 
	 * @param rule text to be checked
	 * 
	 * @return true if rule already exists, otherwise return false
	 */
	private boolean ruleAlreadyExists(final String rule) {
		if (rulesetDAO.getRulesetByRule(rule)!=null) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checking if rule text exists with some other ruleset other than this
	 * 
	 * @param rule text to be checked
	 * 
	 * @return true if rule already exists, otherwise return false
	 */
	private boolean ruleAlreadyExistsAmongOthers(final String rule, final Integer id) {
		if (rulesetDAO.getRulesetByRuleAndNotId(rule, id)!=null) {
			return true;
		}
		return false;
	}
	
}
