import { Field, sqlToJava, TableType } from "../TypeDev";
import { scope, snakeToCamel, snakeToPascal } from "../util";
import { pack } from "./package";

export function modelContract(def: TableType) {
    const name = "I" + snakeToPascal(def.name);
    const getAndSets = getSetAndGetsC(def);
    return `${pack}\ninterface ${name} ${scope(getAndSets)}`;
}
function getSetAndGetsC(def: TableType){
    return Object.entries(def.cols).map(getSetAndGetC).join("\n\n");
}

function getSetAndGetC(entry: [string, Field]){
    return `${getGetterC(entry)}
${getSetterC(entry)}`;
}

function getGetterC([name, {type}]: [string, Field]){
    const jType = sqlToJava[type];
    const sName = snakeToPascal(name);
    return `    ${jType} get${sName}();`;
}
function getSetterC([name, {type}]: [string, Field]){
    const jType = sqlToJava[type];
    const jName = snakeToCamel(name);
    const sName = snakeToPascal(name);
    return `    void set${sName}(${jType} ${jName});`;
}
