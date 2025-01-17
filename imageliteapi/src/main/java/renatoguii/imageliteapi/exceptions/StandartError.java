package renatoguii.imageliteapi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StandartError {

    private Instant timeStamp;
    private Integer status;
    private String error;
    private String msg;
    private String path;

}
