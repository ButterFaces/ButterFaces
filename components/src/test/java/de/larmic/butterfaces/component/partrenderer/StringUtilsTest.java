package de.larmic.butterfaces.component.partrenderer;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

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
    public void testConvertToCommaSeparated() throws Exception {
        final List<String> values = Arrays.asList("ding", "dong", "test", "me");
        assertThat(StringUtils.convertToCommaSeparated(values)).isEqualTo("ding, dong, test, me");
        assertThat(StringUtils.convertToCommaSeparated(values, false)).isEqualTo("ding, dong, test, me");
        assertThat(StringUtils.convertToCommaSeparated(values, true)).isEqualTo("'ding', 'dong', 'test', 'me'");
    }

    @Test
    public void testConvertToSeparated() throws Exception {
        final List<String> values = Arrays.asList("ding", "dong", "test", "me");
        assertThat(StringUtils.convertToSeparated(values)).isEqualTo("ding dong test me");
    }
}