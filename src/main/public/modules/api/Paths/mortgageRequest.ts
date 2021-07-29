import { myFetch } from "../myFetch";
import { neverText, okJson } from "../Response";
import { Status } from "../Status";
import { ProcessState } from "./processState";

export class Mortgage {
    m_id!: number;
    c_id!: number;
    s_id!: number;
}
export class MortgageProcessState {
    mortgageRequest: Mortgage;
    processState: ProcessState;
    date: String;
    customerName: String;

    constructor(mortgageRequest, processState, date, customerName) {
        this.mortgageRequest = mortgageRequest;
        this.processState = processState;
        this.customerName = customerName;
        this.date = date;
    }
}

export class MortgageData {
    m_id!: number;
    c_id!: number;
    s_id!: number;
    name!: String;
    surname!: String;
    state!: String;
    status!: String;
    date!: String;
}

export class InterestOffer {
    m_id!: number;
    duration !: string;
    interest_rate!: number;

    constructor(m_id: number,duration: string,interest){
        this.m_id = m_id;
        this.duration=duration;
        this.interest_rate=interest;
    }
}

export class DocumentCheck {
    m_id!: number;
    isFiniancialObligationsAccepted !: boolean;
    isCollateralAccepted!: boolean;
    isIncomeAccepted!: boolean;
    failedExplannation !: string;
    constructor(m_id: number,explanation: string,incomeAccepted: boolean,collateralAccepted: boolean,financialAccepted: boolean){
        this.m_id = m_id;
        this.failedExplannation =explanation;
        this.isIncomeAccepted =incomeAccepted;
        this.isCollateralAccepted = collateralAccepted;
        this.isFiniancialObligationsAccepted=financialAccepted;
    }
}

export async function getMortgageProcessStateByCustomer(customerID: number){
    const res = await myFetch(`/api/mortgagerequests/${customerID}/trackandtrace`);

    switch (res.status){
        case Status.OK: return okJson(res, MortgageProcessState, res.status);
        default: return neverText(res);
    }
}


export async function getMortgageProcessState(m_id: number){
    const res = await myFetch(`/api/mortgagerequests?m_id=${m_id}`);
    console.log(res, await res.text());
    switch (res.status){
        case Status.OK: return okJson(res, MortgageProcessState, res.status);
        default: return neverText(res);
    }
}

export async function getMortgageInterestOffer(m_id: number){
    console.log("Get mortgage interest in here");
    const res = await myFetch(`/api/interestoffer/${m_id}`);
    console.log(res);

    switch (res.status){
        case Status.OK: return okJson(res, InterestOffer, res.status);
        default: return neverText(res);
    }
}

export async function getMortgageDocumentCheck(m_id: number){
    console.log("Get mortgage interest in here");
    const res = await myFetch(`/api/mortgagecheck/${m_id}`);
    console.log(res);

    switch (res.status){
        case Status.OK: return okJson(res, DocumentCheck, res.status);
        default: return neverText(res);
    }
}

export async function getServiceProviderMortgages(spId: number) {
    const res = await myFetch(`/api/mortgagerequests/${spId}/sp`);

    switch (res.status) {
        case Status.OK:
            return okJson(res, [MortgageData], res.status);
        default:
            return neverText(res);
    }
}
