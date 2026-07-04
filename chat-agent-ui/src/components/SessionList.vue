<template>
  <div class="sidebar">
    <button class="new-btn" @click="$emit('create')">+ New Chat</button>
    <div class="list">
      <div v-for="s in sessions" :key="s.id" :class="['item',{active:s.id===activeId}]" @click="$emit('select',s.id)">
        <span class="title">{{ s.title||s.id }}</span>
        <button class="del" @click.stop="$emit('delete',s.id)">&times;</button>
      </div>
    </div>
  </div>
</template>
<script>
export default { props:{sessions:Array,activeId:String}, emits:["select","create","delete"] };
</script>
<style scoped>
.sidebar{width:260px;background:#16213e;display:flex;flex-direction:column;border-right:1px solid #0f3460}
.new-btn{margin:12px;padding:8px;background:#0f3460;color:#e0e0e0;border:none;border-radius:6px;cursor:pointer;font-size:13px}
.new-btn:hover{background:#1a4a8a}
.list{flex:1;overflow-y:auto}
.item{padding:10px 12px;cursor:pointer;display:flex;justify-content:space-between;align-items:center;border-bottom:1px solid #0f3460;font-size:13px}
.item:hover{background:#1a3050}
.item.active{background:#0f3460}
.title{overflow:hidden;text-overflow:ellipsis;white-space:nowrap;flex:1}
.del{background:none;border:none;color:#666;cursor:pointer;font-size:16px;padding:0 4px}
.del:hover{color:#e74c3c}
</style>
