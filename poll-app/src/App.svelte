<script>
    import { currentUser } from './stores';
    import CreateUser from './components/CreateUser.svelte';
    import CreatePoll from './components/CreatePoll.svelte';
    import VotePoll   from './components/VotePoll.svelte';

    let tab = 'vote';
    $: displayName = $currentUser?.username || '';
</script>

<svelte:head>
    <title>Poll App</title>
</svelte:head>

<div class="container">
    <h1>Create or vote on a poll</h1>
    {#if $currentUser}
        <p style="margin:.25rem 0 1rem">Hi, {displayName}!</p>
    {:else}
        <p style="margin:.25rem 0 1rem; color: var(--muted)">No user yet — create one below.</p>
    {/if}

    <div class="tabs">
        <button class="tab" aria-pressed={tab==='user'}   on:click={() => tab='user'}>User</button>
        <button class="tab" aria-pressed={tab==='create'} on:click={() => tab='create'}>Create</button>
        <button class="tab" aria-pressed={tab==='vote'}   on:click={() => tab='vote'}>Vote</button>
    </div>

    <div class="card">
        {#if tab === 'user'}     <CreateUser/>
        {:else if tab === 'create'} <CreatePoll/>
        {:else}                  <VotePoll/>
        {/if}
    </div>
</div>
