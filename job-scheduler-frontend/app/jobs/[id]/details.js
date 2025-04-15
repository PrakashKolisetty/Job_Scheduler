'use client';
import { useEffect, useState } from 'react';
import { useParams, useRouter } from 'next/navigation';
import styles from '../../styles/JobDetails.module.css';

export default function JobDetails() {
  const { id } = useParams();
  const router = useRouter();
  const [job, setJob] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchJob() {
      try {
        const res = await fetch(`http://localhost:8080/api/jobs/${id}`);
        const data = await res.json();
        setJob(data);
      } catch (err) {
        console.error('Failed to fetch job:', err);
      } finally {
        setLoading(false);
      }
    }

    if (id) fetchJob();
  }, [id]);

  const handleDelete = async () => {
    const confirmDelete = window.confirm('Are you sure you want to delete this job?');
    if (!confirmDelete) return;

    try {
      const res = await fetch(`http://localhost:8080/api/jobs/${id}`, {
        method: 'DELETE',
      });

      if (res.ok) {
        alert('Job deleted successfully');
        router.push('/jobs');
      } else {
        alert('Failed to delete job');
      }
    } catch (err) {
      console.error('Error deleting job:', err);
      alert('Something went wrong while deleting the job.');
    }
  };

  if (loading) return <p>Loading job details...</p>;
  if (!job) return <p>Job not found</p>;

  return (
    <div className={styles.detailsContainer}>
      <h1>{job.name}</h1>
      <p><strong>ID:</strong> {job.id}</p>
      <p><strong>Type:</strong> {job.type}</p>
      <p><strong>Status:</strong> {job.status}</p>
      <p><strong>Scheduled Time:</strong> {new Date(job.scheduledTime).toLocaleString()}</p>
      <p><strong>Time Zone:</strong> {job.timeZone}</p>
      <p><strong>Kafka Topic:</strong> {job.kafkaTopic}</p>
      <p><strong>Binary Path:</strong> {job.binaryPath || 'None'}</p>
      <p><strong>Kafka Metadata:</strong> <pre>{job.kafkaMetadata}</pre></p>

      <div className={styles.buttonGroup}>
        <button className={styles.backButton} onClick={() => router.push('/jobs')}>
          ⬅ Back to Jobs
        </button>
        <button className={styles.deleteButton} onClick={handleDelete}>
          🗑️ Delete Job
        </button>
      </div>
    </div>
  );
}
