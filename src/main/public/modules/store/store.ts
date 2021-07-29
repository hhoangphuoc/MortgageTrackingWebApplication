import { logProp } from "../annotations/logProp";
import { UserGet, userLogout } from "../api/Paths/user";
import { isDebug } from "../build";
import { ValueOf } from "../util";
import { Events, eventUserChanged } from "./Events";
import { setEventProp } from "./setEventProp";
import { storageProp, StorageType } from "./storageProp";

const millisecond = 1;
const second = 1000 * millisecond;
const minute = 60 * second;
const hour = 60 * minute;
const day = 24 * hour;
/**
 * This class will store the state
 * of the global single page application.
 * for example to store the current user that is logged in.
 */
class StoreState {
    @setEventProp({event: eventUserChanged})
    @storageProp({type: StorageType.LOCAL, timeToLive: day})
    userData: UserGet | undefined;
}

export const store = new StoreState();
