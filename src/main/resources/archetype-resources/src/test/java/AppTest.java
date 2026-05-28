package ${package};

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    @Test
    void greetingContainsArtifactId() {
        assertEquals("Hello from ${artifactId}", App.greeting());
    }
}
