import { Field, sqlToJava, TableType } from "../TypeDev";
import { capitalize, scope, snakeToCamel, snakeToPascal } from "../util";
import { pack } from "./package";

export function modelClass(def: TableType) {
    const name = modelName(def.name);
    const fields = getFields(def);
    const getAndSets = getSetAndGets(def);
    return `${pack}\npublic class ${name} ${scope(`
public int size(){
    return ${attr(def).length};
}
public int insertSize(){
    return ${insertAttr(def).length};
}

${fields}\n\n${getAndSets}
`)}`;
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
    const jType = sqlToJava[type];
    const jName = name;
    return `private ${jType} ${jName};`;
}
function getSetAndGets(def: TableType) {
    return Object.entries(def.cols).map(getSetAndGet).join("\n\n");
}
function getSetAndGet(entry: [string, Field]) {
    return `${getGetter(entry)}
${getSetter(entry)}`;
}

export function getterName(name: string){
    return `get${capitalize(name)}`;
}
export function setterName(name: string){
    return `set${capitalize(name)}`;
}

function getGetter([name, { type }]: [string, Field]) {
    const jType = sqlToJava[type];
    const jName = name;
    const getName = getterName(name);
    return `public ${jType} ${getName}(){
    return this.${jName};
}`;
}
function getSetter([name, { type }]: [string, Field]) {
    const jType = sqlToJava[type];
    const jName = name;
    const setName = setterName(name);
    return `public void ${setName}(${jType} ${jName}){
    this.${jName} = ${jName};
}`;
}
