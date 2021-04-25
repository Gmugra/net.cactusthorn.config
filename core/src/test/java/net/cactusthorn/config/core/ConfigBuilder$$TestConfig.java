package net.cactusthorn.config.core;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.List;
import java.util.SortedSet;

public class ConfigBuilder$$TestConfig extends ConfigBuilder<ConfigTestConfig> {

    private enum Method {
        aaa, str, ostr, ostr1, dstr, dstr2, list, olist, olist2, dlist, dlist2, set, oset, oset2, dset, dset2, sort, osort, osort2, dsort,
        dsort2
    }

    private static final Map<Method, String> KEYS;
    static {
        KEYS = new EnumMap<>(Method.class);

        KEYS.put(Method.aaa, "aaa");
        KEYS.put(Method.str, "test.string");
        KEYS.put(Method.ostr, "ostr");
        KEYS.put(Method.ostr1, "test.ostr1");
        KEYS.put(Method.dstr, "test.dstr");
        KEYS.put(Method.dstr2, "test.dstr2");

        KEYS.put(Method.list, "test.list");
        KEYS.put(Method.olist, "test.olist");
        KEYS.put(Method.olist2, "test.olist2");
        KEYS.put(Method.dlist, "test.dlist");
        KEYS.put(Method.dlist2, "test.dlist2");

        KEYS.put(Method.set, "test.set");
        KEYS.put(Method.oset, "test.oset");
        KEYS.put(Method.oset2, "test.oset2");
        KEYS.put(Method.dset, "test.dset");
        KEYS.put(Method.dset2, "test.dset2");

        KEYS.put(Method.sort, "test.sort");
        KEYS.put(Method.osort, "test.osort");
        KEYS.put(Method.osort2, "test.osort2");
        KEYS.put(Method.dsort, "test.dsort");
        KEYS.put(Method.dsort2, "test.dsort2");
    }

    public ConfigBuilder$$TestConfig(final Map<String, String> properties) {
        super(properties);
    }

    @Override public ConfigTestConfig build() {

        String aaa = getDefault(s -> s, KEYS.get(Method.aaa), "ddd");
        String str = get(s -> s, KEYS.get(Method.str));
        Optional<String> ostr = getOptional(s -> s, KEYS.get(Method.ostr));
        Optional<String> ostr1 = getOptional(s -> s, KEYS.get(Method.ostr1));
        String dstr = getDefault(s -> s, KEYS.get(Method.dstr), "A");
        String dstr2 = getDefault(s -> s, KEYS.get(Method.dstr2), "B");

        List<String> list = getList(s -> s, KEYS.get(Method.list), ",");
        Optional<List<String>> olist = getOptionalList(s -> s, KEYS.get(Method.olist), ",");
        Optional<List<String>> olist2 = getOptionalList(s -> s, KEYS.get(Method.olist2), ",");
        List<String> dlist = getListDefault(s -> s, KEYS.get(Method.dlist), ",", "A,A");
        List<String> dlist2 = getListDefault(s -> s, KEYS.get(Method.dlist2), ",", "B,B");

        Set<String> set = getSet(s -> s, KEYS.get(Method.set), ",");
        Optional<Set<String>> oset = getOptionalSet(s -> s, KEYS.get(Method.oset), ",");
        Optional<Set<String>> oset2 = getOptionalSet(s -> s, KEYS.get(Method.oset2), ",");
        Set<String> dset = getSetDefault(s -> s, KEYS.get(Method.dset), ",", "A,A");
        Set<String> dset2 = getSetDefault(s -> s, KEYS.get(Method.dset2), ",", "B,B");

        SortedSet<String> sort = getSortedSet(s -> s, KEYS.get(Method.sort), ",");
        Optional<SortedSet<String>> osort = getOptionalSortedSet(s -> s, KEYS.get(Method.osort), ",");
        Optional<SortedSet<String>> osort2 = getOptionalSortedSet(s -> s, KEYS.get(Method.osort2), ",");
        SortedSet<String> dsort = getSortedSetDefault(s -> s, KEYS.get(Method.dsort), ",", "A,A");
        SortedSet<String> dsort2 = getSortedSetDefault(s -> s, KEYS.get(Method.dsort2), ",", "B,B");

        return new ConfigTestConfig(aaa, str, ostr, ostr1, dstr, dstr2, list, olist, olist2, dlist, dlist2, set, oset, oset2, dset, dset2,
                sort, osort, osort2, dsort, dsort2);
    }
}
