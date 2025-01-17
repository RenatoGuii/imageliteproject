package renatoguii.imageliteapi.entities.image;

import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.Arrays;

@Getter
public enum ImageExtension {
    PNG(MediaType.IMAGE_PNG),
    JPEG(MediaType.IMAGE_JPEG),
    GIF(MediaType.IMAGE_GIF);

    private MediaType mediaType;

    ImageExtension(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public static ImageExtension ofValue(MediaType mediaType) {
        return Arrays.stream(values()).filter(ie -> ie.mediaType.equals(mediaType)).findFirst().orElse(null);
    }

    public static ImageExtension ofName(String name) {
        return Arrays.stream(values()).filter(ie -> ie.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}
