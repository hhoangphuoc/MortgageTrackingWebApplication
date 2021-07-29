import { mapProp } from "../annotations/mapProp";
import { isDebug } from "../build";

export function logProp<T extends {}>
    (target: T, prop: keyof T)
{
    if (!isDebug()){
        return;
    }
    mapProp(target, prop, {
        get: (value) => {
            console.log("got property", prop, "with", value, "from", target)
            return value;
        },
        set: (value) => {
            console.log("set property", prop, "to", value, "on", target)
            return value;
        }
    });
}
