import { Class } from "../util";

export interface BaseResponse{
    ok: boolean,
    status: number,
    statusText: string;
    text(): PromiseLike<string>,
    json(): PromiseLike<any>,
}

export class UnexpectedResponse extends Error {

}

export function okText<S extends number>
    (res: BaseResponse, status?: S)
{
    return textResponse(res, status, true)
}
export async function neverText(res: BaseResponse) {
    return {
        ok: undefined,
        status: undefined,
        neverStatus: res.status,
        statusText: res.statusText,
        content: await res.text()
    } as const;
}

export function errorText<S extends number>
    (res: BaseResponse, status?: S)
{
    return textResponse(res, status, false);
}

export function okJson<T extends Class | Class[], S extends number>
    (res: BaseResponse, type: T, status: S)
{
    return jsonResponse(res, type, status, true);
}
export function errorJson<T extends Class | Class[], S extends number>
    (res: BaseResponse, type: T, status: S)
{
    return jsonResponse(res, type, status, false);
}

export function okEmpty<S extends number>
    (res: BaseResponse, status: S)
{
    return response(res, status, true);
}
export function errorEmpty<S extends number>
    (res: BaseResponse, status: S)
{
    return response(res, status, false);
}

async function response<S extends number, O extends boolean>
    (res: BaseResponse, status?: S, ok?: O)
{
    return {
        ok: ok ?? res.ok as O,
        status: status ?? res.status as S,
        statusText: res.statusText,
    } as const;
}

async function textResponse<S extends number, O extends boolean>
    (res: BaseResponse, status?: S, ok?: O)
{
    return {
        ok: ok ?? res.ok as O,
        status: status ?? res.status as S,
        statusText: res.statusText,
        content: await res.text()
    } as const
}

async function jsonResponse<
    T extends Class | Class[], 
    S extends number|unknown,
    O extends boolean|unknown,
>
(res: BaseResponse, type: T, status?: S, ok?: O)
{
    return {
        ok: ok ?? res.ok as O,
        status: status ?? res.status as S,
        statusText: res.statusText,
        content: await res.json() as T extends Class
            ? InstanceType<T> : (
                T extends Array<Class> ? InstanceType<T[number]>[] : never
            )
    } as const;
}

function revive(json: {}, type: Class | Class[])
{
    if (Array.isArray(type)){
        if (!Array.isArray(json)){
            console.error(json, "does not match type:", type);
            return json;
        }
        return (json as {}[]).map(element => {
            return revive(element, type[0]);
        });
    } else {
        const revived = new type();
        for (const name in revived){
            if (!(name in json)){
                console.error(name, "is missing on", json);
                continue;
            }
            revived[name] = json[name];
        }
        return revived;
    }
}
