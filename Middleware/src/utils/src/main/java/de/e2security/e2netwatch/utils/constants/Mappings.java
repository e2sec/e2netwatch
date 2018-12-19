package de.e2security.e2netwatch.utils.constants;

public final class Mappings {
	
	// public static final String BASE = WebConstants.PATH_SEP;
    public static final String BASE = "/rest";
    
    public static final String REPORTS = "/reports";
    public static final String USER_MANAGEMENT = "/um";
    public static final String ELASTICSEARCH = "/es";
    public static final String PUBLIC = "/public";
    
    public static final String USERS = USER_MANAGEMENT + "/users";
    public static final String USERSTATUSES = USER_MANAGEMENT + "/userstatuses";
    public static final String USERGROUPS = USER_MANAGEMENT + "/usergroups";
    public static final String TIMEZONES = USER_MANAGEMENT + "/timezones";

    private Mappings() {
        throw new AssertionError();
    }

    // API

}
