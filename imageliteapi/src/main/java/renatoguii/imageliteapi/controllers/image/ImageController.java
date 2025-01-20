package renatoguii.imageliteapi.controllers.image;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import renatoguii.imageliteapi.dtos.image.ImageJsonDTO;
import renatoguii.imageliteapi.dtos.image.SaveImageDTO;
import renatoguii.imageliteapi.entities.image.ImageEntity;
import renatoguii.imageliteapi.entities.image.ImageExtension;
import renatoguii.imageliteapi.services.ImageService;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Tag(name = "Images", description = "APIs para gerenciamento de imagens")
@RestController
@RequestMapping("/v1/images")
public class ImageController {

    @Autowired
    ImageService imageService;

    @Operation(summary = "Salvar uma nova imagem", description = "Registra uma nova imagem no sistema e retorna o URI da imagem criada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Imagem salva com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = URI.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<URI> save(@RequestBody(description = "Dados da imagem a ser salva", required = true)
                                    @Valid SaveImageDTO data) throws IOException {
        ImageEntity newImage = imageService.saveImage(data);
        return ResponseEntity.created(imageService.buildImageUri(newImage)).build();
    }

    @Operation(summary = "Obter uma imagem pelo ID", description = "Retorna os dados binários de uma imagem com base no ID fornecido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagem retornada com sucesso",
                    content = @Content(mediaType = "application/octet-stream")),
            @ApiResponse(responseCode = "404", description = "Imagem não encontrada",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    @GetMapping("{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable("id") String id) {
        Map<String, Object> response = imageService.getImageById(id);

        byte[] file = (byte[]) response.get("file");
        HttpHeaders headers = (HttpHeaders) response.get("headers");

        return new ResponseEntity<>(file, headers, HttpStatus.OK);
    }

    @Operation(summary = "Buscar imagens", description = "Realiza uma busca por imagens com base em extensão e uma query opcional.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagens retornadas com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ImageJsonDTO.class))),
            @ApiResponse(responseCode = "400", description = "Parâmetros de busca inválidos",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ImageJsonDTO>> search(
            @RequestParam(value = "extension", required = false, defaultValue = "") String extension,
            @RequestParam(value = "query", required = false) String query) {
        List<ImageJsonDTO> listImages = imageService.search(ImageExtension.ofName(extension), query);
        return ResponseEntity.status(HttpStatus.OK).body(listImages);
    }
}
