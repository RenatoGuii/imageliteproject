import React from "react";

interface ButtonProps {
    style?: string;
    label: string
    type?: "submit" | "button" | "reset" | undefined
    onClick?: (e: any) => void;
}

export const Button: React.FC<ButtonProps> = ({style, label, type,  onClick}: ButtonProps) => {
    return (
        <>
            <button 
            className={`${style} text-white px-4 py-2 rounded-lg duration-500`}
            type={type}
            onClick={onClick}>
                {label}
            </button>
        </>
    )
}

