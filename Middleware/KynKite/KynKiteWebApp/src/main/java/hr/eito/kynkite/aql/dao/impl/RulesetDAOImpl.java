
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


package hr.eito.kynkite.aql.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import hr.eito.kynkite.aql.dao.RulesetDAO;
import hr.eito.kynkite.aql.model.Ruleset;

@Repository
@Transactional("aqlTransactionManager")
public class RulesetDAOImpl implements RulesetDAO {
	
	@PersistenceContext(unitName="aqlEntityManager")
	private EntityManager aqlEntityManager;

	@Override
	public List<Ruleset> getAllRuleset() {
		Query query = aqlEntityManager.createNativeQuery(
			    "select * from ruleset order by id", Ruleset.class);
		@SuppressWarnings("unchecked")
		List<Ruleset> rulesets = query.getResultList();
		return rulesets;
	}

	@Override
	public Ruleset getRulesetByRule(String rule) {
		Query query = aqlEntityManager.createNativeQuery(
			    "select * from ruleset where trim(rule) = :rule", Ruleset.class)
				.setParameter("rule", rule);
		try {
			Ruleset ruleset = (Ruleset) query.getSingleResult();
			return ruleset;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public Ruleset getRulesetByRuleAndNotId(String rule, Integer id) {
		Query query = aqlEntityManager.createNativeQuery(
			    "select * from ruleset where trim(rule) = :rule and id <> :id", Ruleset.class)
				.setParameter("rule", rule)
				.setParameter("id", id);
		try {
			Ruleset ruleset = (Ruleset) query.getSingleResult();
			return ruleset;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void insertRuleset(Ruleset ruleset) {
		aqlEntityManager.persist(ruleset);
		aqlEntityManager.flush();
	}

	@Override
	public Ruleset getById(Integer id) {
		Query query = aqlEntityManager.createNativeQuery(
			    "select * from ruleset where id = :id", Ruleset.class)
				.setParameter("id", id);
		try {
			Ruleset ruleset = (Ruleset) query.getSingleResult();
			return ruleset;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void updateRuleset(Ruleset ruleset) {
		aqlEntityManager.merge(ruleset);
		aqlEntityManager.flush();
	}

	@Override
	public void deleteRuleset(Ruleset ruleset) {
		aqlEntityManager.remove(aqlEntityManager.contains(ruleset) ? ruleset : aqlEntityManager.merge(ruleset));
	}
	
}
