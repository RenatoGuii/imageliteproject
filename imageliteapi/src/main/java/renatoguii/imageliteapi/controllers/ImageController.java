package renatoguii.imageliteapi.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import renatoguii.imageliteapi.dtos.ImageJsonDTO;
import renatoguii.imageliteapi.dtos.SaveImageDTO;
import renatoguii.imageliteapi.entities.image.ImageEntity;
import renatoguii.imageliteapi.entities.image.ImageExtension;
import renatoguii.imageliteapi.services.ImageService;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/images")
// Configura a escolha dos domínios que serão permitidos fazer requisições
// @CrossOrigin("*") -> Configuração feita globalmente em SecurityConfig
public class ImageController {

    @Autowired
    ImageService imageService;

    @PostMapping
    public ResponseEntity<URI> save(@Valid SaveImageDTO data) throws IOException {
        ImageEntity newImage = imageService.saveImage(data);
        return ResponseEntity.created(imageService.buildImageUri(newImage)).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        Map<String, Object> response = imageService.getImageById(id);

        byte[] file = (byte[]) response.get("file");
        HttpHeaders headers = (HttpHeaders) response.get("headers");

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ImageJsonDTO>> search(@RequestParam(value = "extension", required = false, defaultValue = "") String extension, @RequestParam(value = "query", required = false) String query) {
        List<ImageJsonDTO> listImages = imageService.search(ImageExtension.ofName(extension), query);
        return ResponseEntity.status(HttpStatus.OK).body(listImages);
    }



}
