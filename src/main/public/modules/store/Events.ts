export enum Events {
    StoreChanged = "storeChanged",
    UserChanged = "userChanged"
}



export function eventStoreChanged(){
    return new CustomEvent(Events.StoreChanged);
} 
export function eventUserChanged(){
    return new CustomEvent(Events.UserChanged);
} 
