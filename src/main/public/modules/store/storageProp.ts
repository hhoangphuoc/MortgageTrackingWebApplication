import { mapProp } from "../annotations/mapProp";

export enum StorageType {LOCAL, SESSION}

interface StoragePropOptions {
    key?: string,
    type?: StorageType,
    timeToLive?: number
}
const expirePrefix = "expire_";

export function storageProp(options: StoragePropOptions = {}){
    const storage = options.type == StorageType.LOCAL ? localStorage : sessionStorage;
    return <T extends {}>(target: T, prop: keyof T & string) =>
    {
        const key = options.key ?? prop;
        const expireKey = expirePrefix + key;
        mapProp(target, prop, {
            get: (value) => {
                const fromStorage = storage.getItem(key);
                const expire = storage.getItem(expireKey);
                const expireTime = Number.parseInt(expire??"Infinity") || Infinity;
                
                const now = Date.now();
                storage.setItem(
                    expireKey,
                    `${now + (options?.timeToLive ?? Infinity)}`
                );
                if (expireTime < now){
                    target[prop as any] = undefined;
                    return undefined as any;
                }
                if (fromStorage != null){
                    try {
                        value = JSON.parse(fromStorage);
                    } catch {
                        console.error(value, "is not valid json");
                    }
                }
                return value;
            },
            set: (value) => {
                if (value === undefined || value === null){
                    storage.removeItem(key);
                } else {
                    storage.setItem(
                        expireKey,
                        `${Date.now() + (options?.timeToLive ?? Infinity)}`
                    );
                    storage.setItem(key, JSON.stringify(value));
                }
                return value;
            }
        });
    }
}
