package test;

import net.cactusthorn.config.core.Config;
import java.util.List;
import java.util.ArrayList;

@Config interface WrongInterfaceArgWrongClass {

    @SuppressWarnings("rawtypes") List<ArrayList> map();
}
