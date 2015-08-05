package com.konka.upgrade;

public class OneShotSignal {
    
    private static boolean ready = false;
    
    public synchronized static boolean isUpgradeReady() {
        return ready;
    }
    
    public synchronized static void setUpgradeReady() {
        ready = true;
    }
}
