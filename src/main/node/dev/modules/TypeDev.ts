
export type JSON = Value | [Value] | Record<string, Value>
export type Value = keyof typeof sqlToJava;

export const sqlToSql = {
    "text": "text",
    "int4": "int4",
    "int8": "int8",
    "serial4": "serial4",
    "serial8": "serial8",
    "float4": "float4",
    "float8": "float8",
    "bool": "bool",
    "bytea": "bytea",
    "boolean": "bool",
    "integer": "int4",
    "int": "int4",
    "float": "float4",
    "float(53)": "float8",
    "serial": "serial4",
    "date": "date",
    "timestamp": "timestamp"
}

export const sqlToJs = {
    "text": "string",
    "int4": "number",
    "int8": "bigint",
    "serial4": "number",
    "serial8": "bigint",
    "float4": "number",
    "float8": "number",
    "bool": "boolean",
    "bytea": "Int8Array",
    "date": "string",
    "timestamp": "string"
} as const;

export const sqlToJava = {
    "text": "String",
    "int4": "int",
    "int8": "long",
    "serial4": "int",
    "serial8": "long",
    "float4": "float",
    "float8": "double",
    "bool": "boolean",
    "bytea": "byte[]",
    "date": "String", 
    "timestamp": "String" 
} as const;
export const sqlToResult = {
    "text": "String",
    "int4": "Int",
    "int8": "Long",
    "serial4": "Int",
    "serial8": "Long",
    "float4": "Float",
    "float8": "Double",
    "bool": "Boolean",
    "bytea": "Bytes",
    "date": "String",
    "timestamp": "String"
} as const;

export type Field = {type: Value}

export type TableType = {
    name: string;
    cols: Record<string, Field>;
}

type ApiPoint = {
    method: string,
    request: Message,
    response: Record<number, Message>
};
type Message = {
    body: JSON
}


export function javaCols(cols: Record<string, Field>){
    return Object.fromEntries(
        Object.entries(cols).map(([name, field]) =>
            [name, {...field, type: sqlToJava[field.type]}]
        )
    );
}
