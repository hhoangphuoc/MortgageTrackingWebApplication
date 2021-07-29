import { modelClass } from "./modelClass";
import { modelContract } from "./modelContract";
import { selectQuery } from "../queries/select";
import { Field, javaCols, sqlToJava, sqlToResult, TableType } from "../TypeDev";
import { formatString, snakeToCamel, snakeToCaps, snakeToPascal, tab } from "../util";
import { fieldToStatement } from "./modelToStatement";
import { withConnection } from "./withConnectionMethod";

export function selectMethod(def: TableType, by:string){
    const jCols = javaCols(def.cols);
    const byType = jCols[by]?.type ?? undefined;
    if (byType === undefined){
        console.log(jCols);
        const names = Object.keys(jCols).join(", ");
        throw new Error(`can't search by ${by} because it is not in the table ${def.name} (${names})`);
    }
    const setter = "set" + sqlToResult[def.cols[by].type];
    const sql = selectQuery(def, by);
    const queryName = `GET_BY_${snakeToCaps(by)}_QUERY`;
    const funcType = snakeToPascal(def.name);
    const FuncName = `getBy${snakeToPascal(by)}`;
    return `${withConnection(funcType, FuncName, byType)}
private final String ${queryName}
${tab("= " + formatString(sql))};

public ${funcType} _${FuncName}
    (Connection conn, ${byType} ${by})
    throws SQLException
{
    var statement = conn.prepareStatement(${queryName});
    statement.${setter}(1, ${by});
    ResultSet resultSet = statement.executeQuery();
    if (resultSet.next()){
        return resultToModel(resultSet);
    }
    return null;
}`;
}
