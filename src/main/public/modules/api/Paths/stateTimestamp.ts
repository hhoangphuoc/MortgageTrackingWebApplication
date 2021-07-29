import { myFetch } from "../myFetch";
import { errorText, neverText, okJson } from "../Response";
import { Status } from "../Status";

export class StateTimeStamp {
    m_id!: number;
    state!: string;
    started_time!: string;
    end_time!: string;
}

export async function getStateTimeStamp(m_id: number, state: string){
    const res = await myFetch(`./api/statetimestamp/${m_id}?state=${state}`);
    switch (res.status){
        case Status.OK: return okJson(res, StateTimeStamp, res.status);
        default: return neverText(res);
    }
}

export async function getTimePredictions(m_id: number) {
    const res = await myFetch(`./api/statetimestamp/${m_id}/predictions`);
    switch (res.status){
        case Status.OK: return okJson(res, [String], res.status);
        case Status.NOT_FOUND: return errorText(res, res.status);
        default: return neverText(res);
    }
}
