import { APPLICATION_JSON } from "../../util";
import { Method, myFetch } from "../myFetch";
import {errorText, neverText as neverText, okEmpty, okJson} from "../Response";
import { Status } from "../Status";
export interface UserLoginPost {
    email: string,
    password: string
}
export class UserRegisterPost {
    firstName!: String;
    lastName!: String;
    email!: String;
    password!: String;
}
export class UserGet {
    id!: number;
    first_name!: string;
    last_name!: string;
    email!: string;
    is_staff!: boolean;
    profile_img?: 0
}


export async function userLoginPost(body: UserLoginPost)
{
    const res = await myFetch("api/user/login", {
        method: "POST",
        headers: {
            "Content-Type": APPLICATION_JSON,
        },
        body: JSON.stringify(body)
    });
    switch (res.status){
        case Status.OK: return okJson(res, UserGet, res.status);
        case Status.NOT_FOUND: return errorText(res, res.status);
        case Status.UNAUTHORIZED: return errorText(res, res.status);
        default: return neverText(res);
    }
}

export async function userLogout(){
    const res = await myFetch("api/user/logout", {method: Method.DELETE});
    switch (res.status){
        case Status.NO_CONTENT: return okEmpty(res, res.status);
        default: return neverText(res);
    }
}

export async function userGet
    (u_id: number)
{
    const res = await myFetch(`api/user/${u_id}`);
    switch (res.status){
        case Status.OK:
            return okJson(res, UserGet, res.status);
        case Status.NOT_FOUND: return errorText(res, res.status);
        case Status.UNAUTHORIZED: return errorText(res, res.status);
        default: return neverText(res);
    }
}
export async function userRegisterPost(body: UserRegisterPost) {
    const res = await myFetch("api/user/register", {
        method: "POST",
        headers: {
            "Content-Type": APPLICATION_JSON,
        },
        body: JSON.stringify(body)
    });
    switch (res.status) {
        case Status.CREATED:
            console.log(res.status);
            return okEmpty(res, res.status);
        case Status.BAD_REQUEST:
            return errorText(res, res.status);
        default:
            return neverText(res);
    }
}
