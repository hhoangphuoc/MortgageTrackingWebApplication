import { BaseResponse, neverText } from "./Response";
import { Status } from "./Status";

const NETWORK_ERROR = "NetworkError when attempting to fetch resource.";

export function myFetch(url: string, init?:MyRequestInit)
    : Promise<BaseResponse>
{
    // return myFetchFetch(url, init as RequestInit);
    if (url.startsWith("/")){
        return myFetchXMLHttp(url.replace("/", "./"), init);
    } else {
        return myFetchXMLHttp(url, init);
    }
}

function myFetchFetch(input: RequestInfo, init?:RequestInit): Promise<BaseResponse> {
    return fetch(input, init)
    .catch(err => {
        const errMsg = NETWORK_ERROR;
        return {
            ok: false,
            status: Status.NETWORK_ERROR,
            statusText: errMsg,
            text: async () => errMsg,
            json: async () => err,
    }});
}

function myFetchXMLHttp(url: string, init:MyRequestInit = {})
    : Promise<BaseResponse>
{
return new Promise((resolve) => {
    const request = new XMLHttpRequest();
    request.open(init?.method ?? Method.GET, url, true);
    request.onload = () => {
        resolve(new XmlResponse(request))
    };
    request.onerror = () => {
        const errMsg = NETWORK_ERROR;
        resolve({
            ok: false,
            status: Status.NETWORK_ERROR,
            statusText: errMsg,
            text: async () => errMsg,
            json: async () => {},
        });
    };
    const headers = init?.headers ?? {};
    for (const name in headers){
        request.setRequestHeader(name, headers[name]);
    }
    request.send(init?.body);
});
    
        
}
export enum HeaderName {CONTENT_TYPE = "Content-Type", ACCEPT = "Accept"}
export enum Method {
    GET = "GET", POST = "POST", DELETE = "DELETE",
    PUT = "PUT", PATCH = "PATCH"
}
type MyHeadersInit = Partial<Record<HeaderName, string>>;


interface MyRequestInit {
    method?: string,
    body?: Blob | BufferSource | FormData | URLSearchParams | string;
    headers?: MyHeadersInit,
}

class XmlResponse{
    constructor(
        private request: XMLHttpRequest,
    ){
        console.assert(request.readyState == 4);
    }
    get status(){
        return this.request.status;
    }
    get statusText(){
        return this.request.statusText;
    }
    get ok(){
        return this.status >= 200 && this.status < 300;
    }
    async text(){
        return this.request.responseText;
    }
    json(){
        return this.text().then(text => JSON.parse(text));
    }
}
