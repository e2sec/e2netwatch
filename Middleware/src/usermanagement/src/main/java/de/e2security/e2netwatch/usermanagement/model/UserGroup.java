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

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "user_group")
public class UserGroup {
	
	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @ManyToMany
    @JoinTable(
            name = "ugr_aty",
            joinColumns = {@JoinColumn(name = "ugr_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "aty_id", referencedColumnName = "id")})
    @LazyCollection(LazyCollectionOption.TRUE)
    private Set<Authority> authorities;
    
    @OneToOne(mappedBy = "userGroup")
    private ProfilePreference profilePreference;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public ProfilePreference getProfilePreference() {
		return profilePreference;
	}

	public void setProfilePreference(ProfilePreference profilePreference) {
		this.profilePreference = profilePreference;
	}
	
}