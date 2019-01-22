package nw104;

import org.junit.Test;

import de.e2security.processors.e2esper.utilities.SupportUtility;
import junit.framework.Assert;

public class SupportUtilityTest {
	@Test public void transformMethodsTest() {
		String userQuerySchema = "create schema T_50005_0008_01_02 as ("
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)";
		String expectedQuerySchema = "create schema T_50005_0008_01_02 as( flowFileAttributes Map,"
				+ "tilde_event_uuid string, "
				+ "cep_delta long, "
				+ "host_hash string, "
				+ "target_user_name string, "
				+ "target_user_name_hash string, "
				+ "event_id int, "
				+ "hostname_domain string)";
		Assert.assertEquals(expectedQuerySchema, SupportUtility.modifyUserDefinedSchema(userQuerySchema));
		String userQueryStmt = "@Name(T_50005_0008_01_02_filter) SELECT '50005-0008-01' AS alert_uc_scenario, "
				+ "'ip_hash' AS enrichment_field,"
				+ "'50005_inv_ip2ci' AS enrichment_index, "
				+ "host_hash AS enrichment_key, "
				+ "tilde_event_uuid AS alert_reference1, "
				+ "tilde_event_uuid AS alert_reference2, "
				+ "cep_delta AS cep_delta, "
				+ "host_hash AS host_hash, "
				+ "target_user_name AS target_user_name, "
				+ "target_user_name_hash AS target_user_name_hash "
				+ "FROM "
				+ "T_50005_0008_01_02(event_id=4720 or event_id=4726)";
		String expectedQueryStmt = "@Name(T_50005_0008_01_02_filter) SELECT flowFileAttributes, '50005-0008-01' AS alert_uc_scenario, "
				+ "'ip_hash' AS enrichment_field,"
				+ "'50005_inv_ip2ci' AS enrichment_index, "
				+ "host_hash AS enrichment_key, "
				+ "tilde_event_uuid AS alert_reference1, "
				+ "tilde_event_uuid AS alert_reference2, "
				+ "cep_delta AS cep_delta, "
				+ "host_hash AS host_hash, "
				+ "target_user_name AS target_user_name, "
				+ "target_user_name_hash AS target_user_name_hash "
				+ "FROM "
				+ "T_50005_0008_01_02(event_id=4720 or event_id=4726)";
		Assert.assertEquals(expectedQueryStmt, SupportUtility.modifyUserDefinedEPStatement(userQueryStmt));
	}
}
