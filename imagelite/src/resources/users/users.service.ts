import { AccessToken, Credentials, User, UserSessionToken } from "./users.resource"
import * as jwt from "jwt-decode"

class AuthService {
    baseURL: string = `${process.env.NEXT_PUBLIC_API_URL}/v1/users`
    static AUTH_PARAM: string = "_auth"

    async authenticate(credentials: Credentials) : Promise<AccessToken> {
   
        const response = await fetch(`${this.baseURL}/auth`, {
            method: "POST",
            body: JSON.stringify(credentials), // Transforma em String JSON
            headers: {
                "Content-Type": "application/json" // Especifica ao servidor que o dados recebidos devem ser analisados em formato JSON
            }
        });

        if (response.status == 401) {
            throw new Error("Email ou senha estão incorretas!"); 
        } 

        return await response.json();
    }

    async save(user: User) : Promise<void> {

        const response = await fetch(this.baseURL, {
            method: "POST",
            body: JSON.stringify(user),
            headers: {
                "Content-Type": "application/json"
            }
        });

        if (response.status == 404) {
            throw new Error("Esse email já esta sendo utilizado");
        }

    }

    initSession(token: AccessToken) {
        if(token.accessToken){
            const decodedToken: any = jwt.jwtDecode(token.accessToken);

            console.log(decodedToken);

            const userSessionToken: UserSessionToken = {
                accessToken: token.accessToken,
                name: decodedToken.name, // Colocar o nome do claim
                email: decodedToken.sub, // Busca a informação do subject (nesse caso o email)
                expiration: decodedToken.exp // Busca a data de expiração do Token
            }

            this.setUserSession(userSessionToken);
        }
    }

    setUserSession(userSessionToken: UserSessionToken) {
       try {
        localStorage.setItem(AuthService.AUTH_PARAM, JSON.stringify(userSessionToken)); 
       } catch (error){}
    }

    getUserSessionToken(): UserSessionToken | null {
        try {
            const authString = localStorage.getItem(AuthService.AUTH_PARAM);

            if (!authString) {
                return null;
            }
    
            const token: UserSessionToken = JSON.parse(authString);
    
            return token;
        } catch (error) {
            return null;
        }
    }

    isSessionValid() {
        const userSession: UserSessionToken | null = this.getUserSessionToken();

        if (!userSession) {
            return false;
        }

        const expiration: number | undefined = Number(userSession.expiration);

        if (expiration) {
            // Converte Segundos em Milissegundos
            const expirationDateInMillis = expiration * 1000;

            return new Date() < new Date(expirationDateInMillis);
        }

        return false;
    }

    invalidateSession(): void {
        try {
            localStorage.removeItem(AuthService.AUTH_PARAM);
        } catch (error) {}
    }
 
}

export const useAuth = () => new AuthService();