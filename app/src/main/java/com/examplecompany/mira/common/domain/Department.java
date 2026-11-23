package com.examplecompany.mira.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_code", nullable = false, length = 32)
    private String departmentCode;

    @Column(name = "department_name", nullable = false, length = 128)
    private String departmentName;

    @Column(name = "create_datetime", nullable = false)
    private OffsetDateTime createDatetime;

    @Column(name = "update_datetime", nullable = false)
    private OffsetDateTime updateDatetime;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(final String departmentName) {
        this.departmentName = departmentName;
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