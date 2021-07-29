import { insertAttr, modelClass, modelName } from "./modelClass";
import { modelContract } from "./modelContract";
import { selectQuery } from "../queries/select";
import { Field, javaCols, sqlToJava, sqlToResult, TableType, Value } from "../TypeDev";
import { formatString, snakeToCamel, snakeToCaps, snakeToPascal, tab } from "../util";
import { fieldToStatement } from "./modelToStatement";
import { insertQuery, returningName } from "../queries/insert";
import { withConnection } from "./withConnectionMethod";

export function insertMethod(def: TableType){
    const jCols = javaCols(def.cols);
    const modelType = modelName(def.name);
    const ret = returningName(def);
    const JReturn = jCols[ret].type;
    const sqlReturn = def.cols[ret].type;
    const sql = insertQuery(def);
    const queryName = `INSERT_QUERY`;
    const FuncName = `insert`;
    return `${withConnection(JReturn, FuncName, modelType)}    
    
private final String ${queryName}
${tab("= " + formatString(sql))};

protected ${JReturn} _${FuncName}
    (Connection conn, ${modelType} model)
    throws SQLException
{
    var statement = conn.prepareStatement(${queryName});
    this.modelToStatement(statement, model);
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()){
        return resultSet.get${sqlToResult[sqlReturn]}(1);
    }
    return -1;
}`;
}
