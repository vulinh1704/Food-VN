package com.food_vn.lib.base_serivce;

public class BaseService {
    public boolean isExist(Object _object) {
        return _object != null;
    }

    public void isAssert(boolean _criterial, String _msg) throws RuntimeException {
        if (_criterial) return;
        throw new RuntimeException(_msg);
    }
}
