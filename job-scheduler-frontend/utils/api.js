const API_BASE = 'http://localhost:8080/api/jobs';

export async function createJob(data) {
  const res = await fetch(API_BASE, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(data),
  });
  return await res.json();
}


export async function getJobById(id) {
    const res = await fetch(`${API_BASE}/${id}`);
    return await res.json();
  }
  