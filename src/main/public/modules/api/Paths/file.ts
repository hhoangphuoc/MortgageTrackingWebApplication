import { myFetch } from "../myFetch";
import { neverText, okJson } from "../Response";
import { Status } from "../Status";

export class FileMeta {
    file_id!: number;
    doc_id!: number;
    file_name!: string;
    file_type!: string;
}

export async function getFileMetaByDoc_id(doc_id: number){
    const res = await myFetch(`/api/file/meta/doc_id/${doc_id}`);
    switch (res.status){
        case Status.OK: return okJson(res, [FileMeta], res.status);
        default: return neverText(res);
    }
}
