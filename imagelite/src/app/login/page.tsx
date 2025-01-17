'use client'
 
import { Template, RenderIf, InputText, Button, FieldError, useNotification } from "components"
import { useState } from "react"
import { useFormik } from "formik";
import { credentialScheme, CredentialsProps, credentialValidationScheme } from "./formScheme";
import { useAuth } from "resources";
import { useRouter } from "next/navigation"
import { AccessToken } from "resources/users/users.resource";

export default function loginPage() {

    const [loading, setLoading] = useState<boolean>(false);
    const [newUserState, setNewUserState] = useState<boolean>(true);

    const auth = useAuth();
    const notification = useNotification();
    const router = useRouter();

    const handleSubmit = async (values: CredentialsProps) => {
        setLoading(true);

        if (!newUserState) {

            const credentials: CredentialsProps = {
                email: formik.values.email,
                password: formik.values.password
            }

            try {

                const accessToken: AccessToken = await auth.authenticate(credentials);

                auth.initSession(accessToken);
                
                router.push("/gallery")

            } catch (error: any) {

                const message = error?.message;
                notification.notify(message, "error");

            }

        } else {

            const user: CredentialsProps = {
                name: formik.values.name,
                email: formik.values.email,
                password: formik.values.password
            }

            try {
                await auth.save(user);
                notification.notify("Usuário cadastrado com sucesso!", "success");
                formik.resetForm();
                setNewUserState(false);
            } catch(error: any) {
                const message = error?.message;
                notification.notify(message, "error");
                console.log(message)
            }

        }

        setLoading(false);
    }

    const formik = useFormik<CredentialsProps>({
        initialValues: credentialScheme,
        onSubmit: handleSubmit,
        validationSchema: credentialValidationScheme,
    });

    return (
        <Template loading={loading}>

            <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">

                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                    <h2 className="mt-10 text-center text-xl font-bold leading-9 tracking-tight text-gray-900">
                            {newUserState ? "Cadastre sua nova conta" : "Faça login na sua conta"}
                    </h2>
                </div>

                <div className="mt-10 sm:mx-auto sm:w-full sm:max-w-sm">
                    <form onSubmit={formik.handleSubmit} className="space-y-2">

                        <RenderIf condition={newUserState}>
                            <div>
                                <label className="block text-sm font-medium leading-6 text-gray-900">Nome:</label>
                            </div>
                            <div className="mt-2">
                                <InputText style="w-full" id="name" name="name" value={formik.values.name} onChange={formik.handleChange} /> 
                            </div>
                            <FieldError error={formik.errors.name} />
                        </RenderIf>

                        <div>
                            <label className="block text-sm font-medium leading-6 text-gray-900">Email:</label>
                        </div>
                        <div className="mt-2">
                            <InputText style="w-full" id="email" name="email" value={formik.values.email} onChange={formik.handleChange} /> 
                        </div>
                        <FieldError error={formik.errors.email} />

                        <div>
                            <label className="block text-sm font-medium leading-6 text-gray-900">Senha:</label>
                        </div>
                        <div className="mt-2">
                            <InputText style="w-full" type="password" id="password" name="password" value={formik.values.password} onChange={formik.handleChange} /> 
                        </div>
                        <FieldError error={formik.errors.password} />

                        <RenderIf condition={newUserState}>
                            <div>
                                <label className="block text-sm font-medium leading-6 text-gray-900">Repita a senha:</label>
                            </div>
                            <div className="mt-2">
                                <InputText style="w-full" type="password" id="passwordMatch" name="passwordMatch" value={formik.values.passwordMatch} onChange={formik.handleChange} /> 
                            </div>
                            <FieldError error={formik.errors.passwordMatch} />

                        </RenderIf>

                        <div>
                            <RenderIf condition={newUserState} >
                                <Button 
                                type="submit" 
                                style="bg-indigo-950 hover:bg-indigo-700" 
                                label="Salvar" />

                                <Button 
                                type="button" 
                                style="bg-red-700 hover:bg-red-600 mx-2" 
                                label="Cancelar" onClick={e => setNewUserState(false)} />
                            </RenderIf>

                            <RenderIf condition={!newUserState} >
                                <Button 
                                type="submit" 
                                style="bg-indigo-950 hover:bg-indigo-700" 
                                label="Entrar" />

                                <Button 
                                type="button" 
                                style="bg-red-700 hover:bg-red-600 mx-2" 
                                label="Cadastrar-se" onClick={e => setNewUserState(true)} />
                            </RenderIf> 
                        </div>
                    </form>
                </div>

            </div>

        </Template>
    )
}