'use client'

import { Template, InputText, Button, RenderIf, useNotification, FieldError } from "components";
import { useImageService } from "resources/image/image.service";
import { PhotoIcon } from '@heroicons/react/24/solid';
import Link from "next/link";
import { useState } from "react";
import { useFormik } from "formik";
import { FormProps, formScheme, formValidationScheme } from "./formScheme";
import { AuthenticatedPage } from "components";

export default function formPage() {
    const notification = useNotification();
    const [previewFile, setPreviewFile] = useState<string>('');
    const service = useImageService();

    const handleSubmit = async (values: FormProps, { resetForm }: any) => {
        const formData = new FormData();
    
        // Os nomes colocados aqui são os mesmos nomes que estão no SaveImageDTO (Backend), é necessário para funcionar
        formData.append("file", values.file);
        formData.append("name", values.title);
        formData.append("tags", values.tags);
    
        await service.save(formData);
    
        resetForm();
        setPreviewFile("");
    
        notification.notify("Imagem adicionada com sucesso!", "success");
    };

    const formik = useFormik<FormProps>({
        initialValues: formScheme,
        onSubmit: handleSubmit,
        validationSchema: formValidationScheme,
    });
    
    const onFileUpdate = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files) {
            const file = e.target.files[0];
            formik.setFieldValue("file", file);
            setPreviewFile(URL.createObjectURL(file));
        }
    };

    return (
        <AuthenticatedPage>
            <Template>
                <section className="flex flex-col items-center justify-center my-5">
                    <h5 className="mt-3 mb-10 text-3x1 font-extrabold tracking-tight text-gray-900">Nova Imagem</h5>

                    <form onSubmit={formik.handleSubmit}>
                        <div className="grid grid-cols-1">
                            <label className="block text-sm font-medium leading-6 text-gray-700">
                                Título: 
                            </label>
                            <InputText
                                id="titulo"
                                name="title"
                                value={formik.values.title}
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                placeholder="Digite o nome da imagem"
                            />
                            <FieldError error={formik.errors.title} />
                        </div>

                        <div className="mt-5 grid grid-cols-1">
                            <label className="block text-sm font-medium leading-6 text-gray-700">
                                Tags: 
                            </label>
                            <InputText
                                id="tags"
                                name="tags"
                                value={formik.values.tags}
                                onChange={formik.handleChange}
                                onBlur={formik.handleBlur}
                                placeholder="Digite o nome das tags separados por vírgula"
                                style="w-96"
                            />
                            <FieldError error={formik.errors.tags} />
                        </div>

                        <div className="mt-5 grid grid-cols-1">
                            <label className="block text-sm font-medium leading-6 text-gray-700">
                                Imagem: 
                            </label>
                            <FieldError error={formik.errors.file} />
                            
                            <div className="mt-2 flex justify-center rounded-lg border border-dashed border-gray-900/25 px-6 py-10">
                                <div className="text-center">
                                    <RenderIf condition={!previewFile}>
                                        <PhotoIcon aria-hidden="true" className="mx-auto size-12 text-gray-300" />
                                    </RenderIf>
                                    <RenderIf condition={!!previewFile}>
                                        <img src={previewFile} width={250} className="rounded-mb" />
                                    </RenderIf>
                                    <div className="mt-4 flex text-sm leading-6 text-gray-600">
                                        <label className="relative cursor-pointer rounded-md bg-white font-semibold text-indigo-600">
                                            <RenderIf condition={!previewFile}>
                                                <span>Clique para carregar a imagem</span>
                                            </RenderIf>
                                            <input
                                                type="file"
                                                name="file"
                                                onChange={onFileUpdate}
                                                className="sr-only"
                                            />
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="mt-6 flex items-center justify-center gap-x-4">
                            <Link href="/gallery">
                                <Button
                                    style="bg-red-500 hover:bg-red-300"
                                    type="button"
                                    label="Cancelar"
                                />
                            </Link>
                            <Button
                                style="bg-blue-500 hover:bg-blue-300"
                                type="submit"
                                label="Salvar"
                            />
                        </div>
                    </form>
                </section>
            </Template>
        </AuthenticatedPage>
    );
}
