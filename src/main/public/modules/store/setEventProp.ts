import { mapProp } from "../annotations/mapProp";
import { Events, eventStoreChanged } from "./Events";

interface SetEventOptions {
    event?: () => Event,
    eventTarget?: EventTarget
}

export function setEventProp(options: SetEventOptions = {}){
    const event = options.event ?? eventStoreChanged;
    const eventTarget = options.eventTarget ?? window;

    return <T extends {}>
        (target: T, prop: keyof T & string) =>
    {
        const desc = Reflect.getOwnPropertyDescriptor(target, prop) ?? {};

        Reflect.defineProperty(target, prop, {
            set(value){
                desc.set?.(value);
                eventTarget.dispatchEvent(event());
            }
        });
    }
}
