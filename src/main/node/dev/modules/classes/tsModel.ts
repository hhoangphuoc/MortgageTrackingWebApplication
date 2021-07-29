import { Field, sqlToJs, TableType } from "../TypeDev";
import { scope, snakeToPascal } from "../util";

export function tsModelClass(def: TableType) {
    const name = modelName(def.name);
    const fields = getFields(def);
    return `export class ${name} ${scope(`${fields}`)}`;
}
export function modelName(name: string){
    return snakeToPascal(name);
}

export function attr(def: TableType){
    return Object.entries(def.cols);
}

export function insertAttr(def: TableType){
    return Object.entries(def.cols)
        .filter(([_, col]) => !col.type.startsWith("serial"))
}


function getFields(def: TableType) {
    return Object.entries(def.cols).map(getField).join("\n");
}
function getField([name, { type }]: [string, Field]) {
    const jType = sqlToJs[type];
    const jName = name;
    return `${jName}!: ${jType}`;
}
