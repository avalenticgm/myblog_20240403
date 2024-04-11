package it.cgmconsulting.myblog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Avatar {

    @EmbeddedId
    @EqualsAndHashCode.Include
    private AvatarId avatarId;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false, length = 10)
    private String filetype;

    @Lob
    @Column(nullable = false, length = 65535) // BLOB -> Binary Long OBject; from 0 to 65535 bytes
    private byte[] data;
}
