<script>
    import { currentUser } from '../stores';
    import { createUser } from '../api';

    let username = '';
    let email = '';

    async function submit() {
        const user = await createUser({ username, email });
        currentUser.set(user);
        username = ''; email = '';
    }
</script>

<h2>User</h2>

{#if $currentUser}
    <p>Signed in as <strong>{$currentUser.username}</strong>
        {#if $currentUser.email} ({$currentUser.email}){/if}.
    </p>
    <button class="btn" on:click={() => currentUser.set(null)}>Switch user</button>
{:else}
    <div class="stack">
        <input class="input" placeholder="Username" bind:value={username} />
        <input class="input" placeholder="Email (optional)" bind:value={email} />
        <div class="row" style="justify-content:flex-end">
            <button class="btn btn-primary" on:click={submit} disabled={!username.trim()}>
                Create user
            </button>
        </div>
    </div>
{/if}
