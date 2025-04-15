export default function Home() {
  return (
    <main style={{ padding: '2rem' }}>
      <h1>Welcome to the Job Scheduler</h1>
      <p>
        Manage and automate your job executions with flexible scheduling and real-time Kafka integration.
      </p>
      <ul style={{ marginTop: '1rem' }}>
        <li>Schedule one-time, recurring, delayed, or immediate jobs</li>
        <li>Upload and execute `.jar` or `npm` projects</li>
        <li>View, monitor, and delete jobs easily</li>
      </ul>
    </main>
  );
}
