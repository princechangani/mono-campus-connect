package com.monocampusconnect.model.postgres;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "rooms")
public class Room extends BaseAuditModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_public_id")
    private UUID roomPublicId;

   @Column(name = "tenant_id")
    private UUID tenantId;

    @Column(name = "building")
    private String building;

    @Column(name = "floor")
    private Integer floor;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "name")
    private String name;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "room_type")
    private String roomType;
}

