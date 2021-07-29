import { TableType } from "../TypeDev";
import { scope, snakeToPascal } from "../util";
import { insertMethod } from "./insertMethod";
import { modelToStatement } from "./modelToStatement";
import { pack } from "./package";
import { resultToModel } from "./resultToModel";
import { selectMethod } from "./selectMethod";

export function daoClass(def: TableType, by:string[]){
    const modelName = snakeToPascal(def.name);
    const className = modelName + "Dao";
    const rToM = resultToModel(def);
    const mToS = modelToStatement(def);
    const selectors = by.map((b) => selectMethod(def, b)).join("\n\n\n");
    const insert = insertMethod(def);
    
    return `${pack}
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.MyPool;
import database.ConnectionFailed;
 
public class ${className} ${scope(
`
${singleton(className)}

${constructor(className)}

${mToS}
${rToM}
${selectors}

${insert}
`)
}`
}

function constructor(className: string){
return `MyPool pool;

public ${className}(){
    this.pool = MyPool.getInstance();
}`;
}

function singleton(className: string){
return `private static ${className} instance;
public static ${className} getInstance(){
    return instance;
}`
}
