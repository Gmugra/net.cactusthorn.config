package net.cactusthorn.config.core;

import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;
import net.cactusthorn.config.core.converter.Converter;
import net.cactusthorn.config.core.converter.standard.DurationConverter;
import net.cactusthorn.config.core.loader.ConfigHolder;
import net.cactusthorn.config.core.loader.LoadStrategy;
import net.cactusthorn.config.core.loader.Loaders;
import net.cactusthorn.config.core.util.ConfigBuilder;

public final class ConfigBuilder_TestConfig extends ConfigBuilder<Config_TestConfig> {
  private static final String[] URIS = new String[] {""};

  public ConfigBuilder_TestConfig(final Loaders loaders) {
    super(loaders);
  }

  @Override
  public Config_TestConfig build() {
    ConfigHolder ch = loaders().load(Config_TestConfig.class.getClassLoader(), LoadStrategy.UNKNOWN, URIS);
    CONVERTERS.computeIfAbsent(DurationConverter.class, c -> new DurationConverter());
    CONVERTERS.computeIfAbsent(ToTestConverter.class, c -> new ToTestConverter());
    Map<String,Object> values = new HashMap<>();
    values.put("aaa", ch.get(s -> s, "aaa", "ddd"));
    values.put("test.dlist", ch.getList(s -> s, "test.dlist", ",", "A,A"));
    values.put("test.dlist2", ch.getList(s -> s, "test.dlist2", ",", "B,B"));
    values.put("test.dset", ch.getSet(s -> s, "test.dset", ",", "A,A"));
    values.put("test.dset2", ch.getSet(s -> s, "test.dset2", ",", "B,B"));
    values.put("test.dsort", ch.getSortedSet(s -> s, "test.dsort", ",", "A,A"));
    values.put("test.dsort2", ch.getSortedSet(s -> s, "test.dsort2", ",", "B,B"));
    values.put("test.dstr", ch.get(s -> s, "test.dstr", "A"));
    values.put("test.dstr2", ch.get(s -> s, "test.dstr2", "B"));
    values.put("test.duration", ch.getOptional(s -> convert(DurationConverter.class, s, Converter.EMPTY), "test.duration"));
    values.put("test.list", ch.getList(s -> s, "test.list", ","));
    values.put("test.olist", ch.getOptionalList(s -> s, "test.olist", ","));
    values.put("test.olist2", ch.getOptionalList(s -> s, "test.olist2", ","));
    values.put("test.oset", ch.getOptionalSet(s -> s, "test.oset", ","));
    values.put("test.oset2", ch.getOptionalSet(s -> s, "test.oset2", ","));
    values.put("test.osort", ch.getOptionalSortedSet(s -> s, "test.osort", ","));
    values.put("test.osort2", ch.getOptionalSortedSet(s -> s, "test.osort2", ","));
    values.put("ostr", ch.getOptional(s -> s, "ostr"));
    values.put("test.ostr1", ch.getOptional(s -> s, "test.ostr1"));
    values.put("test.set", ch.getSet(s -> s, "test.set", ","));
    values.put("test.sort", ch.getSortedSet(s -> s, "test.sort", ","));
    values.put("test.string", ch.get(s -> s, "test.string"));
    values.put("test.testconverter", ch.get(s -> convert(ToTestConverter.class, s, Converter.EMPTY), "test.testconverter", "default"));
    return new Config_TestConfig(values);
  }
}
