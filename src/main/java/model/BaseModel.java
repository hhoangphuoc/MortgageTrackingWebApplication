package model;

import util.ObjectUtil;

public class BaseModel {
    @Override
    public String toString() {
        return ObjectUtil.objectToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){return true;}
        if (!(obj instanceof BaseModel)){
            return false;
        }
        return obj.toString().equals(this.toString());
    }
}
