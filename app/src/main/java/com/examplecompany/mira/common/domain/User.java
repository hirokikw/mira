package com.examplecompany.mira.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(name = "login_id", nullable = false, length = 64)
    private String loginId;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "user_name", nullable = false, length = 128)
    private String userName;

    @Column(name = "authority_code", nullable = false, length = 32)
    private String authorityCode;

    @Column(name = "last_login_datetime")
    private OffsetDateTime lastLoginDatetime;

    @Column(name = "create_datetime", nullable = false)
    private OffsetDateTime createDatetime;

    @Column(name = "update_datetime", nullable = false)
    private OffsetDateTime updateDatetime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(final Long userId) {
        this.userId = userId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(final Department department) {
        this.department = department;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(final String loginId) {
        this.loginId = loginId;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(final String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public String getAuthorityCode() {
        return authorityCode;
    }

    public void setAuthorityCode(final String authorityCode) {
        this.authorityCode = authorityCode;
    }

    public OffsetDateTime getLastLoginDatetime() {
        return lastLoginDatetime;
    }

    public void setLastLoginDatetime(final OffsetDateTime lastLoginDatetime) {
        this.lastLoginDatetime = lastLoginDatetime;
    }

    public OffsetDateTime getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(final OffsetDateTime createDatetime) {
        this.createDatetime = createDatetime;
    }

    public OffsetDateTime getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(final OffsetDateTime updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}