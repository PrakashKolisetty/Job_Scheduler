'use client';
import React, { useState } from 'react';
import styles from './JobForm.module.css';

export default function JobForm() {
  const [form, setForm] = useState({
    name: '',
    type: 'ONE_TIME',
    cronExpression: '',
    scheduledTime: '',
    timeZone: 'Asia/Kolkata',
    kafkaTopic: '',
    kafkaMetadata: '',
    binaryFile: null,
  });

  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState('');

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    setForm({
      ...form,
      [name]: type === 'file' ? e.target.files[0] : value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setUploading(true);
    setMessage('');

    let binaryPath = null;
    if (form.binaryFile) {
      const formData = new FormData();
      formData.append('file', form.binaryFile);

      const uploadRes = await fetch('http://localhost:8080/api/jobs/upload', {
        method: 'POST',
        body: formData,
      });

      if (!uploadRes.ok) {
        setMessage('File upload failed');
        setUploading(false);
        return;
      }

      binaryPath = await uploadRes.text();
    }

    const payload = {
      name: form.name,
      type: form.type,
      cronExpression: form.cronExpression || null,
      scheduledTime: form.scheduledTime,
      timeZone: form.timeZone,
      kafkaTopic: form.kafkaTopic,
      kafkaMetadata: form.kafkaMetadata,
      binaryPath,
      status: 'SCHEDULED',
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString(),
    };

    const res = await fetch('http://localhost:8080/api/jobs', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
    });

    setUploading(false);
    if (res.ok) {
      setMessage('✅ Job created successfully!');
    } else {
      setMessage('❌ Failed to create job');
    }
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <label>Name</label>
      <input name="name" value={form.name} onChange={handleChange} required />

      <label>Type</label>
      <select name="type" value={form.type} onChange={handleChange}>
        <option value="ONE_TIME">One Time</option>
        <option value="RECURRING">Recurring</option>
        <option value="IMMEDIATE">Immediate</option>
        <option value="DELAYED_MESSAGE">Delayed Message</option>
      </select>

      <label>Scheduled Time</label>
      <input type="datetime-local" name="scheduledTime" value={form.scheduledTime} onChange={handleChange} required />

      <label>Time Zone</label>
      <input name="timeZone" value={form.timeZone} onChange={handleChange} required />

      <label>Cron Expression (if recurring)</label>
      <input name="cronExpression" value={form.cronExpression} onChange={handleChange} />

      <label>Kafka Topic</label>
      <input name="kafkaTopic" value={form.kafkaTopic} onChange={handleChange} required />

      <label>Kafka Metadata (JSON)</label>
      <textarea name="kafkaMetadata" value={form.kafkaMetadata} onChange={handleChange} required />

      <label>Binary File (optional)</label>
      <input type="file" name="binaryFile" onChange={handleChange} />

      <button type="submit" disabled={uploading}>
        {uploading ? 'Submitting...' : 'Create Job'}
      </button>

      {message && <p className={styles.message}>{message}</p>}
    </form>
  );
}
