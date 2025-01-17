import { Image } from "./image.resource";
import { useAuth } from "resources";

class ImageService {
    baseURL: string = `${process.env.NEXT_PUBLIC_API_URL}/v1/images`;
    auth = useAuth();

    // Uma função async sempre retornará uma promise
    // string = "" permite que o parâmetro seja opcional
    async search(query: string = "", extension: string = ""): Promise<Image[]> {
        const userSession = this.auth.getUserSessionToken();
        const url = `${this.baseURL}?extension=${extension}&query=${query}`
        
        try {
            // Await garante o resultado do método antes de passar pra próxima linha
            // Response não é o array de imagens, se trata do array de resposta da requisição, cujo o array de imagens fica dentro dele
            // Por padrão o fetch é um GET
            const response = await fetch(url, {
                headers: {
                    "Authorization": `Bearer ${userSession?.accessToken}`
                }
            });

            if (response.status === 404) {
                return [];
            }
    
            if (response.ok && response.headers.get("Content-Length") !== "0") {
                return await response.json();
            }
    
            return []; 
        } catch (error) {
            console.error("Error fetching images:", error);
            return [];
        }
        
    }

    async save(dados: FormData) : Promise<string | null> {
        const userSession = this.auth.getUserSessionToken();

        try {
            const response = await fetch(this.baseURL, {
                method: 'POST',
                headers: {
                    "Authorization": `Bearer ${userSession?.accessToken}`
                },
                body: dados
            })
            return response.headers.get('location');
        } catch (error) {
            console.error("Error saving image: ", error);
            return null;
        }

    }


}

// Instância única para toda aplicação
export const useImageService = () => new ImageService();