package de.larmic.butterfaces.resolver;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MustacheResolverTest {

    @Test
    public void testGetMustacheKeysWithEmptyText() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys(null)).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("")).isEmpty();
    }

    @Test
    public void testGetMustacheKeysWithNoMatch() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys("test")).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("{test}")).isEmpty();
        assertThat(MustacheResolver.getMustacheKeys("{{test}")).isEmpty();
        //assertThat(MustacheResolver.getMustacheKeys("{test}}")).isEmpty(); // TODO fails
    }

    @Test
    public void testGetMustacheKeysWithSingleMatch() throws Exception {
        assertThat(MustacheResolver.getMustacheKeys("{{test}}")).containsExactly("test");
        assertThat(MustacheResolver.getMustacheKeys("{{test}}and{{test}}")).containsExactly("test");
    }

}