import { MediaType } from "../MediaType";
import { Method, myFetch } from "../myFetch";
import { errorText, neverText, okEmpty, okJson } from "../Response";
import { Status } from "../Status";

export enum EDocumentStatus {
    PENDING="Pending", SUBMITTED="Submitted", ACCEPTED="Accepted", REJECTED="Rejected"
}

export enum EDocumentType {
    GROSS_INCOME = "Gross Income",
    FINANCIAL_OBLIGATIONS = "Financial Obligations",
    COLLATERAL = "Collateral"
}

export class DocRequestCreate {
    mortgage_id!: number;
    document_type!: string;
}

export class DocumentRequest {
    doc_id!: number;
    mortgage_id!: number;
    document_type!: string;
    document_status!: EDocumentStatus;
    failed_explanation?: string;
}
export class DocRejection {
    doc_id!: number;
    failed_explanation?: string;
}


export async function getDocumentRequest(doc_id: number){
    const res = await myFetch(`api/document_request/doc_id/${doc_id}`);
    switch (res.status){
        case Status.OK: return okJson(res, DocumentRequest, res.status);
        case Status.NOT_FOUND: return errorText(res, res.status);
        default: return neverText(res);
    }
}

export async function getDocumentRequestsByM_id(m_id: number) {
    var res = await myFetch(`api/document_request/m_id/${m_id}`);
    switch (res.status){
        case Status.OK: return okJson(res, [DocumentRequest], res.status);
        default: return neverText(res);
    }
}

export async function postDocumentRequest(newDoc: DocRequestCreate) {
    var res = await myFetch(`api/document_request`, {
        method: Method.POST,
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        },
        body: JSON.stringify(newDoc)
    });
    switch (res.status){
        case Status.OK: return okJson(res, Number, res.status);
        case Status.NOT_FOUND: return errorText(res, res.status);
        default: return neverText(res);
    }
}

export async function patchDocumentRequestSubmit(doc_id: number){
    var res = await myFetch(`api/document_request/submit`, {
        method: Method.PATCH,
        body: JSON.stringify(doc_id)
    });
    switch (res.status){
        case Status.NO_CONTENT: return okEmpty(res, res.status)
        case Status.NOT_FOUND: return errorText(res, res.status);
        default: return neverText(res);
    }
}

export async function patchDocumentRequestAccept(doc_id: number){
    var res = await myFetch(`api/document_request/accept`, {
        method: Method.PATCH,
        body: JSON.stringify(doc_id)
    });
    switch (res.status){
        case Status.NO_CONTENT: return okEmpty(res, res.status)
        case Status.NOT_FOUND: return errorText(res, res.status);
        default: return neverText(res);
    }
}

export async function patchDocumentRequestReject
    (rejection: DocRejection)
{
    var res = await myFetch(`api/document_request/reject`, {
        method: Method.PATCH,
        headers: {
            "Content-Type": MediaType.APPLICATION_JSON
        },
        body: JSON.stringify(rejection)
    });
    switch (res.status){
        case Status.NO_CONTENT: return okEmpty(res, res.status)
        case Status.NOT_FOUND: return errorText(res, res.status);
        default: return neverText(res);
    }
}
