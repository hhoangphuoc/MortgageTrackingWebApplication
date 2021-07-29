import { APPLICATION_JSON } from "../../util";
import { MediaType } from "../MediaType";
import { Method, myFetch } from "../myFetch";
import {errorText, neverText as neverText, okJson} from "../Response";
import { Status } from "../Status";

const baseUrl = "api/processstate";

export class ProcessState {
    mortgageID!: number;
    state!: string;
    status!: string;

    constructor(mortgageID, state, status) {
        this.mortgageID = mortgageID;
        this.state = state;
        this.status = status;
    }
}

export async function 
    getProcessState(mortgageId: number)
{
    const res = await myFetch(`${baseUrl}/${mortgageId}`, {
        method: Method.GET,
        headers: {
            "Content-Type": APPLICATION_JSON
        }
    });
    switch (res.status){
        case Status.OK:
            console.log("Successful GET");
            return okJson(res, ProcessState, res.status);
        case Status.NOT_FOUND: return errorText(res, res.status);
        case Status.UNAUTHORIZED: return errorText(res, res.status);
        default: return neverText(res);
    }
}

export async function
    declineState(mortgageId: number)
{
    changeState(mortgageId, "Failed");
}

export async function
    acceptState(mortgageId: number)
{
    changeState(mortgageId, "Accepted");
}

export async function
    changeState(mortgageId: number, state: String) {
    const accept = new ProcessState(mortgageId, "Application", state);
    const res = await myFetch(`${baseUrl}/${mortgageId}`, {
        method: Method.PUT,
        body: JSON.stringify(accept),
        headers: {
            "Content-Type": APPLICATION_JSON
        }
    });
    switch (res.status){
        case Status.OK:
            console.log("Successful PUT: ", accept);
            return okJson(res, ProcessState, res.status);
        case Status.NOT_FOUND: return errorText(res, res.status);
        case Status.UNAUTHORIZED: return errorText(res, res.status);
        default: return neverText(res);
    }
}
export enum ProcessStateEnum {
    InterestOffer = "Interest Offer",
    BindingOffer = "Binding Offer",
    Application = "Application",
    DocumentsCheck ="Documents Check",
    Done = "Done",
    DeleteRequest = "Delete Request"
}
export enum ProcessStatusEnum {
    Processing = "Processing",
    ActionRequired = "Action Required",
    Failed = "Failed",
    Finished = "Finished"
}

export async function changeStatus(processState: ProcessState){
    //TODO: change processState.status to Processing, update to server:

    console.log(processState);
    
    const processStateURL = `./api/processstate/${processState.mortgageID}` ;
    const stateRes = await myFetch(processStateURL, {
        method: Method.PUT,
        body: JSON.stringify(processState),
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        }
    });
    switch (stateRes.status){
        case Status.NO_CONTENT: {
          console.log("No content");
        } break;
        default: {
            //TODO: handle error
            console.error("unexpected response", stateRes.status, stateRes.statusText,await stateRes.text(), stateRes);
        }
    }
  }
