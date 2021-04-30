package net.cactusthorn.config.core;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

public final class ConfigTestConfig implements TestConfig {

    private final String aaa;

    private final String str;
    private final Optional<String> ostr;
    private final Optional<String> ostr1;
    private final String dstr;
    private final String dstr2;

    private final List<String> list;
    private final Optional<List<String>> olist;
    private final Optional<List<String>> olist2;
    private final List<String> dlist;
    private final List<String> dlist2;

    private final Set<String> set;
    private final Optional<Set<String>> oset;
    private final Optional<Set<String>> oset2;
    private final Set<String> dset;
    private final Set<String> dset2;

    private final SortedSet<String> sort;
    private final Optional<SortedSet<String>> osort;
    private final Optional<SortedSet<String>> osort2;
    private final SortedSet<String> dsort;
    private final SortedSet<String> dsort2;

    private final Optional<URL> url;

    ConfigTestConfig(String aaa, String str, Optional<String> ostr, Optional<String> ostr1, String dstr, String dstr2, List<String> list,
            Optional<List<String>> olist, Optional<List<String>> olist2, List<String> dlist, List<String> dlist2, Set<String> set,
            Optional<Set<String>> oset, Optional<Set<String>> oset2, Set<String> dset, Set<String> dset2, SortedSet<String> sort,
            Optional<SortedSet<String>> osort, Optional<SortedSet<String>> osort2, SortedSet<String> dsort, SortedSet<String> dsort2,
            Optional<URL> url) {
        this.aaa = aaa;
        this.str = str;
        this.ostr = ostr;
        this.ostr1 = ostr1;
        this.dstr = dstr;
        this.dstr2 = dstr2;

        this.list = list;
        this.olist = olist;
        this.olist2 = olist2;
        this.dlist = dlist;
        this.dlist2 = dlist2;

        this.set = set;
        this.oset = oset;
        this.oset2 = oset2;
        this.dset = dset;
        this.dset2 = dset2;

        this.sort = sort;
        this.osort = osort;
        this.osort2 = osort2;
        this.dsort = dsort;
        this.dsort2 = dsort2;

        this.url = url;
    }

    @Override public String aaa() {
        return aaa;
    }

    @Override public String str() {
        return str;
    }

    @Override public Optional<String> ostr() {
        return ostr;
    }

    @Override public Optional<String> ostr1() {
        return ostr1;
    }

    @Override public String dstr() {
        return dstr;
    }

    @Override public String dstr2() {
        return dstr2;
    }

    @Override public List<String> list() {
        return list;
    }

    @Override public Optional<List<String>> olist() {
        return olist;
    }

    @Override public Optional<List<String>> olist2() {
        return olist2;
    }

    @Override public List<String> dlist() {
        return dlist;
    }

    @Override public List<String> dlist2() {
        return dlist2;
    }

    @Override public Set<String> set() {
        return set;
    }

    @Override public Optional<Set<String>> oset() {
        return oset;
    }

    @Override public Optional<Set<String>> oset2() {
        return oset2;
    }

    @Override public Set<String> dset() {
        return dset;
    }

    @Override public Set<String> dset2() {
        return dset2;
    }

    @Override public SortedSet<String> sort() {
        return sort;
    }

    @Override public Optional<SortedSet<String>> osort() {
        return osort;
    }

    @Override public Optional<SortedSet<String>> osort2() {
        return osort2;
    }

    @Override public SortedSet<String> dsort() {
        return dsort;
    }

    @Override public SortedSet<String> dsort2() {
        return dsort2;
    }

    @Override public Optional<URL> url() {
        return url;
    }
}
