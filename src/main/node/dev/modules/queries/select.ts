import { TableType } from "../TypeDev";

export function selectQuery(def: TableType, by:string){
    const select = Object.entries(def.cols)
        .map(([name]) => name).join(", ");
    return `SELECT ${select}
FROM ${def.name}
WHERE ${by} = ?
LIMIT 1`;
}
export function selectAllQuery(def: TableType, by:string){
    const select = Object.entries(def.cols)
        .map(([name]) => name).join(", ");
    return `SELECT ${select}
FROM "${def.name}"`;
}
