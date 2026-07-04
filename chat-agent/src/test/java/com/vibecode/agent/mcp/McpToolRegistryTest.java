package com.vibecode.agent.mcp;

import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class McpToolRegistryTest {

    @Test
    void register_shouldMakeToolsAvailable() {
        var registry = new McpToolRegistry();
        registry.registerTool("get_weather", "Get weather for a city",
            Map.of("city", "string"), params -> "sunny");

        List<ToolDefinition> tools = registry.listTools();
        assertEquals(1, tools.size());
        assertEquals("get_weather", tools.get(0).name());
    }

    @Test
    void invoke_shouldCallRegisteredTool() {
        var registry = new McpToolRegistry();
        registry.registerTool("echo", "Echo input", Map.of("text", "string"),
            params -> "Echo: " + params.get("text"));

        String result = registry.invoke("echo", Map.of("text", "hello"));
        assertEquals("Echo: hello", result);
    }

    @Test
    void invoke_unknownTool_shouldThrow() {
        var registry = new McpToolRegistry();
        assertThrows(ToolNotFoundException.class,
            () -> registry.invoke("nonexistent", Map.of()));
    }
}
