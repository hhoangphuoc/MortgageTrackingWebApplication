// eslint-disable-next-line
export async function testApi(info: RequestInfo, init?: RequestInit){
    try {
        const res = await fetch(info, init);
        const contentType = res.headers.get("content-type");
        let body: any;
        if (contentType == "application/json"){
            body = await res.json();
        } else {
            body = await res.text();
        }
        if (res.ok){
            console.log(info, init, body);
        } else {
            console.error(info, init, body);
        }
    } catch (e){
        console.error(e);
    }
}
