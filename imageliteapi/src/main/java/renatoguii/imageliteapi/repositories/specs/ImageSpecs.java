package renatoguii.imageliteapi.repositories.specs;

import org.springframework.data.jpa.domain.Specification;
import renatoguii.imageliteapi.entities.image.ImageEntity;
import renatoguii.imageliteapi.entities.image.ImageExtension;

public class ImageSpecs {

    private ImageSpecs () {}

    //Busca imagens que sua respectiva extension seja igual a extension passada no argumento
    public static Specification<ImageEntity> extensionEqual(ImageExtension extension) {
        return (root, q, cb) -> cb.equal(root.get("extension"), extension);
    }

    //Busca imagens cujo nome contém o texto da query
    public static Specification<ImageEntity> nameLike(String query) {
        return (root, q, cb) -> cb.like(cb.upper(root.get("name")), "%" + query.toUpperCase() + "%");
    }

    //Busca imagens que alguma das tags contém o texto da query
    public static Specification<ImageEntity> tagsLike(String query) {
        return (root, q, cb) -> cb.like(cb.upper(root.get("tags")), "%" + query.toUpperCase() + "%");
    }

}
