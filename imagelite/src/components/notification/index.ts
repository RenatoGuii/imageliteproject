import { toast } from 'react-toastify'

// Hook que retorna a função que por sua vez retorna um toast

export const useNotification = () => {

    function notify(message: string, level: "success" | "info" | "warning" | "error") {

        toast(message, {
            type: level
        })

    }

    return {
        notify
    }

}