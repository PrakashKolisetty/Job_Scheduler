// components/JobCard.js
import React from 'react';
import styles from './JobCard.module.css';
import Link from 'next/link';

export default function JobCard({ job }) {
  return (
    <div className={styles.card}>
      <h3>{job.name}</h3>
      <p><strong>Type:</strong> {job.type}</p>
      <p><strong>Status:</strong> {job.status}</p>
      <p><strong>Scheduled Time:</strong> {new Date(job.scheduledTime).toLocaleString()}</p>
      <Link href={`/jobs/${job.id}/details`} className={styles.detailsButton}>
        View Details
      </Link>
    </div>
  );
}
