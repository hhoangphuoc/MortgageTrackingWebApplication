import { Field, sqlToSql, TableType, Value } from "./TypeDev";

const createString = "CREATE TABLE ";

export function parseQuery(query: string){
    query = query;
    let tables: TableType[] = [];
    let i = 0;
    while (i >= 0){
        i = query.indexOf(createString, i);
        if (i == -1){
            break;
        }
        i += createString.length;
        const tableStart = query.indexOf("(", i);
        const name = query.slice(i, tableStart).trim().replace(/"/g, "");
        i = tableStart+1;
        const tableEnd = query.indexOf(");", i);
        const cols = parseCols(query.slice(i, tableEnd));
        i = tableEnd+1;
        const table = {
            name, cols
        }
        tables.push(table);
    }

    return tables;
}
function parseCols(query: string){
    const cols: Record<string, Field> = {};
    const lines = query.split(",");
    for (const line of lines){
        const trimmed = line.trim().replace(/  /g, " ");
        
        const [name, type] = trimmed.split(" ");
        const sqlType = sqlToSql[type as Value];
        if (sqlType != undefined){
            cols[name] = {type: sqlType} as Field;
        }
    }
    return cols;
}
