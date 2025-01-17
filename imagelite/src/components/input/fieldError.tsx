import { error } from "console";

interface FieldErrorProps {
    error: any | null
}

export const FieldError: React.FC<FieldErrorProps> = ({error}: FieldErrorProps) => {

    if(error) {
        return (
            <span className="text-red-500 text-sm mt-1" >{error}</span>
        )
    }

    return false;
}