import { insertAttr } from "../classes/modelClass";
import { TableType } from "../TypeDev";

export function insertQuery(def: TableType){
    const entries = insertAttr(def);
    const ret = returningName(def);
    const names = entries.map(([name]) => name).join(", ");
    const values = entries.map(() => "?").join(", ");
    return `INSERT INTO ${def.name} 
(${names})
VALUES (${values})
RETURNING ${ret}`
}

export function returningName(def: TableType){
    return Object.keys(def.cols)[0];
}
export function returnType(def: TableType){
    return Object.values(def)[0];
}
