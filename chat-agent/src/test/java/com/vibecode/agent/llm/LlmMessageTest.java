package com.vibecode.agent.llm;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class LlmMessageTest {

    @Test
    void factoryMethods_shouldSetCorrectRoles() {
        assertEquals("system", LlmMessage.system("sys").role());
        assertEquals("user", LlmMessage.user("usr").role());
        assertEquals("assistant", LlmMessage.assistant("asst").role());
    }

    @Test
    void constructor_shouldPreserveContent() {
        var msg = LlmMessage.user("hello world");
        assertEquals("hello world", msg.content());
    }
}
