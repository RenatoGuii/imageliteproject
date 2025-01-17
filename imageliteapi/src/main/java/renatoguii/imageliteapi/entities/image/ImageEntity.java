package renatoguii.imageliteapi.entities.image;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "image")
@EntityListeners(AuditingEntityListener.class) // Monitora o @CreatedDate
// Lombok
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column
    private String name;

    @Column
    private Long size;

    @Column
    @Enumerated(EnumType.STRING)
    private ImageExtension extension;

    @Column
    @CreatedDate // Indica que é a data que foi salva no banco de dados
    private LocalDateTime uploadData;

    @Column
    private String tags;

    @Column
    @Lob //Indica que se trata de um arquivo
    private byte[] file;

    public String getFileName() {
        return this.getName().toLowerCase().concat(".").concat(this.getExtension().name().toLowerCase());
    }

}
