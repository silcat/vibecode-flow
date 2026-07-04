package com.vibecode.agent.mcp;

public class ToolNotFoundException extends RuntimeException {
    public ToolNotFoundException(String name) { super("Tool not found: " + name); }
}
