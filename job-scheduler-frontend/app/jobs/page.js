// app/jobs/page.js
'use client';

import { useEffect, useState } from 'react';
import JobCard from '../../components/JobCard';
import '../../styles/jobs.css';

export default function JobsPage() {
  const [jobs, setJobs] = useState([]);

  useEffect(() => {
    async function fetchJobs() {
      try {
        const response = await fetch('http://localhost:8080/api/jobs');
        const data = await response.json();
        setJobs(data);
      } catch (error) {
        console.error('Failed to fetch jobs:', error);
      }
    }

    fetchJobs();
  }, []);

  return (
    <div className="jobs-page">
      <h1 className="jobs-title">Scheduled Jobs</h1>
      <div className="jobs-grid">
        {jobs.length > 0 ? (
          jobs.map((job) => <JobCard key={job.id} job={job} />)
        ) : (
          <p className="no-jobs">No jobs scheduled yet.</p>
        )}
      </div>
    </div>
  );
}
