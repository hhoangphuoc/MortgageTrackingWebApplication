export class MyUrl {
    private urlString: string;
    private url: URL;
    constructor(realHash: string, private autoUpdate = false){
        const hash = this.normHash(realHash);
        this.url = new URL(hash, location.origin);
        this.urlString = this.normHash(hash);
    }
    get pathname(){
        this.beforeGet();
        return this.url.pathname.substr(1);
    }
    set pathname(value: string){
        this.url.pathname = value;
        this.onSet();
    }
    get hash(){
        this.beforeGet();
        return this.normHash(this.url.hash);
    }
    set hash(value: string){
        this.url.hash = value;
        this.onSet();
    }
    getParam(name: string){
        this.beforeGet();
        return this.url.searchParams.get(name);
    }
    setParam(name: string, value: string){
        this.url.searchParams.set(name, value);
        this.onSet();
    }
    get realHash(){
        this.beforeGet();
        return this.pathname + this.url.search + this.url.hash;
    }
    copy(){
        this.beforeGet();
        return new MyUrl(this.realHash);
    }
    navigate(){
        location.hash = this.realHash;
    }
    private normHash(hash: string){
        return (hash || "#").replace("#", "");
    }
    private beforeGet(){
        if (this.autoUpdate){
            const hash = this.normHash(location.hash);
            if (this.urlString !== hash){
                this.url = new URL(hash, location.origin);
                this.urlString = hash;
            }
        }
    }
    private onSet(){
        if (this.autoUpdate){
            this.navigate();
        }
    }
}

export const currentUrl = new MyUrl(location.hash, true);
