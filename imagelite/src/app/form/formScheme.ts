import * as Yup from "yup";

// Blob é um tipo para Arquivos
export interface FormProps {
    title: string;
    tags: string;
    file: string | Blob;
}

// Para que os inputs sejam VIGIADOS, ATUALIZADOS E VALIDADOS, é necessário que a propiedade "name" do input seja o mesmo de uma das chaves do schema
export const formScheme: FormProps = { title: '', tags: '', file: '' };

export const formValidationScheme = Yup.object().shape({
    title: Yup.string().trim()
        .required("Titulo é obrigatório")
        .max(50, "O título possuí um limite de 50 caracteres"),
    tags: Yup.string().trim()
        .required("Tag(s) são obrigatórias")
        .max(50, "A(s) tag(s) possuí(em) um limite de 50 caracteres"),
    file: Yup.mixed<Blob>()
        .required("Selecione imagem para upload")
        .test("size", "A imagem não pode ser maior que 4MB", (file) => {
            return file.size < 4000000;
        })
        .test("type", "Formatos aceitos: jpeg, png ou gif", (file) => {
            return file.type === "image/jpeg" || file.type === "image/png" || file.type === "image/gif";
        })
});

