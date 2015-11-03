package de.larmic.butterfaces.component.partrenderer;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class StringUtilsTest {

   @Test
   public void testIsEmpty() throws Exception {
      Assertions.assertThat(StringUtils.isEmpty(null)).isTrue();
      Assertions.assertThat(StringUtils.isEmpty("")).isTrue();
      Assertions.assertThat(StringUtils.isEmpty(" ")).isFalse();
      Assertions.assertThat(StringUtils.isEmpty("foo")).isFalse();
   }

   @Test
   public void testIsNotEmpty() throws Exception {
      Assertions.assertThat(StringUtils.isNotEmpty(null)).isFalse();
      Assertions.assertThat(StringUtils.isNotEmpty("")).isFalse();
      Assertions.assertThat(StringUtils.isNotEmpty(" ")).isTrue();
      Assertions.assertThat(StringUtils.isNotEmpty("foo")).isTrue();
   }
}