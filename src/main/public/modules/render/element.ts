type TagMap = HTMLElementTagNameMap;
type TagNames = keyof TagMap;

export function element<T extends TagNames>(
    tag: T,
    properties?: Record<string, unknown>,
    ...children: (Node|string)[]
)
    : TagMap[T]
{
    const element = document.createElement(tag);

    if (properties){
        props(element, properties);
    }
    element.append(...children);

    return element;
}

function props(target: HTMLElement, props: Record<string, unknown>){
    for (const name in props){
        const prop = props[name];
        if (name in target){
            target[name] = prop;
        } else {
            target.setAttribute(name, ""+prop);
            if (typeof prop === "string"){
                console.warn("failed to set property", name ,"on", target, "to", prop)
            }
        }
    }
}
