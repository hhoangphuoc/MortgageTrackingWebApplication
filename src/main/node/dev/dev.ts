import { daoClass } from "./modules/classes/daoClass";
import { modelClass } from "./modules/classes/modelClass";
import { parseQuery } from "./modules/parseQuery";
import { TableType } from "./modules/TypeDev";
import { snakeToPascal, tab } from "./modules/util";

import {mkdir, readFile, writeFile} from "fs";
import { modelContract } from "./modules/classes/modelContract";
import { tsModelClass } from "./modules/classes/tsModel";

const javaDir = "src/main/java/generated/";
const modelDir = "src/main/public/modules/api/generated/";
const sqlDir = "src/main/resources/pgsql/";

mkdir(javaDir, (err)=> {
    console.error(err);
})

readFile(`${sqlDir}createSchema.pgsql`, {encoding: "utf-8"}, (err, data) => {
    if (!err){
        generate(data);
    } else {
        console.error(err);
    }
});

function generate(data: string){
    const tables = parseQuery(data);
    for (const table of tables){
        const name = snakeToPascal(table.name);
        save(`${name}Dao.java`, daoClass(table, Object.keys(table.cols).slice(0, 1)));
        save(`${name}.java`, modelClass(table));
        save(`${name}.model.ts`, tsModelClass(table), modelDir);

    }
}
function save(filename: string, text: string, baseDir = javaDir){
    writeFile(`${baseDir}${filename}`, text, {encoding: "utf-8"}, (err) => {
        if (err){
            console.error(err);
        }
    });
}
