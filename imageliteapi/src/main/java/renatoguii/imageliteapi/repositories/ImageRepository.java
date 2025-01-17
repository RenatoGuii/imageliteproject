package renatoguii.imageliteapi.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import renatoguii.imageliteapi.entities.image.ImageEntity;
import renatoguii.imageliteapi.entities.image.ImageExtension;

import java.util.List;

import static org.springframework.data.jpa.domain.Specification.anyOf;
import static org.springframework.data.jpa.domain.Specification.where;
import static renatoguii.imageliteapi.repositories.specs.GenericSpecs.conjunction;
import static renatoguii.imageliteapi.repositories.specs.ImageSpecs.*;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,String>, JpaSpecificationExecutor<ImageEntity> {


    // SELECT * FROM IMAGE WHERE 1 = 1 AND EXTENSION = 'PNG' AND (NAME LIKE 'QUERY' OR TAGS LIKE 'QUERY' )
    default List<ImageEntity> findByExtensionAndNameOrTagsLike(ImageExtension extension, String query) {
        //SELECT * FROM IMAGE WHERE 1 = 1
        // Inicialização da Specification com a condição inicial (conjunction)
        Specification<ImageEntity> spec = where(conjunction());

        if (extension != null) {
            //AND EXTENSION = 'PNG'
            //Adiciona a nova condição ao Spec principal
            spec = spec.and(extensionEqual(extension));
        }

        if (StringUtils.hasText(query)) {
            //AND (NAME LIKE 'QUERY' OR TAGS LIKE 'QUERY')
            //allOf indica que todos os critérios devem ser atendidos
            //anyOf indica que qualquer um dos critérios podem ser atendidos (Esse foi usado)
            //Adiciona essa condição ao Spec principal
            spec = spec.and(anyOf(nameLike(query), tagsLike(query)));
        }

        return findAll(spec);
    }

}
