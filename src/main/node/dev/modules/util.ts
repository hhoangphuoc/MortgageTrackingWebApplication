export function formatString(sql: string){
    return sql.split("\n")
        .map(line => JSON.stringify(line+"\n"))
        .join(" +\n");
}

export function snakeToCamel(name: string){
    const [first, ...rest] = name.split("_")
    return first + snakeToPascal(rest.join("_"));
}
export function snakeToPascal(name: string){
    return name.split("_").map(capitalize).join("");
}
export function snakeToCaps(name: string){
    return name.split("_").map((word) => word.toUpperCase()).join("_");
}

export function capitalize(word: string){
    return word.charAt(0).toUpperCase()  + word.slice(1);
}

export function tab(string: string){
    return string.split("\n").map(line => "    " + line).join("\n");
}
export function scope(string: string){
    return `{\n${tab(string)}\n}`;
}
