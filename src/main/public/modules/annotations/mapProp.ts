interface GetAndSet<T> {
    get?: (value: T) => T,
    set?: (value: T) => T,
}
export function mapProp<T extends {}>
    (target: T, prop: keyof T, getAndSet: GetAndSet<T[typeof prop]>)
{
    const get = getAndSet.get ?? (value => value);
    const set = getAndSet.set ?? (value => value);
    const desc = Reflect.getOwnPropertyDescriptor(target, prop) ?? {};
    if (!desc.get && !desc.set){
        let _value = target[prop];
        Reflect.defineProperty(target, prop, {
            get: () => get(_value),
            set: (value: T[typeof prop]) => {
                _value = set(value);
            },
            configurable: true,
        });
    } else {
        Reflect.defineProperty(target, prop, {
            get: desc.get === undefined ? undefined
                : () => get(desc.get?.() as T[typeof prop]),
            set: desc.set == undefined ? undefined :
            (value: T[typeof prop]) => {
                desc.set?.(set(value));
            },
            configurable: true,
        });
    }
    
}
