package de.e2security.e2netwatch.usermanagement.dto;

public class ProfilePreferenceUpdateDTO {
	
    private Integer id;
    private String timezone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
    
}
