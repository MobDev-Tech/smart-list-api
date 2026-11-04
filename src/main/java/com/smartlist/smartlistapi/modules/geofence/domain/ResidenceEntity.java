package com.smartlist.smartlistapi.modules.geofence.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "residences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResidenceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JdbcTypeCode(SqlTypes.INTEGER)
    private Long id;

    @Column(nullable = false)
    private String address;

    private String complement;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Column(name = "radius_meters", nullable = false)
    private Integer radiusMeters; // Ex: 150m (entre 100m e 300m)

    @Column(name = "geofence_active", nullable = false)
    private boolean geofenceActive;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        this.updatedAt = LocalDateTime.now();
    }
}