package renatoguii.imageliteapi.dtos;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record SaveImageDTO(@RequestParam("file") MultipartFile file     ,
                           @RequestParam("name") String name,
                           @RequestParam("tags") List<String> tags) {
}
