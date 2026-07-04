package com.vibecode.agent.mcp;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.ArrayList;

public class McpToolRegistry {
    private final Map<String, ToolDefinition> definitions = new ConcurrentHashMap<>();
    private final Map<String, Function<Map<String, Object>, String>> handlers = new ConcurrentHashMap<>();

    public void registerTool(String name, String description,
                              Map<String, String> parameters,
                              Function<Map<String, Object>, String> handler) {
        definitions.put(name, new ToolDefinition(name, description, parameters));
        handlers.put(name, handler);
    }

    public List<ToolDefinition> listTools() {
        return List.copyOf(definitions.values());
    }

    public String invoke(String name, Map<String, Object> params) {
        var handler = handlers.get(name);
        if (handler == null) throw new ToolNotFoundException(name);
        return handler.apply(params);
    }
}
