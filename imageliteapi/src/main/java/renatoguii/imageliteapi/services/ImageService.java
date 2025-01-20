package renatoguii.imageliteapi.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import renatoguii.imageliteapi.dtos.image.ImageJsonDTO;
import renatoguii.imageliteapi.dtos.image.SaveImageDTO;
import renatoguii.imageliteapi.entities.image.ImageEntity;
import renatoguii.imageliteapi.entities.image.ImageExtension;
import renatoguii.imageliteapi.repositories.ImageRepository;

import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    public ImageEntity saveImage(SaveImageDTO data) throws IOException {
        ImageEntity newImage = ImageEntity.builder()
                .name(data.name())
                .size(data.file().getSize())
                .extension(ImageExtension.ofValue(MediaType.valueOf(Objects.requireNonNull(data.file().getContentType()))))
                .tags(String.join(",", data.tags()))
                .file(data.file().getBytes())
                .build();
        return imageRepository.save(newImage);
    }

    public Map<String, Object> getImageById(String id) {
        Optional<ImageEntity> possibleImage = imageRepository.findById(id);

        if (possibleImage.isEmpty()) {
            throw new EntityNotFoundException("Image not found for the provided id");
        }

        ImageEntity image = possibleImage.get();

        // Adicionando informações adicionais aos Headers da requisição HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(image.getExtension().getMediaType()); // Isso que garante que será renderizado uma imagem
        headers.setContentLength(image.getSize());
        // Define se a imagem vai ser renderizada na página (inline()) ou vai ser baixada diretamente (attachment())
        headers.setContentDisposition(ContentDisposition.inline().filename(image.getFileName()).build());

        // Encapsulando os dados
        Map<String, Object> response = new HashMap<>();
        response.put("file", image.getFile()); // Conteúdo do arquivo
        response.put("headers", headers);      // Headers HTTP

        return response;
    }

    // Função que pesquisa images e retorna uma lista dessas images convertidas em ImageJsonDTO
    public List<ImageJsonDTO> search(ImageExtension extension, String query) {
        List<ImageEntity> listImages = imageRepository.findByExtensionAndNameOrTagsLike(extension, query);

        if (listImages.isEmpty()) {
            throw new EntityNotFoundException("There is no record of images with these conditions");
        }

        // Conversão da lista de ImageEntity para uma lista de ImageJsonDTO
        List<ImageJsonDTO> listImageJsonDto = listImages.stream().map(image -> {
            URI imageUrl = buildImageUri(image);
            ImageJsonDTO imageDto = imageToJson(image, imageUrl);
            return imageDto;
        }).collect(Collectors.toList());
        return listImageJsonDto;
    }

    // Cria uma instância de um ImageJsonDTO
    public ImageJsonDTO imageToJson(ImageEntity image, URI url) {
        return ImageJsonDTO.builder()
                .Url(url.toString())
                .name(image.getName())
                .extension(image.getExtension().name())
                .size(image.getSize())
                .uploadDate(image.getUploadData().toLocalDate())
                .build();
    }

    // Função que resgata a URL da imagem criada
    public URI buildImageUri(ImageEntity image) {
        String imagePath = "/" + image.getId();
        return ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path(imagePath)
                .build().toUri();
    }

}
