<template>
  <div class="chat">
    <div class="msgs" ref="box">
      <div v-for="(m,i) in msgs" :key="i" :class="['msg',m.role]">
        <div class="label">{{ m.role==='user'?'You':'Agent' }}</div>
        <div class="content">{{ m.content }}</div>
      </div>
      <div v-if="streaming" class="msg assistant">
        <div class="label">Agent</div>
        <div class="content">{{ streamingText }}<span class="cursor">|</span></div>
      </div>
    </div>
    <div class="input">
      <input v-model="text" @keydown.enter="send" placeholder="Type a message..." :disabled="streaming" ref="inp" />
      <button @click="send" :disabled="streaming||!text.trim()">Send</button>
    </div>
  </div>
</template>
<script>
export default {
  props: { sessionId: String }, emits: ["updated"],
  data() { return { msgs:[], text:"", streaming:false, streamingText:"" }; },
  mounted() { this.$nextTick(()=>this.$refs.inp?.focus()); },
  methods: {
    async send() {
      const t = this.text.trim(); if(!t||this.streaming) return;
      this.msgs.push({role:"user",content:t}); this.text=""; this.streaming=true; this.streamingText="";
      try {
        const r = await fetch("/api/chat/"+this.sessionId, {method:"POST",headers:{"Content-Type":"application/json"},body:JSON.stringify({message:t})});
        const reader = r.body.getReader(); const dec = new TextDecoder();
        while(true){ const {done,value}=await reader.read(); if(done)break; const parts=dec.decode(value,{stream:true}).split("\n");
          for(const p of parts) if(p.startsWith("data:")) this.streamingText+=p.substring(5).trim(); }
      } catch(e) { this.streamingText="[Error: "+e.message+"]"; }
      this.msgs.push({role:"assistant",content:this.streamingText});
      this.streaming=false; this.streamingText=""; this.$nextTick(()=>this.scroll()); this.$emit("updated");
    },
    scroll() { const e=this.$refs.box; if(e) e.scrollTop=e.scrollHeight; }
  }
};
</script>
<style scoped>
.chat{flex:1;display:flex;flex-direction:column;max-width:800px;margin:0 auto;width:100%}
.msgs{flex:1;overflow-y:auto;padding:20px}
.msg{margin-bottom:16px;max-width:80%}.msg.user{margin-left:auto}.msg.user .content{background:#0f3460}.msg.assistant .content{background:#16213e}
.label{font-size:11px;color:#888;margin-bottom:4px;text-transform:uppercase}
.content{padding:10px 14px;border-radius:8px;font-size:14px;line-height:1.5;white-space:pre-wrap}
.cursor{animation:blink 1s infinite}@keyframes blink{50%{opacity:0}}
.input{display:flex;padding:12px 20px;border-top:1px solid #0f3460}
.input input{flex:1;padding:10px 14px;background:#16213e;border:1px solid #0f3460;border-radius:6px;color:#e0e0e0;font-size:14px;outline:none}
.input input:focus{border-color:#1a4a8a}
.input button{margin-left:8px;padding:10px 20px;background:#0f3460;color:#e0e0e0;border:none;border-radius:6px;cursor:pointer;font-size:13px}
.input button:hover{background:#1a4a8a}.input button:disabled{opacity:.4;cursor:default}
</style>
