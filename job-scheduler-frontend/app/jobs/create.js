'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import './CreateJob.css';

export default function CreateJobPage() {
  const router = useRouter();
  const [jobData, setJobData] = useState({
    name: '',
    type: 'ONE_TIME',
    scheduledTime: '',
    timeZone: 'Asia/Kolkata',
    kafkaTopic: '',
    kafkaMetadata: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setJobData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch('http://localhost:8080/api/jobs', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(jobData),
      });

      if (res.ok) {
        router.push('/jobs');
      } else {
        console.error('Failed to create job');
      }
    } catch (err) {
      console.error('Error:', err);
    }
  };

  return (
    <div className="form-wrapper">
      <h2>Create a New Job</h2>
      <form onSubmit={handleSubmit} className="job-form">
        <label>
          Name:
          <input type="text" name="name" value={jobData.name} onChange={handleChange} required />
        </label>

        <label>
          Type:
          <select name="type" value={jobData.type} onChange={handleChange}>
            <option value="ONE_TIME">ONE_TIME</option>
            <option value="RECURRING">RECURRING</option>
            <option value="HOURLY">HOURLY</option>
            <option value="DAILY">DAILY</option>
            <option value="WEEKLY">WEEKLY</option>
            <option value="MONTHLY">MONTHLY</option>
            <option value="DELAYED_MESSAGE">DELAYED_MESSAGE</option>
          </select>
        </label>

        <label>
          Scheduled Time:
          <input type="datetime-local" name="scheduledTime" value={jobData.scheduledTime} onChange={handleChange} />
        </label>

        <label>
          Time Zone:
          <input type="text" name="timeZone" value={jobData.timeZone} onChange={handleChange} />
        </label>

        <label>
          Kafka Topic:
          <input type="text" name="kafkaTopic" value={jobData.kafkaTopic} onChange={handleChange} />
        </label>

        <label>
          Kafka Metadata (JSON):
          <textarea name="kafkaMetadata" value={jobData.kafkaMetadata} onChange={handleChange}></textarea>
        </label>

        <button type="submit" className="submit-btn">Create Job</button>
      </form>
    </div>
  );
}
