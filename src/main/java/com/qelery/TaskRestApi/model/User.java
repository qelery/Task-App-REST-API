package com.qelery.TaskRestApi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String username;

    @Column
    @JsonProperty(access=JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(unique=true)
    private String emailAddress;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="profile_id")
    private UserProfile userProfile;

    @OneToMany(mappedBy="user") // This means that the Category.user property holds the logic for mapping this relationship
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Category> categories;

    @OneToMany(mappedBy="user") // This means that the Task.user property holds the logic for mapping this relationship
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Task> tasks;

    public User() {
    }

    public User(Long id, String username, String password, String emailAddress) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                '}';
    }
}
