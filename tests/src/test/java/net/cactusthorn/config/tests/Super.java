package net.cactusthorn.config.tests;

import java.io.Serializable;

import net.cactusthorn.config.core.Accessible;

public interface Super extends Serializable, Accessible {

    String superInterface();
}
