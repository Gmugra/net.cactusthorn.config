package net.cactusthorn.config.core;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import net.cactusthorn.config.core.converter.DurationConverter;
import net.cactusthorn.config.core.converter.ToTestConverter;

public final class ConfigBuilder_TestConfig extends ConfigBuilder<Config_TestConfig> {
  private static final Map<String, String> DEFAULTS;

  static {
    DEFAULTS = new HashMap<>();
    DEFAULTS.put("aaa", "ddd");
    DEFAULTS.put("dlist", "A,A");
    DEFAULTS.put("dlist2", "B,B");
    DEFAULTS.put("dset", "A,A");
    DEFAULTS.put("dset2", "B,B");
    DEFAULTS.put("dsort", "A,A");
    DEFAULTS.put("dsort2", "B,B");
    DEFAULTS.put("dstr", "A");
    DEFAULTS.put("dstr2", "B");
    DEFAULTS.put("testconverter", "default");
  }

  public ConfigBuilder_TestConfig(final ConfigHolder configHolder) {
    super(configHolder);
  }

  @Override
  public Config_TestConfig build() {
    CONVERTERS.computeIfAbsent(DurationConverter.class, c -> new DurationConverter());
    CONVERTERS.computeIfAbsent(ToTestConverter.class, c -> new ToTestConverter());
    Map<String,Object> values = new HashMap<>();
    values.put("aaa", get(s -> s, "aaa", DEFAULTS.get("aaa")));
    values.put("test.dlist", getList(s -> s, "test.dlist", ",", DEFAULTS.get("dlist")));
    values.put("test.dlist2", getList(s -> s, "test.dlist2", ",", DEFAULTS.get("dlist2")));
    values.put("test.dset", getSet(s -> s, "test.dset", ",", DEFAULTS.get("dset")));
    values.put("test.dset2", getSet(s -> s, "test.dset2", ",", DEFAULTS.get("dset2")));
    values.put("test.dsort", getSortedSet(s -> s, "test.dsort", ",", DEFAULTS.get("dsort")));
    values.put("test.dsort2", getSortedSet(s -> s, "test.dsort2", ",", DEFAULTS.get("dsort2")));
    values.put("test.dstr", get(s -> s, "test.dstr", DEFAULTS.get("dstr")));
    values.put("test.dstr2", get(s -> s, "test.dstr2", DEFAULTS.get("dstr2")));
    values.put("test.duration", getOptional(s -> convert(DurationConverter.class, s), "test.duration"));
    values.put("test.list", getList(s -> s, "test.list", ","));
    values.put("test.olist", getOptionalList(s -> s, "test.olist", ","));
    values.put("test.olist2", getOptionalList(s -> s, "test.olist2", ","));
    values.put("test.oset", getOptionalSet(s -> s, "test.oset", ","));
    values.put("test.oset2", getOptionalSet(s -> s, "test.oset2", ","));
    values.put("test.osort", getOptionalSortedSet(s -> s, "test.osort", ","));
    values.put("test.osort2", getOptionalSortedSet(s -> s, "test.osort2", ","));
    values.put("ostr", getOptional(s -> s, "ostr"));
    values.put("test.ostr1", getOptional(s -> s, "test.ostr1"));
    values.put("test.set", getSet(s -> s, "test.set", ","));
    values.put("test.sort", getSortedSet(s -> s, "test.sort", ","));
    values.put("test.string", get(s -> s, "test.string"));
    values.put("test.testconverter", get(s -> convert(ToTestConverter.class, s), "test.testconverter", "default"));
    return new Config_TestConfig(values);
  }
}
