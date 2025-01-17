import { useAuth } from "resources"
import { useRouter } from "next/navigation"
import { useEffect } from "react"

interface AuthenticatedPageProps {
    children: React.ReactNode
}

export const AuthenticatedPage: React.FC<AuthenticatedPageProps> = ({children}) => {

    const auth = useAuth();
    const router = useRouter();

    useEffect(() => {
        if(!auth.isSessionValid()) {
            router.push("/login")
        }
    }, [])

    return (
        <>
            {children}
        </>
    )
}