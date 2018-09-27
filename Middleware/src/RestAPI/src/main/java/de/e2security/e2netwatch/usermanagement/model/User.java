package de.e2security.e2netwatch.usermanagement.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * User entity for db table "user"
 * 
 * @author Hrvoje
 *
 */
@Entity
@Table(name = "user")
public class User {
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "username")
    @NotNull
    private String username;

    @Column(name = "password")
    @NotNull
    private String password;
    
    @Column(name = "first_name")
    @NotNull
    private String firstName;
    
    @Column(name = "last_name")
    @NotNull
    private String lastName;
    
    @Column(name = "email")
    @NotNull
    private String email;

    @ManyToMany
    @JoinTable(
            name = "usr_aty",
            joinColumns = {@JoinColumn(name = "usr_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "aty_id", referencedColumnName = "id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Authority> authorities;
    
    @ManyToMany
    @JoinTable(
            name = "usr_ugr",
            joinColumns = {@JoinColumn(name = "usr_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "ugr_id", referencedColumnName = "id")})
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<UserGroup> userGroups;
    
    @OneToOne(mappedBy = "user")
    private ProfilePreference profilePreference;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Set<Authority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }
    
    public Set<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Set<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public ProfilePreference getProfilePreference() {
		return profilePreference;
	}

	public void setProfilePreference(ProfilePreference profilePreference) {
		this.profilePreference = profilePreference;
	}
    
}
