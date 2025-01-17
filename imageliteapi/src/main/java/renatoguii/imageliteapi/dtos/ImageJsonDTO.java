package renatoguii.imageliteapi.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageJsonDTO {

    private String Url;
    private String name;
    private String extension;
    private Long size;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate uploadDate;
}
