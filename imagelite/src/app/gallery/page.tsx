// 'use client' serve pra indicar componentes de uso do cliente (componentes que não são do servidor)
'use client'

import { Template, ImageCard, Button, InputText, useNotification } from "components"
import { useState, useEffect } from "react"
import { Image } from "resources/image/image.resource";
import { useImageService } from "resources";
import Link from "next/link";
import { AuthenticatedPage } from "components";

export default function galleryPage() {
    const notification = useNotification();
    const useService = useImageService();
    const [images, setImages] = useState<Image[]>([]);
    const [query, setQuery] = useState<string>('');
    const [extension, setExtension] = useState<string>('');
    const [loading, setLoading] = useState<boolean>(false)
    
    // O useEffect será executado quando o componente for montado
    useEffect(() => {

        const fetchImages = async () => {
            setLoading(true);
            const result = await useService.search(query, extension);
            setImages(result);
            setLoading(false);
        };

        fetchImages();

    }, []); // o Array vazio representa que a lógica será rodada apenas uma vez após a renderização da página

    async function searchImages() {
        setLoading(true);

        const result = await useService.search(query, extension);
        setImages(result);

        console.table(result);
        setLoading(false);

        if (!result.length) {
            notification.notify("Nenhum registro encontrado", "warning");
        }

    }

    function renderEmptyState() {
        return (
            <div className="my-10 flex flex-col items-center justify-center text-centerp-6">
                <div className="mb-4">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth={1.5} stroke="currentColor" className="w-20 text-black">
                        <path strokeLinecap="round" strokeLinejoin="round" d="m20.25 7.5-.625 10.632a2.25 2.25 0 0 1-2.247 2.118H6.622a2.25 2.25 0 0 1-2.247-2.118L3.75 7.5m6 4.125 2.25 2.25m0 0 2.25 2.25M12 13.875l2.25-2.25M12 13.875l-2.25 2.25M3.375 7.5h17.25c.621 0 1.125-.504 1.125-1.125v-1.5c0-.621-.504-1.125-1.125-1.125H3.375c-.621 0-1.125.504-1.125 1.125v1.5c0 .621.504 1.125 1.125 1.125Z" />
                    </svg>
                </div>
                <h2 className="text-lg font-medium text-gray-900">Nenhum resultado encontrado</h2>
            </div>
        )
    }

    function renderImageCard(image: Image) {
        return <ImageCard key={image.url} name={image.name} src={image.url} uploadDate={image.uploadDate} size={image.size} extension={image.extension} />;
    }
    
    function renderImageCards() {
        if(images.length == 0) {
            return renderEmptyState();
        }
        return images.map(image => renderImageCard(image));
    }

    return (
        <AuthenticatedPage>
            <Template loading={loading}>

                <section className="flex flex-col items-center justify-center my-5">

                    <div className="flex space-x-4">

                        <InputText value={query} placeholder="Ex: Flor" onChange={e => setQuery(e.target.value)} />

                        <select value={extension} onChange={e => setExtension(e.target.value)} className="border px-4 py-2 rounded-lg text-gray-900">

                            <option value="">Todos os formatos</option>
                            <option value="PNG">PNG</option>
                            <option value="JPEG">JPEG</option>
                            <option value="GIF">GIF</option>

                        </select>

                        <Button 
                        label="Buscar" 
                        style="bg-blue-500 hover:bg-blue-300" 
                        onClick={searchImages} />

                        <Link href="/form">
                            <Button 
                            label="Adicionar Imagem" 
                            style="h-full bg-yellow-500 hover:bg-yellow-300" 
                            />
                        </Link>

                    </div>

                </section>

                <section className={`grid ${images.length === 0 ? 'grid-cols-1' : 'grid-cols-3'} gap-8`}>

                    {renderImageCards()}
                    
                </section>

            </Template>
        </AuthenticatedPage>
    )
}