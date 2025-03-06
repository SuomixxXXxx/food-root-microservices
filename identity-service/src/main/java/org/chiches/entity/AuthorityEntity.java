package org.chiches.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Table(name = "authorities")
public class AuthorityEntity extends BaseEntity implements GrantedAuthority {
    private String authority;

    @ManyToMany(mappedBy = "authorities")
    private List<UserEntity> users;

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }
}