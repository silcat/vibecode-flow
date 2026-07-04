<template>
  <div class="app">
    <SessionList :sessions="sessions" :activeId="activeId" @select="select" @create="create" @delete="del" />
    <ChatView v-if="activeId" :sessionId="activeId" :key="activeId" @updated="loadSessions" />
    <div v-else class="empty">Select or create a session</div>
  </div>
</template>
<script>
import SessionList from "./components/SessionList.vue";
import ChatView from "./components/ChatView.vue";
export default {
  components: { SessionList, ChatView },
  data() { return { sessions: [], activeId: null }; },
  mounted() { this.loadSessions(); },
  methods: {
    async loadSessions() { const r = await fetch("/api/sessions"); const j = await r.json(); this.sessions = j.data || []; },
    async create() { const r = await fetch("/api/sessions", { method:"POST", headers:{"Content-Type":"application/json"}, body:JSON.stringify({title:"New Chat"}) }); const j = await r.json(); this.sessions.unshift(j.data); this.activeId = j.data.id; },
    select(id) { this.activeId = id; },
    async del(id) { await fetch("/api/sessions/"+id, { method:"DELETE" }); this.sessions = this.sessions.filter(s=>s.id!==id); if(this.activeId===id) this.activeId=null; }
  }
};
</script>
<style>
*{margin:0;padding:0;box-sizing:border-box}body{font-family:-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,sans-serif;background:#1a1a2e;color:#e0e0e0}.app{display:flex;height:100vh}.empty{flex:1;display:flex;align-items:center;justify-content:center;color:#666;font-size:14px}
</style>
