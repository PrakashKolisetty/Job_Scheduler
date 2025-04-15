// components/Modal.js
import React from 'react';
import styles from './Modal.module.css';

export default function Modal({ title, message, onConfirm, onCancel }) {
  return (
    <div className={styles.overlay}>
      <div className={styles.modal}>
        <h3>{title}</h3>
        <p>{message}</p>
        <div className={styles.actions}>
          <button onClick={onConfirm} className={styles.confirm}>Delete</button>
          <button onClick={onCancel} className={styles.cancel}>Cancel</button>
        </div>
      </div>
    </div>
  );
}
