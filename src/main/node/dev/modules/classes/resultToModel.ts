import { Field, sqlToResult, TableType } from "../TypeDev";
import { scope, snakeToCamel, snakeToPascal } from "../util";
import { getterName, insertAttr, setterName } from "./modelClass";


export function resultToModel(def: TableType) {
    const funcType = snakeToPascal(def.name);
    const modelInstance = snakeToCamel(def.name);
    const modelClass = snakeToPascal(def.name);
    return `
public static ${funcType} resultToModel
    (ResultSet resultSet)
    throws SQLException
{
    return resultToModel(resultSet, 1);
}

public static ${funcType} resultToModel
    (ResultSet resultSet, int i)
    throws SQLException
${scope(
`var ${modelInstance} = new ${modelClass}();
${insertAttr(def).map(resultToField(modelInstance)).join("\n")}

return ${modelInstance};`
)}`;
}
export function resultToField(objectName: string){
    return function ([name, {type}]: [string, Field]){
        const getter = "get" + sqlToResult[type];
        const setter = setterName(name);
        return `${objectName}.${setter}(resultSet.${getter}(i++));`;
    }
}
