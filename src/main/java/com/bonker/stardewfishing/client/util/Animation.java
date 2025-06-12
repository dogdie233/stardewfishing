package com.bonker.stardewfishing.client.util;

import net.minecraft.util.Mth;

public class Animation {
    private float lastValue;
    private float value;
    private boolean frozen = false;

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
        if (frozen) return value;
        return Mth.lerp(partialTick, lastValue, value);
    }

    public void freeze() {
        frozen = true;
        lastValue = value;
    }

    public void reset(float value) {
        lastValue = value;
        this.value = value;
    }

    // floats are not precise enough for using repeating blit
    public static class Double {
        private double lastValue;
        private double value;
        private boolean frozen = false;

        public Double(double value) {
            this.lastValue = value;
            this.value = value;
        }

        public void setValue(double value) {
            this.lastValue = this.value;
            this.value = value;
        }

        public void addValue(double addition) {
            setValue(value + addition);
        }

        public void addValue(double addition, double min, double max) {
            setValue(Mth.clamp(value + addition, min, max));
        }

        public double getInterpolated(double partialTick) {
            if (frozen) return value;
            return Mth.lerp(partialTick, lastValue, value);
        }

        public void freeze() {
            frozen = true;
            lastValue = value;
        }

        public void reset(double value) {
            lastValue = value;
            this.value = value;
        }
    }
}
