package org.hubert.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hubert.common.enums.RoleEnum;

/**
 * Represents a role in the application.
 * This class is mapped to the database table "t_role" using JPA annotations.
 *
 * @author hubertwong
 * @version 1.0
 * @since 2024/10/29 22:44
 */
@Data
@Entity
@Table(name = "t_role")
public class Role {

    /**
     * The unique identifier for a Role entity. This ID is auto-generated
     * using the strategy defined by {@link GenerationType#IDENTITY}.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the role.
     * It uses the ordinal value of {@link RoleEnum} and is stored as an integer in the database.
     */
    @Enumerated(EnumType.ORDINAL)
    @Column(length = 20)
    private RoleEnum name;
}
