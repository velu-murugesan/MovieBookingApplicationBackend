package com.velu.MovieBookingApplication.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name="users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;
    private String email;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonIgnore
    List<Booking> bookingList;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
