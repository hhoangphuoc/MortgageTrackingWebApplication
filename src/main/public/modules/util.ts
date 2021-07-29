import { MediaType } from "./api/MediaType";

export function clean(text: string){
    const div = document.createElement("div");
    div.textContent = text;
    return div.innerHTML;
}

export function formToJson(form: HTMLFormElement){
    const data = new FormData(form);
    return formDataToJson(data);
}
function formDataToJson(data: FormData){
    return Object.fromEntries(
        [...data.keys()].map((key) => [key, data.get(key)])
    );
}

/**
 * @deprecated
 */
export const APPLICATION_JSON = MediaType.APPLICATION_JSON;

export type Class = {new(...args: any[]): any};

export type ValueOf<T extends Record<any, any>> = T[keyof T];

export { element } from "./render/element";

export function lowercaseObject<T extends {}>(obj: T)
    : {[K in keyof T]: string}
{
    const result = {} as {[K in keyof T]: string}
    for (const key in obj){
        const value = (""+obj[key]).toLowerCase();
        result[key] = value;
    }
    return result;
}

export function findInObject(obj: {[key: string]: string}, search: string) {
    for (const key in obj) {
        if (obj[key].includes(search)) {
            return true;
        }
    }

    return false;
}