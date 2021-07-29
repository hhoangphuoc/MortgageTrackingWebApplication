import { Field, sqlToResult, TableType } from "../TypeDev";
import { snakeToCamel, snakeToPascal } from "../util";
import { getterName } from "./modelClass";

export function modelToStatement(def: TableType) {
    const modelInstance = snakeToCamel(def.name);
    const modelClass = snakeToPascal(def.name);
    return `public void modelToStatement
    (PreparedStatement statement, ${modelClass} ${modelInstance})
    throws SQLException
{
    modelToStatement(statement, ${modelInstance}, 1);
}
    
public void modelToStatement
    (PreparedStatement statement, ${modelClass} ${modelInstance}, int i)
    throws SQLException
{
    ${
        Object.entries(def.cols)
            .filter(([name, field]) => !field.type.startsWith("serial"))
            .map(fieldToStatement(modelInstance))
            .join("\n    ")
    }
}`;
}
export function fieldToStatement(objectName: string){
    return function ([name, {type}]: [string, Field]){
        const getter = getterName(name);
        const setter = "set" + sqlToResult[type];
        return `statement.${setter}(i++, ${objectName}.${getter}());`;
    }
}
