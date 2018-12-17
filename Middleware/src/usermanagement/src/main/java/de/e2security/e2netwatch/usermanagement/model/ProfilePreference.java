package de.e2security.e2netwatch.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * User entity for db table "profile_preference"
 * 
 * @author Hrvoje
 *
 */
@Entity
@Table(name = "profile_preference")
public class ProfilePreference {
	
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "usr_id")
    private User user;

	@OneToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ugr_id")
    private UserGroup userGroup;
	
    private String timezone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
    
}
