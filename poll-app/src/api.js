const API = import.meta.env.VITE_API_URL || '';
console.log('[api] base =', API);

async function jfetch(path, init) {
    const res = await fetch(API + path, {
        headers: { 'Content-Type': 'application/json', ...(init?.headers || {}) },
        ...init,
    });
    if (!res.ok) throw new Error(await res.text().catch(()=>'Request failed'));
    return res.status === 204 ? null : res.json();
}

// users
export const createUser = (u) => jfetch('/users', { method: 'POST', body: JSON.stringify(u) });
export const listUsers  = () => jfetch('/users');

// polls
export const listPolls  = () => jfetch('/polls');
export function createPoll({ question, creatorId, options, publishedAt, validUntil }) {
    const dto = {
        question,
        creatorId,
        publishedAt: publishedAt ?? new Date().toISOString(),
        validUntil: validUntil ?? null,
        options: options.map((caption, i) => ({ caption, presentationOrder: i })),
    };
    return jfetch('/polls', { method: 'POST', body: JSON.stringify(dto) });
}

// votes
export const listVotes  = () => jfetch('/votes');
export const castVote   = (voterId, optionId) =>
    jfetch('/votes', { method: 'POST', body: JSON.stringify({ voterId, optionId }) });
