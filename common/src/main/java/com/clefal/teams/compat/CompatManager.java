package com.clefal.teams.compat;

import java.util.ArrayList;
import java.util.List;

public class CompatManager {
    public static final List<ICompatModule> compats = new ArrayList<>();

    public static void tryEnableAll(){
        for (ICompatModule compat : compats) {
            compat.tryEnable();
        }
    }

}
