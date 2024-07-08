package com.bonker.stardewfishing.client;

import net.minecraft.util.Mth;

public class Animation {
    private float lastValue;
    private float value;

    public Animation(float value) {
        this.lastValue = value;
        this.value = value;
    }

    public void setValue(float value) {
        this.lastValue = this.value;
        this.value = value;
    }

    public void addValue(float addition) {
        setValue(value + addition);
    }

    public void addValue(float addition, float min, float max) {
        setValue(Mth.clamp(value + addition, min, max));
    }

    public float getInterpolated(float partialTick) {
        return Mth.lerp(partialTick, lastValue, value);
    }

    public void freeze() {
        lastValue = value;
    }
}
