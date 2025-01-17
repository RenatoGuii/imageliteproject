package renatoguii.imageliteapi.repositories.specs;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecs {

    private GenericSpecs (){}

    // Função que gera uma condição sempre verdadeira (1 = 1)
    public static <T> Specification<T> conjunction() {
        return (root, q, criteriaBuilder) -> criteriaBuilder.conjunction();
    }

}
