import * as Yup from "yup";

export interface CredentialsProps {
    name?: string,
    email: string,
    password: string;
    passwordMatch?: string;
}

export const credentialScheme: CredentialsProps = { name: '', email: '', password: '', passwordMatch: '' };

export const credentialValidationScheme = Yup.object().shape({
    email: Yup.string().trim()
        .required("O email é obrigatório!")
        .email("O email é inválido!"),
    password: Yup.string()
        .required("A senha é obrigatória!")
        .min(8, "A senha deve conter no mínimo 8 caracteres!"),
    passwordMatch: Yup.string()
        .oneOf([Yup.ref("password")], "As senhas devem ser iguais!")
});