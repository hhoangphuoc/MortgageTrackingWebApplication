export interface ChatMessage {
    from: number,
    to: number | null,
    content: string,
}

export class ChatSocket {
    private socket: WebSocket;

    constructor(
        readonly from: number,
        readonly to: number | null,
        public messageCallback: (message: ChatMessage) => void,
        public errorCallback: (error: any) => void,
    ){
        const protocol = location.protocol === "http:" ? "ws" : "wss";
        const baseUrl = location.href
            .replace(/http|https/, protocol)
  	        .replace(/#.*$/, "");

        const url = `${baseUrl}chat/${from}`;
        this.socket = new WebSocket(url);
        this.socket.onopen = this.onOpen.bind(this);
        this.socket.onclose = this.onClose.bind(this);
        this.socket.onmessage = this.onMessage.bind(this);
        this.socket.onerror = this.onError.bind(this);
    }
    public send(message: ChatMessage){
        this.socket.send( JSON.stringify(message) );
    }

    private onOpen(){
        console.log("connected to server");
    }
    private onClose(){
        console.log("disconnected from the server");
    }
    private onMessage(event: MessageEvent<any>){
        console.log(event);
        this.messageCallback(event.data);
    }
    private onError(event: Event){
        console.error(event);
        this.errorCallback(""+event);
    }
}
