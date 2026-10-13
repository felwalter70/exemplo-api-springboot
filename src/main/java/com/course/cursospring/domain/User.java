package com.course.cursospring.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.course.cursospring.domain.enums.ProfileEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table (name = User.TABLE_NAME)
@Data 
@NoArgsConstructor
@AllArgsConstructor
public class User {
    public static final String TABLE_NAME = "users";

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "id", unique = true)
    private Long id;


    @Column (name = "username", length = 100, nullable = false, unique = true)
    @NotBlank
    @Size (min = 3, max = 100)
    private String username;


    @JsonProperty (access = Access.WRITE_ONLY)
    @Column (name = "password", length = 80, nullable = false)
    @NotBlank
    @Size (min = 8, max = 80)
    private String password;


    @OneToMany (mappedBy = "user", fetch = FetchType.EAGER)
    @JsonProperty (access = Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<Task>();


    @ElementCollection (fetch = FetchType.EAGER)
    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY)
    @CollectionTable (name = "user_profiles")
    @Column (name = "profile", nullable = false)
    private Set<Integer> profiles = new HashSet<>();

    public Set<ProfileEnum> getProfiles() {
        return this.profiles.stream().map(profileCode -> ProfileEnum.toEnum(profileCode)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profile) {
        this.profiles.add(profile.getCode());
    }
}
