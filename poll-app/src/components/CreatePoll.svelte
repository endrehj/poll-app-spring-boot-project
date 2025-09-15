<script>
    import { currentUser, refreshPolls } from '../stores';
    import { createPoll } from '../api';

    let question = '';
    let options = ['', ''];

    const addOption = () => options = [...options, ''];
    const removeOption = (i) => options = options.filter((_, idx) => idx !== i);

    async function submit() {
        const q = question.trim();
        const opts = options.map(o => o.trim()).filter(Boolean);
        if (!q || opts.length < 2) return alert('Add a question and at least two options.');
        if (!$currentUser) return alert('Create a user first.');

        await createPoll({
            question: q,
            creatorId: $currentUser.id,
            options: opts,
        });

        await refreshPolls();
        question = ''; options = ['', ''];
    }
</script>

<h2>Create Poll</h2>
<form class="stack" on:submit|preventDefault={submit}>
    <input class="input" placeholder="Question…" bind:value={question} />

    {#each options as opt, i}
        <div class="row">
            <input class="input" placeholder={`Option ${i+1}`} bind:value={options[i]} />
            {#if options.length > 2}
                <button type="button" class="btn btn-ghost" on:click={() => removeOption(i)}>Remove</button>
            {/if}
        </div>
    {/each}

    <div class="row" style="margin-top:.25rem">
        <button type="button" class="btn" on:click={addOption}>+ Add option</button>
        <div style="flex:1"></div>
        <button type="submit" class="btn btn-primary"
                disabled={!question.trim() || options.map(o=>o.trim()).filter(Boolean).length<2}>
            Create poll
        </button>
    </div>
</form>
