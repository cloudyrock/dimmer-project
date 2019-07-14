package com.github.cloudyrock.dimmer;

import java.util.HashSet;
import java.util.Set;

public final class FeatureUpdateEvent {

    private final Set<String> featuresToggledOn;

    public FeatureUpdateEvent(Set<String> featuresToggledOn) {
        this.featuresToggledOn = featuresToggledOn;
    }

    public Set<String> getFeaturesToggledOn() {
        return new HashSet<>(featuresToggledOn);
    }

}