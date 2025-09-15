<script>
    import { onMount } from 'svelte';
    import { polls, votes, refreshPolls, refreshVotes, currentUser } from '../stores';
    import { castVote } from '../api';

    let selectedId = null;

    onMount(async () => {
        await refreshPolls();
        await refreshVotes();
        if ($polls.length && !selectedId) selectedId = $polls[0].id;
    });

    // current poll and option ids
    $: current = ($polls.find(p => String(p.id) === String(selectedId))) ?? $polls[0];
    $: optionIds = new Set((current?.options || []).map(o => String(o.id)));

    // counts per optionId
    $: counts = (() => {
        const m = new Map();
        if (!current) return m;
        for (const v of $votes) {
            const oid = String(v?.option?.id ?? v?.optionId ?? '');
            if (optionIds.has(oid)) m.set(oid, (m.get(oid) || 0) + 1);
        }
        return m;
    })();

    async function vote(optionId) {
        if (!$currentUser) return alert('Create a user to vote.');
        await castVote($currentUser.id, optionId);
        await refreshVotes();
    }
</script>

<h2>Vote</h2>

{#if $polls.length === 0}
    <p>No polls yet — create one first.</p>
{:else}
    <div class="row" style="margin-bottom:.75rem">
        <label style="font-weight:600">Choose poll:</label>
        <select bind:value={selectedId}>
            {#each $polls as p}
                <option value={p.id}>{p.question}</option>
            {/each}
        </select>
    </div>

    {#if current}
        <div class="card" style="padding:.75rem 1rem">
            <div style="font-weight:700; font-size:1.05rem; text-align:center; margin:.25rem 0 1rem">
                “{current.question}”
            </div>

            {#each current.options as o}
                <div class="option-row">
                    <div>{o.caption ?? o.text}</div>
                    <button class="btn btn-success" on:click={() => vote(o.id)} disabled={!$currentUser}>
                        Vote
                    </button>
                    <span class="badge">{counts.get(String(o.id)) || 0} Votes</span>
                </div>
            {/each}
        </div>
    {/if}
{/if}
