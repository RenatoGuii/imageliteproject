import React from "react"

interface InputTextProps {
    id?: string;
    type?: string;
    name?: string;
    style?: string;
    onBlur?: any;
    placeholder?: string;
    value?: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

// ... = spread operator
// Nesse caso, ele pega os nomes das props e espalha pelas propriedades do componente HTML que tem os MESMOS nomes

export const InputText: React.FC<InputTextProps> = ({style, type = "text", ...rest}: InputTextProps) => {
    return (
        <input 
        type={type}
        {...rest}
        className={`${style} border px-3 py-2 rounded-lg text-gray-900`} />
    )
}