package de.larmic.butterfaces.component.partrenderer;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringUtilsTest {

   @Test
   public void testIsEmpty() throws Exception {
      assertThat(StringUtils.isEmpty(null)).isTrue();
      assertThat(StringUtils.isEmpty("")).isTrue();
      assertThat(StringUtils.isEmpty(" ")).isFalse();
      assertThat(StringUtils.isEmpty("foo")).isFalse();
   }

   @Test
   public void testIsNotEmpty() throws Exception {
      assertThat(StringUtils.isNotEmpty(null)).isFalse();
      assertThat(StringUtils.isNotEmpty("")).isFalse();
      assertThat(StringUtils.isNotEmpty(" ")).isTrue();
      assertThat(StringUtils.isNotEmpty("foo")).isTrue();
   }

    @Test
    public void testJoinWithCommaSeparator() throws Exception {
        final List<String> values = Arrays.asList("ding", "dong", "test", "me");
        assertThat(StringUtils.joinWithCommaSeparator(values)).isEqualTo("ding, dong, test, me");
        assertThat(StringUtils.joinWithCommaSeparator(values, false)).isEqualTo("ding, dong, test, me");
        assertThat(StringUtils.joinWithCommaSeparator(values, true)).isEqualTo("'ding', 'dong', 'test', 'me'");
    }

    @Test
    public void testJoinWithSpaceSeparator() throws Exception {
        final List<String> values = Arrays.asList("ding", "dong", "test", "me");
        assertThat(StringUtils.joinWithSpaceSeparator(values)).isEqualTo("ding dong test me");
    }

    @Test
    public void testJoin() throws Exception {
        final List<String> values = Arrays.asList("ding", "dong", "test", "me");
        assertThat(StringUtils.join(values, " | ", false)).isEqualTo("ding | dong | test | me");
        assertThat(StringUtils.join(values, " | ", true)).isEqualTo("'ding' | 'dong' | 'test' | 'me'");
    }
}