import { writable } from 'svelte/store';
import { listPolls, listVotes } from './api';

// persist current user in localStorage
const saved = typeof localStorage !== 'undefined' && localStorage.getItem('currentUser');
export const currentUser = writable(saved ? JSON.parse(saved) : null);
currentUser.subscribe(v => {
    if (typeof localStorage !== 'undefined') {
        if (v) localStorage.setItem('currentUser', JSON.stringify(v));
        else localStorage.removeItem('currentUser');
    }
});

export const polls = writable([]);
export const votes = writable([]);

export async function refreshPolls() { polls.set(await listPolls()); }
export async function refreshVotes() { votes.set(await listVotes()); }
